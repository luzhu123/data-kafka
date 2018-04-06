package com.keruyun.fintech.commons;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;

/**
 * 请求参数处理工具类
 */
public class RequestUtils {

    public static final String twinSymbol = "&";// 键值对之间的拼接符号

    public static boolean isRequestExpired(long timestamp, long expiredSeconds) {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - timestamp > expiredSeconds * 1000) {
            return true;
        }
        return false;
    }
    public static String queryString(SortedMap<String, Object> params, String key) {
        return queryString(params, key, false);
    }
    public static String queryString(SortedMap<String, Object> params, String key, boolean isEcoding) {
        return queryString(params, "key", key, isEcoding,twinSymbol);
    }
    //added by fw 2015‐6‐3 用于调用h5订单状态接口生成签名时 产生不带key=xx的查询参数
    public static String queryString(SortedMap<String, Object> params) {
        return queryString(params, null, "", false,twinSymbol);
    }
    public static String queryTwinString(SortedMap<String, Object> params, String twinSymbol) {
        return queryString(params, null, "", false,twinSymbol);
    }
    public static String queryString(SortedMap<String, Object> params, String keyName, String keyValue, boolean isEcoding,String twinSymbol) {
        StringBuilder templateParam = new StringBuilder();
        int i = 0;
        int size = params.size();
        String charset = "UTF‐8";
        Object obj;
        for (Map.Entry<String, Object> e : params.entrySet()) {
            i++;
            String name = e.getKey();
            obj = e.getValue();
            String value;
            if (null == obj) {
                value = "";
            } else {
                value = "" + obj;
            }
            if (isEcoding) {
                name = encode(name, charset);
                value = encode(value, charset);
            }
            templateParam.append(name).append("=").append(value);
            if (i < size || !StringUtils.isBlank(keyValue)) templateParam.append(twinSymbol);
        }
        if (StringUtils.isNotBlank(keyValue)) templateParam.append(keyName).append("=").append(keyValue);
        return templateParam.toString();
    }
    public static String getSign(String queryString) {
        return DigestUtils.md5Hex(queryString);
    }
    private static String encode(final String content, final String charset) {
        if (content == null) {
            return null;
        }
        try {
            return URLEncoder.encode(content, charset != null ? charset : HTTP.DEF_CONTENT_CHARSET.name());
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
