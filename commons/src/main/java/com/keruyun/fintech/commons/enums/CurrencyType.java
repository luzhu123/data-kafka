package com.keruyun.fintech.commons.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wanglin
 * @createTime 下午3:34:56
 * @description 币种
 */
public enum CurrencyType {
	RMB("0156", 1, "人民币"), 
	OCTA("0250", 2, "马克"), 
	FRF("0256", 3, "法郎"), 
	HKD("0344", 4, "港元"), 
	JPY("0392", 5, "日元"), 
	GBP("0826", 6, "英镑"), 
	DXY("0840", 7, "美元"), 
	EUR("0954", 8, "欧元");

	private String code;
	private String name;
	private int sequence;//主要用于生成账户编号

	CurrencyType(String code, int sequence, String name) {
		this.code = code;
		this.sequence = sequence;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public int getSequence() {
		return sequence;
	}

	public static CurrencyType getByCode(String code) {
        for (CurrencyType v : CurrencyType.values()) {
            if (StringUtils.isNotBlank(code) && v.getCode().equalsIgnoreCase(code)) {
                return v;
            }
        }
        return null;
    }
    public static CurrencyType getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (CurrencyType v : CurrencyType.values()) {
            if (v.name().equalsIgnoreCase(name)) {
                return v;
            }
        }
        return null;
    }
}
