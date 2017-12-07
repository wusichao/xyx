/**
 * 
 */
package com.xyx.x.model.kernel.action;

import com.xyx.x.model.Campaign;

/**
 * 曝光、点击、到达
 *
 */
public class IcTracking extends Action {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7629362338536545812L;

	/**
	 * 所属活动
	 */
	private Campaign campaign;
	
	/**
	 * 渠道ID
	 */
	private Long channelId;

	/**
	 * 媒体ID
	 */
	private Long mediaId;
	
	/**
	 * 创意ID
	 */
	private Long creativeId;

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	/**
	 * @return the channelId
	 */
	public Long getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the mediaId
	 */
	public Long getMediaId() {
		return mediaId;
	}

	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	/**
	 * @return the creativeId
	 */
	public Long getCreativeId() {
		return creativeId;
	}

	/**
	 * @param creativeId the creativeId to set
	 */
	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}

	
	
}
