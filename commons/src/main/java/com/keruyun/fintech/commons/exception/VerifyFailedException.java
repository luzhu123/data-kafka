package com.keruyun.fintech.commons.exception;

import com.keruyun.fintech.commons.ErrorCode;

public class VerifyFailedException extends RuntimeException {
    
    private transient ErrorCode errorCode;

    public VerifyFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
