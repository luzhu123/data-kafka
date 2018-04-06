package com.keruyun.fintech.commons.enums;

/**
 * @author wanglin
 * @createTime 下午2:28:26
 * @description 冻结类型，这个和交易类型关联起来的。
 *
 */
public enum FreezeType {
	WITHDRAW(1, "提现"), TRANSFER(2, "转账"), PAYMENT(3, "付款");
	
	private int code;
	private String desc;;
	
	FreezeType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static FreezeType getByCode(int code) {
        for (FreezeType v : FreezeType.values()) {
            if (v.getCode() == code) {
                return v;
            }
        }
        return null;
    }

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
