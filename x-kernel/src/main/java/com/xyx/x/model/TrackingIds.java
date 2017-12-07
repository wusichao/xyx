package com.xyx.x.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TrackingIds extends Extensible {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -587361471621782845L;

	/**
	 * 活动ID
	 */
	private long campaignId;
	
	/**
	 * 账户ID
	 */
	private long accountId;
	
	/**
	 * 创意ID,不做序列化/反序列化处理
	 */
	private long creativeId;
	
	/**
	 * 渠道ID,不做序列化/反序列化处理
	 */
	private long channelId;
	
	/**
	 * 媒体ID,不做序列化/反序列化处理
	 */
	private long mediaId;
	
	private long clickTime;
	
	/**
	 * 转化ID,不做序列化/反序列化处理
	 */
	private long cvtId;
	
	/**
	 * 前一流程ID,不做序列化/反序列化处理
	 */
	private String actionPreId;
	
	private String other;
	
	private String type;
	
	private String referer;
	
	private String url;
	
	private String cheId;
	
	
	public String getCheId() {
		return cheId;
	}

	public void setCheId(String cheId) {
		this.cheId = cheId;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * 序列化
	 * @param tkIds
	 * @return
	 */
	public static String serial(Collection<TrackingIds> tkIds) {
		if (tkIds == null) {
			return "";
		}
		StringBuilder buf = new StringBuilder();
		for (TrackingIds tk : tkIds) {
			buf.append(tk.getCampaignId()).append('|');
			buf.append(tk.getAccountId()).append(',');
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	public static List<TrackingIds> deserial(String str) {
		List<TrackingIds> ret = new ArrayList<TrackingIds>();
		if (str != null) {
			String[] strs = StringUtils.split(str, ',');
			for (String s : strs) {
				String[] ss = StringUtils.split(s, '|');
				TrackingIds tkid = new TrackingIds();
				tkid.setCampaignId(Long.parseLong(ss[0].trim()));
				tkid.setAccountId(Long.parseLong(ss[1].trim()));
				ret.add(tkid);
			}
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return new Long(this.getCampaignId()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		TrackingIds dst = (TrackingIds) obj;
		return this.campaignId == dst.campaignId;
	}


	/**
	 * @return the campaignId
	 */
	public long getCampaignId() {
		return campaignId;
	}

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the creativeId
	 */
	public long getCreativeId() {
		return creativeId;
	}

	/**
	 * @param creativeId the creativeId to set
	 */
	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}

	/**
	 * @return the channelId
	 */
	public long getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the mediaId
	 */
	public long getMediaId() {
		return mediaId;
	}

	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}

	/**
	 * @return the cvtId
	 */
	public long getCvtId() {
		return cvtId;
	}

	/**
	 * @param cvtId the cvtId to set
	 */
	public void setCvtId(long cvtId) {
		this.cvtId = cvtId;
	}
	
	public long getClickTime() {
		return clickTime;
	}
	
	public void setClickTime(long clickTime) {
		this.clickTime = clickTime;
	}

	/**
	 * @return the actionPreId
	 */
	public String getActionPreId() {
		return actionPreId;
	}

	/**
	 * @param actionPreId the actionPreId to set
	 */
	public void setActionPreId(String actionPreId) {
		this.actionPreId = actionPreId;
	}

}
