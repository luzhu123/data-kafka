package com.keruyun.fintech.commons.exception;

import com.keruyun.fintech.commons.ErrorCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/1/18 14:44
 */
public class ExceptionTool {
    private static final char JOIN_ERR_INFO_CHAR = ' ';//连接错误信息的字符
    private static final String EMPTY_STRING = "";//空字符串

    public static BizException createBizException(ErrorCode errorCode) {
        throw new BizException(errorCode);
    }

    public static BizException createBizException(int errorCode, String message) {
        throw new BizException(errorCode, message);
    }

    public static BizException createBizException(ErrorCode errorCode, String message) {
        throw new BizException(errorCode, message);
    }

    public static void assertTrue(boolean expire, ErrorCode errorCode) {
        if (!expire) {
            throw new BizException(errorCode);
        }
    }

    public static void assertTrue(boolean expire, ErrorCode errorCode, String error) {
        if (!expire) {
            throw new BizException(errorCode.getCode(), error);
        }
    }

    public static void assertNull(Object obj, ErrorCode errorCode) {
        if (null != obj) {
            throw new BizException(errorCode);
        }
    }

    public static void assertNull(Object obj, ErrorCode errorCode, String error) {
        if (null != obj) {
            throw new BizException(errorCode.getCode(), error);
        }
    }

    public static void assertNotNull(Object obj, ErrorCode errorCode) {
        if (null == obj) {
            throw new BizException(errorCode);
        }
    }

    public static void assertNotNull(Object obj, ErrorCode errorCode, String error) {
        if (null == obj) {
            throw new BizException(errorCode.getCode(), error);
        }
    }

    public static void assertNotBlank(String str, ErrorCode errorCode) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(errorCode);
        }
    }

    public static void assertNotBlank(String str, ErrorCode errorCode, String error) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(errorCode.getCode(), error);
        }
    }

    /**
     * 获取第一个非空错误信息
     * @param errors
     * @return
     */
    public static String getFirstError(String... errors) {
        if (null == errors || 0 == errors.length) {
            return EMPTY_STRING;
        }
        for (String s : errors) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            return s;
        }
        return EMPTY_STRING;
    }
    /**
     * 连接所有非空错误信息
     * @param errors
     * @return
     */
    public static String joinError(String... errors) {
        if (null == errors || 0 == errors.length) {
            return EMPTY_STRING;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : errors) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            sb.append(JOIN_ERR_INFO_CHAR).append(s);
        }
        if (sb.length() == 0) {
            return EMPTY_STRING;
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }
    /**
     * 连接前n个非空错误信息
     * @param n
     * @param errors
     * @return
     */
    public static String joinTopError(int n,String... errors) {
        if (null == errors || 0 == errors.length || n < 1) {
            return EMPTY_STRING;
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String s : errors) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            sb.append(JOIN_ERR_INFO_CHAR).append(s);
            if (++count >= n) {
                break;
            }
        }
        if (sb.length() == 0) {
            return EMPTY_STRING;
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }
}
