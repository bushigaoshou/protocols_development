package com.fengtian.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 命令解码
 *
 * @author bsgs
 */
//@SuppressWarnings(value = {"all"})
public class Decoding {

    private static Logger log = Logger.getLogger(Decoding.class);

    // --------------------附录1：命令码定义---------------------
    private static final String CMD_LOGIN_NET = "0001";    //网络注册
    //    private static final String CMD_LOGOUT_NET = "0002";    //网络注销
//    private static final String CMD_OTHER_CHARGE_PACK = "0003";    //其他充电
    private static final String CMD_CHECK_COIN_QUANTITY = "0004";    //查询机器投币累积
    private static final String CMD_CHECK_CARD_MONEY = "0005";    //查询机器刷卡金额累积
    private static final String CMD_CLEAR_CARD_MONEY = "0006";    //清零机器刷卡金额累积
    private static final String CMD_CHECK_DEV_TYPE = "0007";    //查询机器设备类型
    private static final String CMD_CARD_SETTING = "0008";    //设置带刷卡功能机器的运行参数
    private static final String CMD_CARD_SETTING_READ = "0009";    //读取带刷卡功能机器的运行参数
    private static final String CMD_COIN_SETTING = "000A";    //设置仅投币功能机器的运行参数
    private static final String CMD_COIN_SETTING_READ = "000B";    //读取仅投币功能机器的运行参数
    private static final String CMD_CARD_USE_INFO = "000C";    //上传用户卡的使用情况
    private static final String CMD_DEV_LOCK = "000D";    //设备上锁
    private static final String CMD_READ_DEV_LOCK = "000E";    //读取设备上锁状态
    private static final String CMD_DEV_UNLOCK = "000F";    //设备解锁
    private static final String CMD_RECHARGE = "0010";    //卡充值
    private static final String CMD_RECHARGE_ACK = "0011";     //充值操作结果回应
    private static final String CMD_RD_BLACKLIST = "0012";     //设备向服务器索取黑名单卡
    //    private static final String CMD_BEAT_INTERVAL = "0013";    //获取或变更TCP通信心跳间隔
    private static final String CMD_HEART_BEAT_PACK = "0014";    //心跳数据包
    private static final String CMD_USE_ORDER_INFO = "0015";    //使用订单信息
    private static final String CMD_CHECK_READY = "0016";    //查询插座是否就绪
    private static final String CMD_MIN_CYCLE_INFO = "0017";    //返回、设置最小周期操作
    private static final String CMD_CHARGE_OVER = "0018";    //停止充电
    //    private static final String CMD_OUTLET_ALL_INFO = "0019";    //某个当前所有信息
    private static final String CMD_OUTLET_STATUS = "001A";    //插座状态（开启、关闭、损坏）
    private static final String CMD_CHECK_OUTLET = "001B";    //查询设备插口是否正常
    private static final String CMD_START_CHARGE_PACK = "001C";     //准备充电
    //    private static final String CMD_SET_OUTLET_PARA = "001D";     //设置设备配置参数命令码
//    private static final String CMD_SEND_BLACKLIST = "001E";    //服务器向设备下发挂失卡列表命令
//    private static final String CMD_CLEAR_BLACKLIST = "001F";    //设备向服务器请求将卡清除出挂失卡列表
    private static final String CMD_SERVER_OTA = "0020";    //服务器向设备发起OTA升级请求
    private static final String CMD_DEVICE_OTA = "0021";    //设备向服务器索取hex数据记录

    //------------------附录2：状态码--------------
//    private static final String NORMAL_STAT = "00";    //插座正常空闲状态
//    private static final String REPARE_STAT = "01";    //插座维修或者迁移状态
//    private static final String CHARGE_STAT = "02";    //插座充电状态
//    private static final String FORBID_STAT = "03";    //插座禁用状态


    public static void decondingCmd(String str) {
        // 截取命令
        String packDir = str.substring(4, 6); // 包方向
        String length = str.substring(8, 16); // 数据长度
        String version = str.substring(16, 18); // 软件版本号
        String deviceId = str.substring(20, 36); // 设备id
        String data = str.substring(36, str.length() - 4); // 数据域
        String cmd = data.substring(0, 4);//命令码
        String crc16 = str.substring(str.length() - 4);//crc16较验


        log.info(StringUtils.rightPad("数据长度:" + length, 20, ' ') +
                StringUtils.rightPad("版本号:" + version, 12, ' ') + "设备号:" + deviceId);
        log.info(StringUtils.rightPad("数据区域:" + data, 20, ' ') +
                StringUtils.rightPad("命令码:" + cmd, 12, ' ') + "CRC较验:" + crc16);

        // 变量声明
        String chargingId = deviceId.substring(0, 14);//充电桩id
        String outletNum = "";//插座编号
        int outletQty = 0;// 插座数量
        String status = "";// 状态
        String checkResult = "";//插座就绪状态
        String bStatus = "";//结果
        String fullPwer = "";// 充满临界功率
        String delay = "";// 判断检测延迟
        String preFee = "";// 刷卡预扣费
        String maxPower = "";// 系统运行的最大功率

        switch (cmd) {
            case CMD_LOGIN_NET:// 网络注册
                outletQty = Integer.parseInt(data.substring(4, 6), 16);//插座数量
                status = data.substring(6);//所有状态
                log.info("所有插座状态：" + status);
                if (status.length() == outletQty * 2) {
                    for (int i = 0; i < outletQty; i++) {
                        String st = status.substring(i * 2, i * 2 + 2);
                        log.info("充电桩编号：" + chargingId + "    状态:" + st);
                    }
                } else {
                    log.info("插座数量不对");
                }

                break;
            case CMD_CHECK_READY:// 插座就绪状态查询请求
                checkResult = data.substring(4, 6);
                log.info("设备号:" + deviceId + "  状态：" + checkResult);
                break;

            case CMD_START_CHARGE_PACK:// 开始充电
                outletNum = data.substring(4, 6);//编号
                status = data.substring(6, 8);//状态
                log.info("设备号:" + deviceId + "   插座编号：" + outletNum + "  状态：" + status);
                break;

            case CMD_MIN_CYCLE_INFO:// 最小计费周期执行结束
                outletNum = data.substring(4, 6);
                String sCycleNum = data.substring(6, 10);// 时间片序号，编号从1开始
                String fCyclePower = data.substring(10, 18);// 当前时间片周期的平均功耗，浮点数(瓦W)
                String fCycleCurrent = data.substring(18, 26);// 当前时间片周期的平均电流，浮点数（安）
                String fCycleEnergy = data.substring(26, 34);// 当前时间片内使用的电量，浮点数（度）
                log.info("设备号:" + deviceId + "  时间片序号：" + sCycleNum + "  当前时间片平均功耗："
                        + fCyclePower + "  当前时间片电流：" + fCycleCurrent + "  当前时间片内使用的电量："
                        + fCycleEnergy);
                break;

            case CMD_USE_ORDER_INFO:// 订单生成接口
                String bOutletUseResult = data.substring(4, 6);// 运行结果
                String dTotalUseTime = data.substring(6, 14);// 总耗时(分)
                String fEnergy = data.substring(14, 22);// 总耗电（度）
                String fMaxActivePower = data.substring(22, 30);// 充电过程中的最大功率值（瓦）
                String fCurrentTimeEnergy = data.substring(30, 38);// 上一个时间片结束到当前时间电量值（度）
                log.info("设备号:" + deviceId);
                break;

            case CMD_CHARGE_OVER:// 停止充电请求
                outletNum = data.substring(4, 6);// 插座编号
                bStatus = data.substring(6, 8);//结果（1表示执行成功，0表示失败)
                log.info("设备号:" + deviceId + "  操作状态：" + bStatus);
                break;

            case CMD_HEART_BEAT_PACK:// 心跳数据包
                int bOutletQty = Integer.parseInt(data.substring(4, 6), 16);// 取出插座数量
                data = data.substring(6);// 插座数据状态
                if (bOutletQty != data.length() / 2) {
                    log.info("心跳出错：插座数量与状态数量不符");
                    return;
                }
                for (int i = 0; i < bOutletQty; i++) {
                    String deviceId1 = chargingId + StringUtils.leftPad((i + 1) + "", 2, '0');// 插座编号
                    String devStatus = data.substring(i * 2, i * 2 + 2);// 插座状态
                    log.info("心跳包-设备号：" + deviceId1 + " 设备状态：" + devStatus);
                }
                break;

            case CMD_OUTLET_STATUS:// 自动上报插座状态变更
                outletNum = data.substring(4, 6);//插座编号
                status = data.substring(6, 8);//设备状态
                log.info("设备号:" + deviceId + "   插座编号：" + outletNum + "  插座状态：" + status);
                break;

            case CMD_CHECK_OUTLET:// 查询设备好坏
                outletNum = data.substring(4, 6);//插座编号
                String bOutletStatus = data.substring(6, 8);//插座检测结果
                log.info("设备号:" + deviceId + "   插座编号：" + outletNum + "  插座状态：" + bOutletStatus);
                break;

            case CMD_CHECK_COIN_QUANTITY:// 查询插座所属机器投币的数量
                bStatus = data.substring(4, 6);// 执行结果状态 - 0表示成功，1表示失败，设备不支持
                String bCoinNum = data.substring(6, 10);// 设备投币数 -执行结果失败时，该字段无效
                log.info("设备号:" + chargingId + "  执行结果状态：" + bStatus + "  设备投币数：" + bCoinNum);
                break;

            case CMD_CHECK_CARD_MONEY:// 查询插座所属机器的刷卡金额累积
                bStatus = data.substring(4, 6);// 执行结果状态 - 0表示成功，1表示失败，设备不支持
                String dMoney = data.substring(6, 10);// 刷卡金额累积（单位：分）--执行结果失败时，该字段无效
                log.info("设备号:" + deviceId + "  执行结果状态：" + bStatus + "  金额：" + dMoney);
                break;

            case CMD_CLEAR_CARD_MONEY:// 清零插座所属机器的刷卡金额累积响应
                bStatus = data.substring(4, 6);// 执行结果状态 - 0表示成功，1表示失败，设备不支持
                log.info("设备号:" + deviceId + "  执行结果状态：" + bStatus);
                break;

            case CMD_CHECK_DEV_TYPE:// 查询插座所属机器类型响应
                status = data.substring(4, 6);// 执行结果状态 - 0表示成功，1表示失败，设备不支持
                String bDevType = data.substring(6, 8);//1表示仅刷卡设备，2表示仅投币设备，3表示刷卡投币设备
                log.info("设备号:" + deviceId + "  执行结果状态：" + status + "  设备类型：" + bDevType);
                break;

            case CMD_CARD_SETTING:// 设置带刷卡功能的设备的系统运行参数
                bStatus = data.substring(4, 6);// 执行结果状态 - 0表示成功，1表示失败，设备不支持
                log.info("设备号:" + deviceId + "  操作状态：" + bStatus);
                break;

            case CMD_CARD_SETTING_READ:// 读取带刷卡功能设备的系统运行参数响应
                bStatus = data.substring(4, 6);// 执行结果状态--0表示读取成功，1表示读取失败
                String consumeMode = data.substring(6, 8);// 消费模式--1,计时；2,计量；3,包月
                String minUnit = data.substring(8, 12);// 最小计费单位
                String rate1 = data.substring(12, 20);// 费率等级1
                String rate2 = data.substring(20, 28);// 费率等级2
                String rate3 = data.substring(28, 36);// 费率等级3
                String rate4 = data.substring(36, 44);// 费率等级4
                String pwClass1 = data.substring(44, 48);// 功率等级1
                String pwClass2 = data.substring(48, 52);// 功率等级2
                String pwClass3 = data.substring(52, 56);// 功率等级3
                String pwClass4 = data.substring(56, 60);// 功率等级4
                preFee = data.substring(60, 68);// 刷卡预扣费( 分)
                fullPwer = data.substring(68, 70);// 充满临界功率
                delay = data.substring(70, 74);// 判断检测延迟
                log.info("设备号:" + deviceId + "  操作状态：" + data);
                break;

            case CMD_COIN_SETTING:// 设置仅投币功能设备的系统运行参数响应
                status = data.substring(4, 6);//1表示设置成功，0表示设置失败，不支持
                log.info("设备号:" + deviceId + "  操作状态：" + status);
                break;

            case CMD_COIN_SETTING_READ:// 读取仅投币功能设备的系统运行参数响应
                String rate = data.substring(4, 8);// 费率，表示一个硬币对应的充电时间，单位分钟；
                String pwClass = data.substring(8, 12);// 正常功率值
                maxPower = data.substring(12, 16);// 系统运行的最大功率
                fullPwer = data.substring(16, 18);// 充满临界功率
                delay = data.substring(18, 22);// 判断检测延迟 单位，秒
                log.info("设备号:" + deviceId + "  操作状态：" + data);
                break;

            case CMD_CARD_USE_INFO:// 上传卡的使用情况明细
                String uid = data.substring(4, 20);// 卡的UID
                String cardState = data.substring(20, 22);// 卡的使用状态--1，刷卡充电使用；0，刷卡退费停充
                String cardCurMoney = data.substring(22, 30);// 卡的当前剩余金额 --单位，元
                preFee = data.substring(30, 38);// 卡的当前预扣费累计 --单位，元
                String retFee = data.substring(38, 46);// 卡的刷卡退费返回金额--单位，元，在刷卡充电时，该字段不用
                String usedFee = data.substring(46, 54);// 充电结束后，使用的全部费用--单位，元，其值=preFee-retFee，在刷卡充电时，该字段不用
                maxPower = data.substring(54, 62);// 充电过程最大功率--单位，瓦特（W），在刷卡充电时，该字段不用

                log.info("设备号:" + deviceId + "  操作状态：" + data);
                break;

            case CMD_DEV_LOCK:// 设备上锁响应
                bStatus = data.substring(4, 6);
                log.info("设备号:" + deviceId + "  操作状态：" + bStatus);
                break;

            case CMD_READ_DEV_LOCK:// 读取设备上锁状态响应
                status = data.substring(4, 6);// 1，已上锁；0，未上锁
                log.info("设备号:" + deviceId + "  操作状态：" + status);
                break;

            case CMD_DEV_UNLOCK:// 设备解锁响应
                status = data.substring(4, 6);//1，解锁成功；0，解锁失败
                // 1，解锁成功；0，解锁失败
                log.info("设备号:" + deviceId + "  操作状态：" + status);
                break;

            case CMD_RECHARGE:// IC卡（微信）充值要求查询
                uid = data.substring(4, 20);//卡的UID
                log.info("设备号:" + deviceId + "  UID卡号：" + uid);
                break;

            case CMD_RECHARGE_ACK:// 卡（微信）充值结果上报后台
                uid = data.substring(4, 20);// 卡的UID
                status = data.substring(20, 22);// 充值结果状态--0表示充值成功；1，表示充值失败
                log.info("设备号:" + deviceId + "  UID卡号：" + uid + "   充值结果" + status);
                break;

            case CMD_RD_BLACKLIST:// 终端设备向服务器索取挂失IC卡列表
                log.info("获取挂失卡列表请求");
                break;

            case CMD_SERVER_OTA:// 服务器向设备请求OTA升级响应
                status = data.substring(4, 6);//执行结果 1表示设备就绪，0表示设备忙碌
                log.info("");
                break;

            case CMD_DEVICE_OTA:// 设备向服务器索取hex数据记录进行OTA升级
                String offset = data.substring(4, 12);//数据记录偏移
                log.info(offset);
                break;
        }
    }

    public static void main(String[] args) {
        String cmd = "";
        cmd = "005101010000000F010000123458123456780001010017";
        decondingCmd(cmd);

    }

}
