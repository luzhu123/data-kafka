package com.keruyun.fintech.commons.enums;

/**
 * 交易单状态
 * Created by 王林 on 2017/8/29.
 */
public enum  TranscationStatus {
    SUBMIT(0, "提交"),
    ALLOW(1, "许可成功"),
    CMBC_ACCEPT(2, "民生已受理"),
    SUCCESS(3, "成功"),
    FAIL(4, "失败"),
    EXCEPTION(5, "异常");

    int status;
    String desc;

    TranscationStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}
