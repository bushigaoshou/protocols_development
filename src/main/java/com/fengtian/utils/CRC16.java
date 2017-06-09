package com.fengtian.utils;

/**
 * CRC16相关计算
 * <p>
 * encode: utf-8
 *
 * @author trb
 * @date 2014-12-25
 */

/**
 * CRC16相关计算
 * <p>
 * encode: utf-8
 *
 * @author trb
 * @date 2014-12-25
 */
public class CRC16 {

    /**
     * CRC_XModem
     *
     * @param bytes 校验byte数组
     * @return 反回字符串结果
     */
    public static String CRC_XModem(byte[] bytes) {
        int crc = 0x00;// initial value
        int polynomial = 0x1021;//多项式
        int len = bytes.length;
        for (int index = 0; index < len; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);//byte 的每一位是否为1，从最高位开始
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc = crc << 1;
                if (c15 ^ bit)
                    crc = crc ^ polynomial;
            }
        }
        crc = crc & 0xffff;
        return Integer.toHexString(crc).toUpperCase();
    }

    public static void main(String[] args) throws Exception {
        String str = "515300010000001801000000000000000100010A00000000000000000000";
        String i = CRC_XModem(MyUtils.hexStringToBytes(str));
        System.out.println(i);
    }
}