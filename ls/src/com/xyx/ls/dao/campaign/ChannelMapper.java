package com.xyx.ls.dao.campaign;

import java.util.List;

import com.xyx.ls.model.campaign.Channel;

public interface ChannelMapper {
	void add(Channel channel);
	void delete(String ids);
	void update(Channel channel);
	Channel findByIds(String ids);
	List<Channel> findAll(String name);
	void addChannelAccount(Long accountId, Long id);
	void deleteAccountChannel(String ids, Long accountId);
	List<Channel> findByCanpaignId(Long id);
	List<Channel> findAllByAccount(String name, Long id);
	void deleteCampaignCannel(String ids);
	int findCampaignByChannel(String ids);
	List<Channel> isChannelData(Long id);
	List<Channel> ChannelData(Long id);
}
