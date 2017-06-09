package com.fengtian.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient extends Socket {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 5000;

    // private static final String SERVER_IP = "192.168.2.106";
    // private static final int SERVER_PORT = 9018;
    private Socket client;
    private PrintWriter out;
    private OutputStream os;
    private InputStream is;

    /**
     * 与服务器连接，并输入发送消息
     */
    public SocketClient() throws Exception {
        super(SERVER_IP, SERVER_PORT);
        client = this;
        out = new PrintWriter(client.getOutputStream(), true);
        os = this.getOutputStream();
        is = this.getInputStream();
    }

    /**
     * 发送字符串
     *
     * @param msg
     * @throws IOException
     */
    public void outputString(String msg) {
        out.println(msg);
        out.flush();
        out.close();
    }

    /**
     * 接收字符串
     *
     * @return
     */
    public String inputString() {
        String s = null;
        try {
            long start = System.currentTimeMillis();
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                char[] ch = new char[is.available()];
                br.read(ch);
                s = new String(ch);
                if (s != null && !"".equals(s)) {
                    break;
                }
                long end = System.currentTimeMillis();
                if (end - start > 10000) {
                    System.out.println("跳出循环");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 发送byte数组
     *
     * @param b
     */
    public void outputByte(byte[] b) {
        try {
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(b);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收byte数据
     *
     * @return
     */
    public String inputByte() {
        try {
            long start = System.currentTimeMillis();
            while (true) {
                byte[] b = new byte[is.available()];
                char[] c = new char[is.available()];
                is.read(b);
                if (b != null && b.length > 0) {
                    for (int i = 0; i < b.length; i++) {
                        if (b[i] < 0) {
                            c[i] = (char) (b[i] + 256);
                        } else {
                            c[i] = (char) b[i];
                        }
                    }
                    return MyUtils.charArrayToHexString(c);
                }
                long end = System.currentTimeMillis();
                if (end - start > 10000) {
                    System.out.println("跳出循环");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        try {
            SocketClient c = new SocketClient();
            while (true) {
                Scanner s = new Scanner(System.in);
                String str = s.nextLine();
                if (str != null && !"".equals(str)) {
                    c.outputByte(MyUtils.hexStringToBytes(str));
                    String in = c.inputByte();
                    System.out.println(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
