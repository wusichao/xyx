package com.xyx.x.model;

import java.util.Set;

public class Account extends Identifiable<Long>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2068652268262144767L;

	/**
	 * 媒体Id集合
	 */
	private Set<Long> mediaIdSet;
	
	/**
	 * 渠道Id集合
	 */
	private Set<Long> channelIdSet;
	
	/**
	 * 转化点集合
	 */
	private Set<Long> convertionIdSet;
	
	private int domainType;
	
	public Set<Long> getMediaIdSet() {
		return mediaIdSet;
	}

	public void setMediaIdSet(Set<Long> mediaIdSet) {
		this.mediaIdSet = mediaIdSet;
	}

	public Set<Long> getChannelIdSet() {
		return channelIdSet;
	}

	public void setChannelIdSet(Set<Long> channelIdSet) {
		this.channelIdSet = channelIdSet;
	}
	
	public Set<Long> getConvertionIdSet() {
		return convertionIdSet;
	}

	public void setConvertionIdSet(Set<Long> convertionIdSet) {
		this.convertionIdSet = convertionIdSet;
	}
	
	public void setDomainType(int t){
		this.domainType=t;
	}
	
	public int getDomainType(){
		return domainType;
	}

}