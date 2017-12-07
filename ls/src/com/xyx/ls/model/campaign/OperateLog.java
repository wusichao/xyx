package com.xyx.ls.model.campaign;

import java.io.Serializable;
import java.util.Date;

public class OperateLog implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	时间、IP、XID、操作类型、变更项、变更前内容、变更后内容
	private Long id;
	private Long campaignId;
	private Date operateTime;
	private String IP;
	private String userId;
	private String operateType;
	private String changeItem;
	private String changeBefore;
	private String changeAfter;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getChangeItem() {
		return changeItem;
	}
	public void setChangeItem(String changeItem) {
		this.changeItem = changeItem;
	}
	public String getChangeBefore() {
		return changeBefore;
	}
	public void setChangeBefore(String changeBefore) {
		this.changeBefore = changeBefore;
	}
	public String getChangeAfter() {
		return changeAfter;
	}
	public void setChangeAfter(String changeAfter) {
		this.changeAfter = changeAfter;
	}

}
