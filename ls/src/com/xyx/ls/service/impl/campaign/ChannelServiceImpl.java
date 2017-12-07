package com.xyx.ls.service.impl.campaign;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyx.ls.dao.campaign.ChannelMapper;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.service.campaign.ChannelService;
@Service
@Transactional
public class ChannelServiceImpl implements ChannelService{
	@Resource
	private ChannelMapper channelMapper;
	
	public ChannelMapper getChannelMapper() {
		return channelMapper;
	}

	public void setChannelMapper(ChannelMapper channelMapper) {
		this.channelMapper = channelMapper;
	}

	@Override
	public void add(Channel channel,Long accountId) {
			channel.setCreation(new Date());
			channel.setLast_modified(new Date());
		 channelMapper.add(channel);
		channelMapper.addChannelAccount(accountId,channel.getId());
	}

	@Override
	public void delete(String ids,Long accountId) {
		channelMapper.deleteCampaignCannel(ids);
		channelMapper.deleteAccountChannel(ids,accountId);
		channelMapper.delete(ids);	
	}

	@Override
	public void update(Channel channel) {
		channel.setLast_modified(new Date());
		channelMapper.update(channel);
		
	}

	@Override
	public Channel findByIds(String ids) {
		return channelMapper.findByIds(ids);
	}

	@Override
	public List<Channel> findAll(String name) {
		return channelMapper.findAll(name);
	}

	@Override
	public List<Channel> findByCanpaignId(Long id) {
//		List<Channel> channelList = channelMapper.findByCanpaignId(id);
		//没有数据的
		List<Channel> channelList = channelMapper.ChannelData(id);
		for(Channel channel:channelList){
			channel.setIsData(0);
	}
		//有数据的
		List<Channel> channelLists=channelMapper.isChannelData(id);
		for(Channel channel:channelLists){
				channel.setIsData(1);
		}
		channelLists.addAll(channelList);
		return  channelLists;
	}

	@Override
	public List<Channel> findAllByAccount(String name, Long id) {
		return channelMapper.findAllByAccount(name,id);
	}

	@Override
	public boolean findCampaignByChannel(String ids) {
		if(	channelMapper.findCampaignByChannel(ids)!=0){
			return false;
		}
		return true;
	}
	
}
