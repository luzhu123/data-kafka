package com.keruyun.fintech.commons.exception;

import com.keruyun.fintech.commons.ErrorCode;

/**
 * api接口异常
 * Created by shuwei on 2016-01-31.
 */
public class ApiException extends RuntimeException{
    private static final long serialVersionUID = -2361229501302035810L;
    private int code;
    private String message;

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.message = message;
        this.code = errorCode.getCode();
    }

    /**
     * @param msg
     * @param errorCode
     * @param cause
     */
    public ApiException(ErrorCode errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.message = msg;
        this.code = errorCode.getCode();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
