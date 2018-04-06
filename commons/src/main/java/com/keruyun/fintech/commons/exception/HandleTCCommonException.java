package com.keruyun.fintech.commons.exception;

import com.keruyun.fintech.commons.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class HandleTCCommonException {
    /**
     * 系统错误
     */
    public static final int TC_SYSTEM_ERROR = 1;
    /**
     * 参数错误
     */
    public static final int TC_PARAMETER_ERROR = 2;
    /**
     * 数据异常，未能获取结果
     */
    public static final int TC_DATA_EX_ERROR = 3;
    /**
     * ⽤户不存在或配置错误
     */
    public static final int TC_USER_NOT_EXISTS_OR_ERR_PROFILE = 4;
    /**
     * TokenKey验证失败
     */
    public static final int TC_TOKEN_ERROR = 5;
    /**
     * 接⼜未定义或停⽌使⽤
     */
    public static final int TC_INTERFACE_UNDEFINED_ERROR = 6;
    /**
     * ⽤户没有接口使⽤权限
     */
    public static final int TC_USER_NOT_AUTHORIZED_ERROR = 7;
    /**
     * ⽤户接口暂停使⽤
     */
    public static final int TC_INTERFACE_PAUSE_ERROR = 8;
    /**
     * ⽤户余额不⾜
     */
    public static final int TC_ACCOUNT_NOT_SUFFICIENT_ERROR = 9;
    /**
     * 系统繁忙，请稍候再试
     */
    public static final int TC_SYSTEM_BUSY_ERROR = 10;

//    YJ_SAME_ERROR(4201,"当前相同查询出错，请等2小时后重试"),
//    YJ_INVALID_KEY_ERROR(4202,"当前的key无效或者还未生效中"),
//    YJ_ACCOUNT_NOT_SUFFICIENT_ERROR(4203, "当前key已欠费"),
//    YJ_KEY_PAUSE_ERROR(4204,"当前key被暂停使用"),
//    YJ_INTERFACE_MAINTAIN(4205,"接口正在维护中"),
//    YJ_INTERFACE_OFFLINE_ERROR(4206,"接口已下线停用"),
//    YJ_FORBIDDEN_IP_OR_SIGN_ERROR(4207,"被禁止的ip或者签名错误"),
//    YJ_REQUEST_FORMAT_ERROR(4208,"请求格式错误，请重试"),
//    YJ_OVER_SYSTEM_UNLIMITED_ERROR(4209,"请求超过系统限制"),
//    YJ_UNKNOWN_ERROR(4210,"系统未知错误，请联系技术客服"),
    /**
     * 当前相同查询出错，请等2小时后重试
     */
    public static final int YJ_SAME_ERROR = 110;
    /**
     * 当前的key无效或者还未生效中
     */
    public static final int YJ_INVALID_KEY_ERROR = 101;
    /**
     * 当前key已欠费"
     */
    public static final int YJ_ACCOUNT_NOT_SUFFICIENT_ERROR = 102;
    /**
     * 当前key被暂停使用
     */
    public static final int YJ_KEY_PAUSE_ERROR = 103;
    /**
     * 接口正在维护中
     */
    public static final int YJ_INTERFACE_MAINTAIN = 104;
    /**
     * 接口已下线停用
     */
    public static final int YJ_INTERFACE_OFFLINE_ERROR = 105;
    /**
     * 被禁止的ip或者签名错误
     */
    public static final int YJ_FORBIDDEN_IP_OR_SIGN_ERROR = 107;
    /**
     * 请求格式错误，请重试
     */
    public static final int YJ_REQUEST_FORMAT_ERROR = 108;
    /**
     * 请求超过系统限制
     */
    public static final int YJ_OVER_SYSTEM_UNLIMITED_ERROR = 109;
    /**
     * 系统未知错误，请联系技术客服
     */
    public static final int YJ_UNKNOWN_ERROR = 199;
    /**
     * 系统未知错误，请联系技术客服
     */
    public static final int YJ_DATA_NOT_FOUND_ERROR = 201;

    public ErrorCode handleException(CommonException ex) {

        if (ex.getErrCode() == TC_SYSTEM_ERROR) {
            return ErrorCode.TC_SYSTEM_ERROR;
        } else if (ex.getErrCode() == TC_PARAMETER_ERROR) {
            return ErrorCode.TC_PARAMETER_ERROR;

        } else if (ex.getErrCode() == TC_DATA_EX_ERROR) {
            return ErrorCode.TC_DATA_EX_ERROR;

        } else if (ex.getErrCode() == TC_USER_NOT_EXISTS_OR_ERR_PROFILE) {
            return ErrorCode.TC_USER_NOT_EXISTS_OR_ERR_PROFILE;

        } else if (ex.getErrCode() == TC_TOKEN_ERROR) {
            return ErrorCode.TC_TOKEN_ERROR;

        } else if (ex.getErrCode() == TC_INTERFACE_UNDEFINED_ERROR) {
            return ErrorCode.TC_INTERFACE_UNDEFINED_ERROR;
        } else if (ex.getErrCode() == TC_USER_NOT_AUTHORIZED_ERROR) {
            return ErrorCode.TC_USER_NOT_AUTHORIZED_ERROR;

        } else if (ex.getErrCode() == TC_INTERFACE_PAUSE_ERROR) {
            return ErrorCode.TC_INTERFACE_PAUSE_ERROR;

        } else if (ex.getErrCode() == TC_ACCOUNT_NOT_SUFFICIENT_ERROR) {
            return ErrorCode.TC_ACCOUNT_NOT_SUFFICIENT_ERROR;
        } else if (ex.getErrCode() == TC_SYSTEM_BUSY_ERROR) {
            return ErrorCode.TC_SYSTEM_BUSY_ERROR;
        } else if (ex.getErrCode() == YJ_SAME_ERROR) {
            return ErrorCode.YJ_SAME_ERROR;
        } else if (ex.getErrCode() == YJ_INVALID_KEY_ERROR) {
            return ErrorCode.YJ_INVALID_KEY_ERROR;
        } else if (ex.getErrCode() == YJ_ACCOUNT_NOT_SUFFICIENT_ERROR) {
            return ErrorCode.YJ_ACCOUNT_NOT_SUFFICIENT_ERROR;
        } else if (ex.getErrCode() == YJ_KEY_PAUSE_ERROR) {
            return ErrorCode.YJ_KEY_PAUSE_ERROR;
        } else if (ex.getErrCode() == YJ_INTERFACE_MAINTAIN) {
            return ErrorCode.YJ_INTERFACE_MAINTAIN;
        } else if (ex.getErrCode() == YJ_INTERFACE_OFFLINE_ERROR) {
            return ErrorCode.YJ_INTERFACE_OFFLINE_ERROR;
        } else if (ex.getErrCode() == YJ_FORBIDDEN_IP_OR_SIGN_ERROR) {
            return ErrorCode.YJ_FORBIDDEN_IP_OR_SIGN_ERROR;
        } else if (ex.getErrCode() == YJ_REQUEST_FORMAT_ERROR) {
            return ErrorCode.YJ_REQUEST_FORMAT_ERROR;
        } else if (ex.getErrCode() == YJ_OVER_SYSTEM_UNLIMITED_ERROR) {
            return ErrorCode.YJ_OVER_SYSTEM_UNLIMITED_ERROR;
        } else if (ex.getErrCode() == YJ_UNKNOWN_ERROR) {
            return ErrorCode.YJ_UNKNOWN_ERROR;
        } else if (ex.getErrCode() == YJ_DATA_NOT_FOUND_ERROR) {
            return ErrorCode.YJ_DATA_NOT_FOUND_ERROR;
        }
        return null;
    }
}
