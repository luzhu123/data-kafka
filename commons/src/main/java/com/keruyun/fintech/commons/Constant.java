package com.keruyun.fintech.commons;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/6/5 11:31
 */
public class Constant {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final Charset DEFAULT_CHARSET_OBJ = Charset.forName(DEFAULT_CHARSET);
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    public static final String SYMBOL_UNION = "&";
    public static final String SYMBOL_EQUAL = "=";
    public static final String SYMBOL_SEMICOLON = ";";

    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    public static final Integer INTEGER_ONE = Integer.valueOf(1);
    public static final Long LONG_ZERO = Long.valueOf(0);
    public static final Long LONG_ONE = Long.valueOf(1);
    public static final BigDecimal BigDecimal_HUNDRED = BigDecimal.valueOf(100);

    public static final String KEY_SIGN = "sign";//签名字段
    public static final String KEY_APPKEY = "appKey";// 渠道方的appKey
    public static final String KEY_APPSECRET = "appSecret";// 渠道方的appSecret
    public static final String KEY_ACCESS_TOKEN = "accessToken";//客如云发放的访问token
    public static final String KEY_TIMESTAMP = "timestamp";//时间戳字段

    public static final String PACKAGE_JAVA_LANG = "java.lang";
    public static final String PACKAGE_COM_KERUYUN = "com.keruyun";

    public static final String HTTP_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String HTTP_CONTENT_TYPE_JSON = "application/json";

    public static final String KEY_ERROR_CODE = "errorCode";


    public static final int OK = 0;
    public static final int LEGALPERSON = 4;
    public static final int COMMERCE = 3;
}
