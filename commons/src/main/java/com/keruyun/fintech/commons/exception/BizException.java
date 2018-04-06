package com.keruyun.fintech.commons.exception;

import com.keruyun.fintech.commons.ErrorCode;
import com.keruyun.fintech.commons.web.Response;

/**
 * Created by shuwei on 2016-01-25.
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = -2361229501302035235L;
    private Response<?> response;

    public BizException(Response<?> response) {
        super(response.getMessage());
        this.response = response;
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.response = new Response<>(errorCode);
    }

    public BizException(int errorCode, String message) {
        super(message);
        this.response = new Response<>(errorCode,message);
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.response = new Response<>(errorCode,message);
    }

    public BizException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.response = new Response<>(errorCode,message);
    }

    public Response<?> getResponse() {
        return response;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
