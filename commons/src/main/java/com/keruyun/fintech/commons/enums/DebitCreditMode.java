package com.keruyun.fintech.commons.enums;

/**
 * @author wanglin
 * @createTime 上午11:18:30
 * @description 借贷模型
 */
public enum DebitCreditMode {
	CONSTANT(1, "固定值"), MEMBERID(2, "用户编号"), BANKNO(3, "银行行号");
	
	private int code;
	private String desc;
	
	public static DebitCreditMode getByCode(int code) {
        for (DebitCreditMode v : DebitCreditMode.values()) {
            if (v.getCode() == code) {
                return v;
            }
        }
        return null;
    }
	
	DebitCreditMode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
