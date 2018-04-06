package com.keruyun.fintech.commons;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/6/8 11:27
 */
public class SHA1Utils {
    public static final String KEY_ALGORITHM = "SHA-1";

    public static void main(String[] args) {
        String tdc = SHA1Utils.tdc(0, "f96d40eeab92f375a58b0a40e1608b5f");
        System.out.println(tdc);
    }

    /**
     * 生成动态密码tdv
     * 1.	取当前UTC时间戳,下对齐到分钟,并转换为字符串ts_str
     * 2.	拼接ts_str与渠道密钥,作为待摘要字符串src_str
     * 3.	将src_str使用UTF-8编码转为字节数组,进行SHA-1摘要.
     * 4.	将摘要结果截取前8字节(64bit),用16位16进制大写字母表示.
     * 伪代码公式 ToHexString(Truncate(SHA-1(timestamp_min+key, UTF-8), 8))
     */
    public static String tdc(long timestamp, String key) {
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance(KEY_ALGORITHM);
            if (sha1 == null)
                return null;
        } catch (Exception e) {
            return null;
        }

        // amend timestamp
        if (timestamp == 0)
            timestamp = System.currentTimeMillis() / 1000;

        timestamp = (timestamp / 60 * 60);

        // build source string
        String src_str = String.format("%d%s", timestamp, key);

        // to sha1
        sha1.update(src_str.getBytes(Charset.forName("UTF-8")));
        byte[] full_dig = sha1.digest();

        // truncate to 8 bytes
        byte[] t_dig = new byte[8];
        System.arraycopy(full_dig, 0, t_dig, 0, 8);

        // get result
        return HexString(t_dig);
    }

    /**
     * 转大写十六进制摘要
     */
    private static String HexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        if (data == null || data.length <= 0) {
            return null;
        }
        for (int i = 0; i < data.length; ++i) {
            int unsigned_value = (int) data[i] & 0xFF;
            String CC = String.format("%02X", unsigned_value);
            sb.append(CC);
        }
        return sb.toString();
    }
}
