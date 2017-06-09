// 注册响应(设备id,成功于否(0|1))
String send = Encoding.encodingCmd(1, 1, deviceId, 1);
// 插座就绪状态查询(设备id)
Encoding.encodingCmd(2, 12312);
// 开始充电请求(设备id，最小充电周期，最大充电时长) 单位分钟
Encoding.encodingCmd(3, 12312, 5, 20);
// 最小计费周期执行结束(设备id)
Encoding.encodingCmd(4, 12312);
// 订单生成接口上报(设备id)
Encoding.encodingCmd(5, 123456);



// 停止充电请求(设备id)
Encoding.encodingCmd(6, 123456);
// 心跳数据包响应(设备id,成功于否(0|1))
String send = Encoding.encodingCmd(7, 1, deviceId,1);
// 自动上报插座状态变更(设备id)
Encoding.encodingCmd(8, 123456);
// 查询设备好坏(设备id)
Encoding.encodingCmd(9, 123456);
// 查询插座所属机器投币的数量(设备id)
Encoding.encodingCmd(10, 123456);



// 查询插座所属机器的刷卡金额累积(设备id)
Encoding.encodingCmd(11, 123456);
// 清零插座所属机器的刷卡金额累积(设备id)
Encoding.encodingCmd(12, 123456);
// 查询插座所属机器类型(设备id)
Encoding.encodingCmd(13, 123456);
//设置带刷卡功能的设备的系统运行参数(id,消费模式[1,2,3],最小计费单位,费率1-4,功率1-4,刷卡预扣费,充满临界功率,判断检测延迟)
String cmd=encodingCmd(14, "686868",1,1,33,33,33,33,66,66,66,66,22,20,1);
// 读取插座所属机器（带刷卡功能）的运行参数(设备id)
Encoding.encodingCmd(15, 123456);



// 设置仅投币功能设备的系统运行参数(id,费率,正常功率值,系统运行的最大功率,充满临界功率,判断检测延迟)
String cmd = encodingCmd(16, "686868", 2, 12, 43, 54, 35);
// 读取带刷卡功能设备的系统运行运行参数(设备id)
Encoding.encodingCmd(17, 123456);
// 上传卡的使用情况明细响应(设备id,失败||成功【0,1】)
Encoding.encodingCmd(18, 123456, 0);
// 设备上锁(设备id)
Encoding.encodingCmd(19, 123456);
// 读取设备上锁状态(设备id)
Encoding.encodingCmd(20, 123456);



// 设备解锁(设备id)
Encoding.encodingCmd(21, 123456);
// 卡（微信）充值要求查询回应(设备id,卡的UID【8字节】,充值使能【0,1】,充值金额【4字节】)
Encoding.encodingCmd(22, 123456,654321,0,12);















