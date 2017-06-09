package com.fengtian.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.fengtian.utils.Encoding;
import com.fengtian.utils.MyDateUtils;
import com.fengtian.utils.MyUtils;
import com.fengtian.utils.ProtocolsUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * @author bsgs
 *
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger log = Logger.getLogger(TcpServerHandler.class);

	public static Map<String, ChannelHandlerContext> ctxs = new ConcurrentHashMap<String, ChannelHandlerContext>();

	/**
	 * 第一次连接调用
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		log.info("当前连接大小：" + ctxs.size());
		ctxs.put(UUID.randomUUID().toString(), ctx);
		log.info("设备上线：" + ctx.channel().remoteAddress().toString() + "	当前连接数：" + ctxs.size());
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String str = MyUtils.bytesToHexString(req);

		// 截取命令
		String packDir = str.substring(4, 6); // 包方向
		String length = str.substring(8, 16); // 数据长度
		String version = str.substring(16, 18); // 软件版本号
		String deviceId = str.substring(20, 36); // 设备id
		String data = str.substring(36, str.length() - 4); // 数据域
		String cmd = data.substring(0, 4);// 命令码
		String crc16 = str.substring(str.length() - 4);// crc16较验

		// 变量声明
		String chargingId = deviceId.substring(0, 14);// 充电桩id
		String outletNum = "";// 插座编号
		int outletQty = 0;// 插座数量
		String status = "";// 状态
		String checkResult = "";// 插座就绪状态
		String bStatus = "";// 结果
		String fullPwer = "";// 充满临界功率
		String delay = "";// 判断检测延迟
		String preFee = "";// 刷卡预扣费
		String maxPower = "";// 系统运行的最大功率

		switch (cmd) {
		case "0001":// 网络注册
			log.info("--------注册包 Start---------");
			log.info(str);
			outletQty = Integer.parseInt(data.substring(4, 6), 16);// 插座数量
			status = data.substring(6);// 所有状态
			log.info("所有插座状态：" + status);
			if (status.length() == outletQty * 2) {
				for (int i = 0; i < outletQty; i++) {
					String st = status.substring(i * 2, i * 2 + 2);
					String no = "";
					if (i + 1 < 10)
						no = "0";
					log.info("充电桩编号：" + chargingId + no + (i + 1) + "    状态:" + st);
				}
				String send = Encoding.encodingCmd(1, 1, deviceId, 1);
				log.info("响应数据：" + send);
				ByteBuf bb = Unpooled.copiedBuffer(MyUtils.hexStringToBytes(send));
				ctx.writeAndFlush(bb);
			} else {
				log.info("插座数量不对");
			}
			log.info("--------注册包 End-----------");
			break;
		case "0014":// 心跳数据包
			// log.info("----------心跳包 start------------");
			int bOutletQty = Integer.parseInt(data.substring(4, 6), 16);// 取出插座数量
			data = data.substring(6);// 插座数据状态
			if (bOutletQty != data.length() / 2) {
				log.info("心跳出错：插座数量与状态数量不符");
				return;
			}
			for (int i = 0; i < bOutletQty; i++) {
				String deviceId1 = chargingId + StringUtils.leftPad((i + 1) + "", 2, '0');// 插座编号
				String devStatus = data.substring(i * 2, i * 2 + 2);// 插座状态
				// log.info("心跳包-设备号：" + deviceId1 + " 设备状态：" + devStatus);
			}
			String send = Encoding.encodingCmd(7, 1, deviceId, 1);
			// log.info("响应数据：" + send);
			ByteBuf bb = Unpooled.copiedBuffer(MyUtils.hexStringToBytes(send));
			ctx.writeAndFlush(bb);
			// log.info("--------------心跳包 end-----------");
			break;
		default:
			log.info("收到消息：" + str);
			for (Map.Entry<String, ChannelHandlerContext> e : ctxs.entrySet()) {
				if (e.getValue() != ctx) {
					if (!e.getValue().channel().isActive()) {
						ctxs.remove(e.getKey());
						e.getValue().close();
						continue;
					}
					ByteBuf bfu = Unpooled.copiedBuffer(req);
					e.getValue().writeAndFlush(bfu);
				}
			}
		}

	}

	// 用户事件处理
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			// 判断连接是否为空
			// 获取key
			// 读取超时
			if (e.state() == IdleState.READER_IDLE) {
				log.info("设备掉线:" + ctx.channel().remoteAddress());
				// channel失效，从Map中移除
				for (Map.Entry<String, ChannelHandlerContext> en : ctxs.entrySet()) {
					if (en.getValue() != ctx)
						ctxs.remove(en.getValue());
				}
				ctx.close();
			}

		}
	}

	// 异常处理
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		Channel incoming = ctx.channel();
//		cause.printStackTrace();
		for (Map.Entry<String, ChannelHandlerContext> e : ctxs.entrySet()) {
			if (ctx == e.getValue())
				ctxs.remove(e.getKey());
		}
		log.info("设备掉线:" + incoming.remoteAddress().toString() + "   当前连接数：" + ctxs.size());
		ctx.close();
	}

}
