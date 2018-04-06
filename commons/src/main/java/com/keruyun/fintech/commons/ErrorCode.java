package com.keruyun.fintech.commons;

/**
 * Created by shuwei on 2016-01-25.
 */
public enum ErrorCode {
    OK(1000, "成功"),
    UNKNOW_ERROR(100, "未知错误[UNKNOWN ERROR]"),
    INTERNAL_ERROR(101, "服务器内部错误"),
    INTERNAL_EXCEPTION(102, "服务器内部异常"),
    INTERNAL_DB_EXCEPTION(103, "数据库异常"),
    UNSUPPORTED_OPERATION(104, "不支持该操作"),

    READ_JSON_ERROR(200, "请求的json格式错误"),
    VALIDATE_ERROR(201, "校验请求参数失败"),
    VALIDATE_SIGN_ERROR(202, "校验签名失败"),
    VALIDATE_TIMESTAMP_ERROR(203, "校验时间戳失败，请确认系统时间的准确性"),
    DATA_ERROR(204, "数据错误"),
    DATA_NOT_EXISTS(205, "数据不存在"),
    DATA_EXISTS(206, "数据已存在"),
    DATA_INVALID(207, "数据无效"),

    THIRD_NET_ERROR(211, "请求第三方接口超时或网络出错"),
    THIRD_API_ERROR(212, "请求第三方接口出错"),
    GET_REQUEST_PARAM_MAP_ERROR(212, "从request中获取请求参数map出错"),

    BUSINESS_ERROR(300, "业务异常"),
    FREQUENT_REQUESTS(301, "请求太频繁"),

    RESPONSE_OUTPUT_ERROR(401, "响应输出异常"),

    // 业务相关的异常码 begin
    TC_SYSTEM_ERROR(4001,"tc系统错误"),
    TC_PARAMETER_ERROR(4002,"tc参数错误"),
    TC_DATA_EX_ERROR(4003,"tc数据异常，未能获取结果"),
    TC_USER_NOT_EXISTS_OR_ERR_PROFILE(4004,"tc⽤户不存在或配置错误"),
    TC_TOKEN_ERROR(4005,"tcTokenKey验证失败"),
    TC_INTERFACE_UNDEFINED_ERROR(4006,"tc接口未定义或停⽌使⽤"),
    TC_USER_NOT_AUTHORIZED_ERROR(4007,"tc⽤户没有接口使⽤权限"),
    TC_INTERFACE_PAUSE_ERROR(4008,"tc⽤户接口暂停使⽤"),
    TC_ACCOUNT_NOT_SUFFICIENT_ERROR(4009,"tc⽤户余额不⾜"),
    TC_SYSTEM_BUSY_ERROR(4010, "tc系统繁忙，请稍候再试"),

    SITUDATA_PARAMETER_ERROR(4100,"参数错误"),
    SITUDATA_ACCOUNT_NOT_SUFFICIENT_ERROR(4101,"账户余额不⾜"),
    SITUDATA_TESTING_TIMES_INSUFFICIENT_ERROR(4102,"测试次数不足"),
    SITUDATA_FILE_SIZE_EXCEEDS_LIMIT_ERROR(4103,"文件编码后大小超限"),
    SITUDATA_PICTURE_FORMAT_ERROR(4104,"图片格式错误"),
    SITUDATA_QUERY_FAILURE_ERROR(4105,"查询失败"),
    SITUDATA_CARD_NOT_SUPPORT_ERROR(4106,"不支持的卡片类型"),
    SITUDATA_RECOGNITION_FAILURE_ERROR(4107,"识别失败"),



    YJ_SAME_ERROR(4201,"当前相同查询出错，请等2小时后重试"),
    YJ_INVALID_KEY_ERROR(4202,"当前的key无效或者还未生效中"),
    YJ_ACCOUNT_NOT_SUFFICIENT_ERROR(4203, "当前key已欠费"),
    YJ_KEY_PAUSE_ERROR(4204,"当前key被暂停使用"),
    YJ_INTERFACE_MAINTAIN(4205,"接口正在维护中"),
    YJ_INTERFACE_OFFLINE_ERROR(4206,"接口已下线停用"),
    YJ_FORBIDDEN_IP_OR_SIGN_ERROR(4207,"被禁止的ip或者签名错误"),
    YJ_REQUEST_FORMAT_ERROR(4208,"请求格式错误，请重试"),
    YJ_OVER_SYSTEM_UNLIMITED_ERROR(4209,"请求超过系统限制"),
    YJ_UNKNOWN_ERROR(4210,"系统未知错误，请联系技术客服"),
    YJ_DATA_NOT_FOUND_ERROR(4211,"数据没有找到"),
    YJ_RETURN_DATA_NOT_FOUND_ERROR(4299,"企业查询没有返回状态"),

    TOKEN_IS_OVERDUE_ERROR(6001, "accessToken已经过期，请用户在掌上客如云操作后重新获取"),
    TOKEN_IS_EMPTY_ERROR(6002, "accessToken不存在，请检查参数"),
    TOKEN_IS_ILLEGAL_ERROR(6003, "accessToken校验失败或已过期，请重新获取"),
    CHANNEL_STATUS_NOT_FOUND_ERROR(6004, "未找到渠道配置信息，请检查参数"),


    JSON_CONVERT_ERROR(7001, "转换json出错"),
    REQUEST_NOT_MATCH_ERROR(7002, "请求参数不匹配"),
    RESPONSE_DATA_NOT_FOUND_ERROR(7003, "返回Json数据不包含指定结构体[eg.results,data]"),
    JSON_DATA_CONVERT_NULL_ERROR(7004, "转换Json数据为空");


    // 业务相关的异常码 end

    private Integer code;
    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorCode getErrorByCode(int code) {
        ErrorCode[] codes = values();
        for (ErrorCode ec : codes) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return ErrorCode.UNKNOW_ERROR;
    }

    public static void main(String[] args) {
        for (ErrorCode ec : values()) {
            System.out.println(ec.getCode() + "|" + ec.getMessage());
        }
    }
}
