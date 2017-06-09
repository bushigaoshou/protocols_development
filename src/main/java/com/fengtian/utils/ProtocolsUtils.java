package com.fengtian.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * 协议工具类
 * 
 * @author bsgs
 *
 */
public class ProtocolsUtils {

	private static final Logger log = Logger.getLogger(ProtocolsUtils.class);

	/**
	 * 较验协议
	 * 
	 * @param cmd
	 * @return
	 */
	public static boolean checking(String cmd) {
		try {
			if (cmd == null || "".equals(cmd))
				return false;

			// 字符串长度
			int length = cmd.length();
			// log.info("命令长度：" + length);

			if (length % 2 != 0 || length < 30)
				return false;

			if (!"68".equals(cmd.substring(0, 2)) || !"16".equals(cmd.substring(length - 2, length)))
				return false;

			int dLength = Integer.parseInt(cmd.substring(22, 26), 16);// 取出数据域长度
			// log.info("数据域长度：" + dLength);

			if (!(length == 26 + dLength * 2))// 校验数据长度
				return false;

			String checkSum = cmd.substring(length - 4, length - 2);// 取出最后两位较验和
			String checkSum1 = cmd.substring(26 + dLength * 2 - 4, 26 + dLength * 2 - 2);// 根据数据域取出校验和

			// log.info(checkSum1 + " " + checkSum);
			String sum = MyUtils.HexSUM(cmd.substring(0, length - 4));// 算出校验和
			if (!checkSum.equals(checkSum1) || !checkSum1.equals(sum))// 对比校验
				return false;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 处理粘包问题
	 * 
	 * @param cmd
	 * @return
	 */
	public static List<String> packageSplicing(String cmd) {
		if (cmd == null && "".equals(cmd))
			return null;
		// 字符串长度
		int sumLength = cmd.length();
		List<String> ls = new ArrayList<String>();
		// 处理设备粘包问题
		List<Integer> s = MyUtils.seachString(cmd, "68");
		// log.info("68下标：" + JSON.toJSONString(s));
		try {
			if (s == null && s.size() == 0)
				return null;
			for (int i : s) {

				if (sumLength < (i + 30))// 长度小于30下次循环
					continue;

				int dLength = Integer.parseInt(cmd.substring(i + 22, i + 26), 16);// 取出数据域长度
				// log.info("数据域长度：" + dLength);

				if (sumLength < i + 26 + dLength * 2) // 校验数据长度
					continue;

				if (checking(cmd.substring(i, i + 26 + dLength * 2))) {// 校验协议
					ls.add(cmd.substring(i, i + 26 + dLength * 2));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;
	}

	public static void main(String[] args) {
		String cmd = "";
		cmd = "6873010000000012345678000F00000000987654320100001234DA16";
		cmd += "687100000000000068686800021316";
		cmd += "686C00000000006834687800025216";
		// cmd = "686C00000000006834687800025216";
		log.info(JSON.toJSONString(packageSplicing(cmd)));
		// log.info(checking(cmd));

	}
}
