package com.keruyun.fintech.commons.enums;

/**
 * @author wanglin
 * @createTime 上午11:08:27
 * @description 开会使用的证件类型
 */
public enum CredentialType {
	ID(1, "身份证");
	
	private int code;
	private String desc;
	
	CredentialType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static CredentialType getByCode(int code) {
        for (CredentialType v : CredentialType.values()) {
            if (v.getCode() == code) {
                return v;
            }
        }
        return null;
    }
}
