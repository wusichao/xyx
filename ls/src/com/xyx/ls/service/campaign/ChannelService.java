package com.xyx.ls.service.campaign;

import java.util.List;

import com.xyx.ls.base.BaseService;
import com.xyx.ls.model.campaign.Channel;

public interface ChannelService extends BaseService<Channel> {

	List<Channel> findByCanpaignId(Long id);

	List<Channel> findAllByAccount(String name, Long id);

	boolean findCampaignByChannel(String ids);

	
}
