package com.fengtian.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class MyUtils {

	/**
	 * 查询字符串在另一个字符串中出现的次数，返回下标List
	 * 
	 * @param src
	 *            源字符串
	 * @param s
	 *            要查找的字符串
	 * @return
	 * 
	 */
	public static List seachString(String src, String s) {
		List list = new ArrayList();
		for (int i = 0; i < src.length(); i++) {
			int j = src.indexOf(s, i);
			if (j == -1) {
				break;
			} else {
				i = j;
			}
			list.add(j);
		}
		// System.out.println(list.size());
		return list;
	}

	/**
	 * 将十六进制字符串0803EAAE01 累加求和返回十六进制字符串FA 主要用于效验和
	 * 
	 * @param str
	 *            十六进制字符串
	 * @return
	 */
	public static String HexSUM(String str) {
		byte[] b = hexStringToBytes(str);
		int i = 0;
		for (byte a : b) {
			i += a;
		}
		// 去除大于256的高位
		i = i & 0xFF;
		String s = Integer.toHexString(i).toUpperCase();
		if (s.length() < 2) {
			s = "0" + s;
		}
		// System.out.println(i + " " + s);
		return s;
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	 * 0xD9}
	 * 
	 * @param src
	 * @return
	 */
	public static byte[] hexStringToBytes(String src) {
		// System.out.println(src);
		src = src.trim();
		// 声明返回数组
		byte[] b = new byte[src.length() / 2];
		for (int i = 0; i < src.length() / 2; i++) {
			// 取byte中的高位字符串
			byte a = Byte.decode("0x" + src.charAt(i * 2)).byteValue();
			a = (byte) (a << 4);
			byte c = Byte.decode("0x" + src.charAt(i * 2 + 1)).byteValue();
			b[i] = (byte) (a ^ c);
		}
		return b;
	}

	/**
	 * 
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0)
			return null;
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			// 补零
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
	 * 
	 * @param sourceDate
	 * @param formatLength
	 * @return 重组后的数据
	 */
	public static String BeforeCompWithZore(long sourceDate, int formatLength) {
		/*
		 * 0 指前面补充零 formatLength 字符总长度为 formatLength d代表为正数。
		 */
		return StringUtils.leftPad(sourceDate + "", formatLength, '0');
	}

	public static String BeforeCompWithZore(String sourceDate, int formatLength) {
		return StringUtils.leftPad(sourceDate, formatLength, '0');
	}

	/**
	 * 将长整型高低位转换，截取长度
	 * 
	 * @param l
	 *            源数据
	 * @param j
	 *            取多长
	 * @return
	 */
	public static String longHightLowConvert(long l, int j) {
		String str = BeforeCompWithZore(l, j);
		return stringHightLowConvert(str);
	}

	public static String longHightLowConvert(String l, int j) {
		String str = BeforeCompWithZore(l, j);
		return stringHightLowConvert(str);
	}

	/**
	 * 字符串高低位转换,将字符串 A1B2C3转成 C3B2A1
	 * 
	 * @param s
	 * @return
	 */
	public static String stringHightLowConvert(String s) {
		String str = "";
		if (s == null)
			return null;
		if ("".equals(s.trim()))
			return "";
		if (s.length() % 2 == 0) {
			for (int i = s.length() / 2; i > 0; i--) {
				str += s.substring(i * 2 - 2, i * 2);
			}
		}
		return str;
	}

	/**
	 * char数组转十六进制字符串
	 * 
	 * @param ch
	 * @return
	 */
	public static String charArrayToHexString(char[] ch) {
		String str = "";
		for (char c : ch) {
			String s = Integer.toHexString((int) c);
			if (s.length() < 2)
				s = "0" + s;
			str += s;
		}
		return str.toUpperCase();
	}

	public static void main(String[] args) {
		String s = stringHightLowConvert("12345678");
		System.out.println(s);
	}
}
