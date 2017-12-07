package com.xyx.x.cache.redis;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.xyx.x.cache.TrackingCache;
import com.xyx.x.model.Account;
import com.xyx.x.model.Campaign;
import com.xyx.x.model.Identifiable;
import com.xyx.x.model.TrackingIds;
import com.xyx.x.utils.StringUtils;

import redis.clients.jedis.JedisCluster;

public class RedisTrackingCache implements TrackingCache{
	
	private JedisCluster jedis;

	private String campaignObjPrefix = "ca:obj:";
	
	private String campaignLastCachedTime = "ca:cht:";
	
	private String campaignLastModified = "ca:lmf";
	
	private String accountPrefix = "ac:obj:";
	
	private String accountLastCachedTime = "ac:cht:";
	
	private String accountLastModified = "ac:lmf";
	
	private String idsHashPrefix = "hash:";
	
	private void setObject(Identifiable<?> obj,String objPrefix,String chtPrefix, String lmfKey){
		if(obj == null || obj.getId() == null) return;
		String id = obj.getId().toString();
		jedis.set(objPrefix.concat(id), JSON.toJSONString(obj));
		jedis.set(chtPrefix.concat(id), String.valueOf(System.currentTimeMillis()));
		jedis.hset(lmfKey, id, String.valueOf(obj.getLastModified()));
	}
	
	private <T> T getObject(Long id,String objPrefix,Class<T> clz){
		if(id == null) return null;
		return JSON.parseObject(jedis.get(objPrefix.concat(id.toString())), clz);
	}
	
	private long getLastCacheTime(String id,String chtPrefix){
		return StringUtils.toLong(jedis.get(chtPrefix.concat(id)));
	}
	
	private long getLastModified(String id,String lamKey){
		return StringUtils.toLong(jedis.hget(lamKey, id));
	}
	
	@Override
	public void setCampaign(Campaign campaign) {
		setObject(campaign, campaignObjPrefix, campaignLastCachedTime, campaignLastModified);
	}

	@Override
	public Campaign getCampaign(Long id) {
		return getObject(id, campaignObjPrefix, Campaign.class);
	}

	@Override
	public long getCampaignLastCached(Long id) {
		return getLastCacheTime(id.toString(), campaignLastCachedTime);
	}
	
	@Override
	public long getCampaignLastModified(Long id) {
		return getLastModified(id.toString(), accountLastModified);
	}

	@Override
	public void setAccount(Account account) {
		setObject(account, accountPrefix, accountLastCachedTime, accountLastModified);
	}

	@Override
	public Account getAccount(Long id) {
		return getObject(id, accountPrefix, Account.class);
	}

	@Override
	public long getAccountLastCached(Long id) {
		return getLastCacheTime(id.toString(), accountLastCachedTime);
	}

	@Override
	public long getAccountLastModified(Long id) {
		return getLastModified(id.toString(), accountLastModified);
	}

	
	@Override
	public void setIds(int hash, List<TrackingIds> ids) {
		jedis.set(idsHashPrefix.concat(String.valueOf(hash)), TrackingIds.serial(ids));
	}

	@Override
	public List<TrackingIds> getIds(int hash) {
		return TrackingIds.deserial(jedis.get(idsHashPrefix.concat(String.valueOf(hash)))); 
	}
	
	/**
	 * @return the campaignObjPrefix
	 */
	public String getCampaignObjPrefix() {
		return campaignObjPrefix;
	}

	/**
	 * @param campaignObjPrefix the campaignObjPrefix to set
	 */
	public void setCampaignObjPrefix(String campaignObjPrefix) {
		this.campaignObjPrefix = campaignObjPrefix;
	}

	/**
	 * @return the campaignLastCachedTime
	 */
	public String getCampaignLastCachedTime() {
		return campaignLastCachedTime;
	}

	/**
	 * @param campaignLastCachedTime the campaignLastCachedTime to set
	 */
	public void setCampaignLastCachedTime(String campaignLastCachedTime) {
		this.campaignLastCachedTime = campaignLastCachedTime;
	}

	/**
	 * @return the campaignLastModified
	 */
	public String getCampaignLastModified() {
		return campaignLastModified;
	}

	/**
	 * @param campaignLastModified the campaignLastModified to set
	 */
	public void setCampaignLastModified(String campaignLastModified) {
		this.campaignLastModified = campaignLastModified;
	}

	/**
	 * @return the accountPrefix
	 */
	public String getAccountPrefix() {
		return accountPrefix;
	}

	/**
	 * @param accountPrefix the accountPrefix to set
	 */
	public void setAccountPrefix(String accountPrefix) {
		this.accountPrefix = accountPrefix;
	}

	/**
	 * @return the accountLastCachedTime
	 */
	public String getAccountLastCachedTime() {
		return accountLastCachedTime;
	}

	/**
	 * @param accountLastCachedTime the accountLastCachedTime to set
	 */
	public void setAccountLastCachedTime(String accountLastCachedTime) {
		this.accountLastCachedTime = accountLastCachedTime;
	}

	/**
	 * @return the accountLastModified
	 */
	public String getAccountLastModified() {
		return accountLastModified;
	}

	/**
	 * @param accountLastModified the accountLastModified to set
	 */
	public void setAccountLastModified(String accountLastModified) {
		this.accountLastModified = accountLastModified;
	}

	/**
	 * @param jedis the jedis to set
	 */
	public void setJedis(JedisCluster jedis) {
		this.jedis = jedis;
	}

	/**
	 * @return the idsHashPrefix
	 */
	public String getIdsHashPrefix() {
		return idsHashPrefix;
	}

	/**
	 * @param idsHashPrefix the idsHashPrefix to set
	 */
	public void setIdsHashPrefix(String idsHashPrefix) {
		this.idsHashPrefix = idsHashPrefix;
	}

}
