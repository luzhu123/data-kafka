package com.keruyun.fintech.commons.enums;

/**
 * @author wanglin
 * @createTime 下午4:03:33
 * @description 资金方向
 */
public enum FundDirection {
	RECEIPT(1, "收入"), EXPENDITURE(-1, "支出");
	
	private Integer type;
	private String desc;
	
	FundDirection(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static FundDirection getByType(Integer type) {
		if(type != null) {
			for (FundDirection v : FundDirection.values()) {
	            if (v.getType().intValue() == type.intValue()) {
	                return v;
	            }
	        }
		}
        
        return null;
    }

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
