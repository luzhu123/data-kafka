package com.keruyun.fintech.commons.exception;

import lombok.Data;

@Data
public class CommonException extends RuntimeException{
    private int ErrCode;
    private String ErrMessage;
    public CommonException(int ErrCode, String ErrMessage) {
        super(ErrMessage);
        this.ErrCode=ErrCode;
        this.ErrMessage=ErrMessage;
    }


}
