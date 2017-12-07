package com.xyx.dp.model;

import com.xyx.x.model.TrackingIds;

public class TrackingIdsWithTimes extends TrackingIds {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5934431703621644818L;

	/**
	 * 活动最新更新时间
	 */
	private long campaignLastModified;
	
	/**
	 * 账户最新更新时间
	 */
	private long accountLastModified;

	/**
	 * @return the campaignLastModified
	 */
	public long getCampaignLastModified() {
		return campaignLastModified;
	}

	/**
	 * @param campaignLastModified the campaignLastModified to set
	 */
	public void setCampaignLastModified(long campaignLastModified) {
		this.campaignLastModified = campaignLastModified;
	}

	/**
	 * @return the accountLastModified
	 */
	public long getAccountLastModified() {
		return accountLastModified;
	}

	/**
	 * @param accountLastModified the accountLastModified to set
	 */
	public void setAccountLastModified(long accountLastModified) {
		this.accountLastModified = accountLastModified;
	}
	
}
