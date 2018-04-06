package com.keruyun.fintech.commons.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wanglin
 * @createTime 下午1:44:36
 * @description 交易类型
 */
public enum TradeType {
	GENERAL_LEDGER_INCREMENT("100001", "总账收入"),
	GENERAL_LEDGER_REDUCE("100002", "总账支出"),
	ADJUSTMENT_INCREMENT("110001", "无原单调账(增)"),
	ADJUSTMENT_REDUCE("110002", "无原单调账(减)"),
	TRANSFER("200000", "转账"), 
	RECHARGE("300000", "充值"),
	RECHARGE_ONLINE("300001", "线上充值"),
	WITHDRAW_CASH("400000", "提现"),
	PAYMENT("500000", "付款"), 
	Withdraw_refund("402101", "提现退款"),
	RECHARGE_OFFLINE("800000", "线下充值"),
	PLATFORM_INTERSETWITHDRAW("900003", "平台利息提现");
	
	private String code; //交易类型代码
	private String name; 
	
	public static TradeType getByCode(String code) {
		if (StringUtils.isBlank(code)) {
			return null;
		}
        for (TradeType v : TradeType.values()) {
            if (v.getCode().equalsIgnoreCase(code)) {
                return v;
            }
        }
        return null;
    }
	
    public static TradeType getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (TradeType v : TradeType.values()) {
            if (v.name().equalsIgnoreCase(name)) {
                return v;
            }
        }
        return null;
    }
	
	TradeType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
