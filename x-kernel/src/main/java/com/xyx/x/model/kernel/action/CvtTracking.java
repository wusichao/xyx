package com.xyx.x.model.kernel.action;

public class CvtTracking extends IcTracking{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 91915812740019390L;

	/**
	 * 转化ID。可以为空，为空时表示不指定转化类型。
	 * 
	 */
	private Long cvtId;
	
	private Long clickTime;
	
	/**
	 * 其他参数
	 */
	private String other;
	
	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}


	public Long getCvtId() {
		return cvtId;
	}

	
	public void setCvtId(Long cvtId) {
		this.cvtId = cvtId;
	}
	
	public Long getClickTime() {
		return clickTime;
	}

	
	public void setClickTime(Long clickTime) {
		this.clickTime = clickTime;
	}

	
}
