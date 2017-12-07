package com.xyx.x.center;

import com.xyx.x.model.Campaign;

public interface CaCenter {
	/**
	 * 命中活动
	 * @param CampaignId
	 * @return
	 */
	public Campaign hitCampaign(Long CampaignId);
}
