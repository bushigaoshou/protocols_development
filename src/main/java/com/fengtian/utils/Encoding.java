package com.fengtian.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author bsgs 命令编码
 */
public class Encoding {

	private static Logger log = Logger.getLogger(Encoding.class);

	// --------------------命令码---------------------
	private static final String CMD_LOGIN_NET = "0001"; // 网络注册
	// private static final String CMD_LOGOUT_NET = "0002"; //网络注销
	// private static final String CMD_OTHER_CHARGE_PACK = "0003"; //其他充电
	private static final String CMD_CHECK_COIN_QUANTITY = "0004"; // 查询机器投币累积
	private static final String CMD_CHECK_CARD_MONEY = "0005"; // 查询机器刷卡金额累积
	private static final String CMD_CLEAR_CARD_MONEY = "0006"; // 清零机器刷卡金额累积
	private static final String CMD_CHECK_DEV_TYPE = "0007"; // 查询机器设备类型
	// private static final String CMD_CARD_SETTING = "0008"; //设置带刷卡功能机器的运行参数
	private static final String CMD_CARD_SETTING_READ = "0009"; // 读取带刷卡功能机器的运行参数
	// private static final String CMD_COIN_SETTING = "000A"; //设置仅投币功能机器的运行参数
	private static final String CMD_COIN_SETTING_READ = "000B"; // 读取仅投币功能机器的运行参数
	private static final String CMD_CARD_USE_INFO = "000C"; // 上传用户卡的使用情况
	private static final String CMD_DEV_LOCK = "000D"; // 设备上锁
	private static final String CMD_READ_DEV_LOCK = "000E"; // 读取设备上锁状态
	private static final String CMD_DEV_UNLOCK = "000F"; // 设备解锁
	private static final String CMD_RECHARGE = "0010"; // 卡充值
	private static final String CMD_RECHARGE_ACK = "0011"; // 充值操作结果回应
	private static final String CMD_RD_BLACKLIST = "0012"; // 设备向服务器索取黑名单卡
	// private static final String CMD_BEAT_INTERVAL = "0013"; //获取或变更TCP通信心跳间隔
	private static final String CMD_HEART_BEAT_PACK = "0014"; // 心跳数据包
	private static final String CMD_USE_ORDER_INFO = "0015"; // 使用订单信息
	private static final String CMD_CHECK_READY = "0016"; // 查询插座是否就绪
	private static final String CMD_MIN_CYCLE_INFO = "0017"; // 返回、设置最小周期操作
	private static final String CMD_CHARGE_OVER = "0018"; // 停止充电
	// private static final String CMD_OUTLET_ALL_INFO = "0019"; //某个当前所有信息
	private static final String CMD_OUTLET_STATUS = "001A"; // 插座状态（开启、关闭、损坏）
	private static final String CMD_CHECK_OUTLET = "001B"; // 查询设备插口是否正常
	private static final String CMD_START_CHARGE_PACK = "001C"; // 准备充电
	// private static final String CMD_SET_OUTLET_PARA = "001D"; //设置设备配置参数命令码
	// private static final String CMD_SEND_BLACKLIST = "001E";
	// //服务器向设备下发挂失卡列表命令
	// private static final String CMD_CLEAR_BLACKLIST = "001F";
	// //设备向服务器请求将卡清除出挂失卡列表
	private static final String CMD_SERVER_OTA = "0020"; // 服务器向设备发起OTA升级请求
	private static final String CMD_DEVICE_OTA = "0021"; // 设备向服务器索取hex数据记录

	/**
	 * @param cmd
	 *            协议命令
	 */
	public static void format(String cmd) {
		cmd = cmd.toUpperCase();
		String s = "";
		if (cmd != null && !"".equals(cmd)) {
			for (int i = 0; i < cmd.length(); i++) {
				if (i != 0 && i % 2 == 0)
					s += " ";
				s += cmd.charAt(i);
			}
			log.info(s);
		}
	}

	/**
	 * @param objs
	 *            参数
	 * @return 协议命令
	 */
	public static String encodingCmd(Object... objs) {
		// --------------------命令字段声名 start--------------------
		String cmd = "";// 命令
		String header = "5153";// 帧头，2个字节，固定为0x51,0x53；
		String packDir = "";// 帧包方向，1个字节，0x00位请求包，0x01位响应包；
		String type = "01";// 帧包类型，1个字节，0x01为正常线上业务，0x02为调试业务；
		String length = "";// 后续数据字节个数，4个字节；
		String version = StringUtils.leftPad(Integer.toHexString((int) objs[1]), 2, '0');// 后台版本，1个字节；
		String reserved = "00";// 保留字段，1个字节；
		String deviceId = StringUtils.leftPad((String) objs[2], 16, '0');// 设备ID，8个字节；
		String data = "";// 数据段
		String crc16 = "";// 之前从帧头开始的所有字节的CRC16校验和，CRC16多项式0x1021；
		// --------------------命令字段声名 end--------------------
		// --------------------data字段参数 start--------------------

		// --------------------data字段参数 end--------------------
		// 前面固定18个长度
		if (objs == null || objs.length < 2)
			return "";
		switch ((int) objs[0]) {
		case 1:// 网络注册响应(设备id)
			packDir = "01";// 包方向
			data = CMD_LOGIN_NET + StringUtils.leftPad(objs[3] + "", 2, "0");
			break;
		case 2:// 插座就绪状态查询请求(设备id,插座编号)
			packDir = "00";// 包方向
			data = CMD_CHECK_READY + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0');
			break;
		case 3: // 开始充电请求(设备id，最小充电周期，最大充电时长) 单位分钟
			packDir = "00";// 包方向
			data = CMD_START_CHARGE_PACK + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[4]), 4, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[5]), 4, '0');
			break;
		case 4: // 最小充电计费周期执行结束字段响应(设备id,插座编号)
			packDir = "01";// 包方向
			data = CMD_MIN_CYCLE_INFO + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0') + "01";
			break;
		case 5: // 订单生成接口上报响应(设备id,状态[0表示失败，1表示成功])
			packDir = "01";// 包方向
			data = CMD_USE_ORDER_INFO + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0');
			break;
		case 6: // 停止充电请求(设备id,插座编号)
			packDir = "00";// 包方向
			data = CMD_CHARGE_OVER + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0');
			break;
		case 7: // 心跳数据包响应(设备id,状态[0表示失败，1表示成功])
			packDir = "01";// 包方向
			data = CMD_HEART_BEAT_PACK + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0');
			break;
		case 8: // 自动上报插座状态变更响应(设备id,插座编号,状态[0表示失败，1表示成功])
			packDir = "01";// 包方向
			data = CMD_OUTLET_STATUS + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[4]), 2, '0');
			break;
		case 9:// 查询设备好坏(设备id,插座编号)
			packDir = "00";// 包方向
			data = CMD_CHECK_OUTLET + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0');
			break;
		case 10:// 查询插座所属机器投币的数量(设备id)
			packDir = "00";// 包方向
			data = CMD_CHECK_COIN_QUANTITY;
			break;
		case 11:// 查询插座所属机器的刷卡金额累积(设备id)
			packDir = "00";// 包方向
			data = CMD_CHECK_CARD_MONEY;
			break;
		case 12:// 清零插座所属机器的刷卡金额累积(设备id)
			packDir = "00";// 包方向
			data = CMD_CLEAR_CARD_MONEY;
			break;
		case 13:// 查询插座所属机器类型(设备id)
			packDir = "00";// 包方向
			data = CMD_CHECK_DEV_TYPE;
			break;
		case 14:// 设置带刷卡功能的设备的系统运行参数(id,消费模式[1,2,3],最小计费单位,费率1-4,功率1-4,刷卡预扣费,充满临界功率,判断检测延迟)

			break;
		case 15:// 读取带刷卡功能设备的系统运行运行参数(设备id)
			packDir = "00";// 包方向
			data = CMD_CARD_SETTING_READ;
			break;
		case 16:// 设置仅投币功能设备的系统运行参数(id,费率,正常功率值,系统运行的最大功率,充满临界功率,判断检测延迟)
			break;
		case 17:// 读取仅投币功能设备的系统运行参数(设备id)
			packDir = "00";// 包方向
			data = CMD_COIN_SETTING_READ;
			break;
		case 18:// 上传卡的使用情况明细响应(设备id,状态[0表示失败，1表示成功])
			packDir = "01";// 包方向
			data = CMD_CARD_USE_INFO + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0');
			break;
		case 19:// 设备上锁(设备id)
			packDir = "00";// 包方向
			data = CMD_DEV_LOCK;
			break;
		case 20:// 读取设备上锁状态(设备id)
			packDir = "00";// 包方向
			data = CMD_READ_DEV_LOCK;
			break;
		case 21:// 设备解锁(设备id)
			packDir = "00";// 包方向
			data = CMD_DEV_UNLOCK;
			break;
		case 22:// IC卡（微信）充值要求查询响应(设备id,卡的UID【8字节】,充值使能【0,1】,充值金额【4字节】)
			packDir = "01";// 包方向
			data = CMD_RECHARGE + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 16, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[4]), 2, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[5]), 8, '0');
			break;
		case 23:// 卡（微信）充值结果上报后台响应(设备id,卡的UID【8字节】,状态[0表示失败，1表示成功])
			packDir = "01";// 包方向
			data = CMD_RECHARGE_ACK + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 16, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[4]), 2, '0');
			break;
		case 24:// 终端设备向服务器索取挂失IC卡列表响应(设备id,状态[0表示失败，1表示成功], 挂失卡的总数)
			packDir = "01";// 包方向
			data = CMD_RD_BLACKLIST + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 2, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[4]), 4, '0');
			break;
		case 25:// 服务器向设备请求OTA升级(设备id)
			packDir = "00";// 包方向
			data = CMD_SERVER_OTA;
			break;
		case 26:// 设备向服务器索取hex数据记录进行OTA升级响应(设备id,数据记录偏移,Hex数据记录行长度,Hex数据记录)
			packDir = "01";// 包方向
			data = CMD_DEVICE_OTA + StringUtils.leftPad(Integer.toHexString((int) objs[3]), 8, '0')
					+ StringUtils.leftPad(Integer.toHexString((int) objs[4]), 2, '0') + objs[5];
			break;
		}
		// log.info(data.length());
		// 计算数据域长度
		length = StringUtils.leftPad(Integer.toHexString(10 + data.length() / 2 + 2), 8, '0');
		// 命令组装
		cmd = header + packDir + type + length + version + reserved + deviceId + data;
		// CRC16较验
		crc16 = CRC16.CRC_XModem(MyUtils.hexStringToBytes(cmd));
		cmd += crc16;

		// format(cmd);
		// log.info(cmd);
		return cmd.toUpperCase();
	}

	public static void main(String[] args) {
		String cmd = "";
		// 订单生成接口上报响应(设备id,状态[0表示失败，1表示成功])
		// cmd=encodingCmd(5, 1, "1234567812345678", 1);
		// 停止充电请求(设备id,插座编号)
		// encodingCmd(6, 1, "1234567812345678", 17);
		// 心跳数据包响应(设备id,,状态[0表示失败，1表示成功])
		// encodingCmd(7, 1, "1234567812345678", 1);
		// 自动上报插座状态变更响应(设备id,插座编号,状态[0表示失败，1表示成功])
		// encodingCmd(8, 1, "1234567812345678", 19,0);
		// 查询设备好坏(设备id,插座编号)
		// encodingCmd(9, 1, "1234567812345678", 19);

		// 网络注册响应字段说明(设备id)
		encodingCmd(1, 1, "12345812345678");
		// 插座就绪状态查询请求字段说明(设备id,插座编号)
		encodingCmd(2, 1, "1234567812345678", 15);
		// 开始充电请求(设备id，最小充电周期，最大充电时长) 单位分钟
		encodingCmd(3, 1, "1234567812345678", 15, 20, 60);
	}
}
