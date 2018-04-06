package com.keruyun.fintech.commons.enums;

/**
 * 文件下载
 * @author wanglin
 * @createTime 下午5:07:11
 * @description 
 *
 */
public enum FileDownLoadStatus {
	UNLOAD(1, "未下载"),  		//未下载
	DOWNLOAD(2, "已下载"),      	//已下载
	DOWNLOAD_FAIL(3, "下载失败");	//下载失败
	
	
	private int code;
	private String desc;

	FileDownLoadStatus(int type, String desc) {
		this.code = type;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
