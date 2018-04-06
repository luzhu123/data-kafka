package com.keruyun.fintech.commons.web;

import com.keruyun.fintech.commons.ErrorCode;
import com.keruyun.fintech.commons.MDCUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by shuwei on 2016-01-25.
 */
@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 9102477554828905959L;
    public static final String INTERNAL_SERVER_ERROR = "internal_server_error";
    protected Integer status;
    protected String message;
    protected Timestamp timestamp;//服务器时间戳
    protected String msgId;//服务器请求编号
    protected Integer pageSize=0;
    protected Integer totalRecords=0;
    protected Integer pageIndex=0;
    protected T content;

    public Response() {
        this(ErrorCode.OK);
    }

    public Response(ErrorCode errorCode) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.setErrorCode(errorCode);
    }

    public Response(ErrorCode errorCode, String message) {
        this(errorCode);
        this.message = message;
    }

    public Response(int errorCode, String message) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = errorCode;
        this.message = message;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.status = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.msgId = MDCUtils.getMsgId();
    }

    public void setErrorMessage(ErrorCode errorCode, String msg) {
        this.status = errorCode.getCode();
        if (StringUtils.isNotBlank(msg)) {
            this.setMessage(msg);
        } else {
            this.setMessage(errorCode.getMessage());
        }
    }

    public static <T> Response<T> of(T content) {
        Response<T> resp = new Response<>();
        resp.setContent(content);
        return resp;
    }

}
