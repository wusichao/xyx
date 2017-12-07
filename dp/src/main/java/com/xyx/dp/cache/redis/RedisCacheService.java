package com.xyx.dp.cache.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.dp.cache.CacheService;
import com.xyx.dp.dao.TrackingDao;
import com.xyx.dp.model.TrackingIdsWithTimes;
import com.xyx.x.cache.TrackingCache;
import com.xyx.x.model.Account;
import com.xyx.x.model.Campaign;
import com.xyx.x.model.Identifiable;
import com.xyx.x.model.TrackingIds;

public class RedisCacheService implements CacheService{
	
	private static Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
	
	private TrackingCache trackingCache;
	
	private TrackingDao trackingDao;
	
	public void update() {
		List<TrackingIdsWithTimes> idsWithTimes = trackingDao.getTrackingIds();
		Map<Long, Long> caMap = new HashMap<>();
		Map<Long, Long> acMap = new HashMap<>();
		
		idsWithTimes.forEach( i -> {
			caMap.put(i.getCampaignId(), i.getCampaignLastModified());
			acMap.put(i.getAccountId(), i.getAccountLastModified());
		});
		
		accountUpdater.update(acMap);
		campaignUpdater.update(caMap);
		
		List<TrackingIds> ids = new ArrayList<>(idsWithTimes);
		
		String s = TrackingIds.serial(ids);
		for(int i=0;i<5;i++){
			try {
				trackingCache.setIds(0, ids);
				logger.info("缓存在投活动{}个:"+s,ids.size());
				break;
			} catch (Exception e) {
				logger.warn("第{}次写入{}失败",i,s);
			}
		}
		
	}
	
	abstract class ObjectUpdater<T extends Identifiable<Long>>{
		
		public void update(Map<Long, Long> map){
			Set<Long> updateIds = new HashSet<>();
			map.forEach( (id,dbTime) ->{
				T cacheObj=null;
				long cachedTime = 0;
				for(int i=0;i<5;i++){
					try {
						cacheObj = getCache(id);
						cachedTime = getPreUpdateTime(id);
						break;
					} catch (Exception e) {
						logger.warn("第{}次检查{}[{}]更新时间失败",i,getName(),id);
					}
				}
				if(cacheObj==null || cachedTime < dbTime * 1000){
					updateIds.add(id);
				}
			});
			List<T> dbList = getFromDb(updateIds.toArray(new Long[updateIds.size()]));
			logger.info("自数据库载入{}条{}",dbList.size(),getName());
			dbList.forEach( db ->{
				for(int i=0;i<5;i++){
					try {
						setCache(db);
						logger.info("{}[{}]写入缓存",getName(),db.getId());
						break;
					} catch (Exception e) {
						logger.warn("第{}次{}[{}]写入缓存失败",i,getName(),db.getId());
					}
				}
			});
		}
		
		public abstract String getName();
		
		public abstract void setCache(T obj);
		
		public abstract T getCache(Long id);
		
		public abstract long getPreUpdateTime(Long id);
		
		public abstract List<T> getFromDb(Long... ids);
	}
	
	private ObjectUpdater<Campaign> campaignUpdater  = new ObjectUpdater<Campaign>(){

		@Override
		public String getName() {
			return "活动";
		}

		@Override
		public void setCache(Campaign obj) {
			trackingCache.setCampaign(obj);
		}
		
		@Override
		public Campaign getCache(Long id) {
			return trackingCache.getCampaign(id);
		}

		@Override
		public long getPreUpdateTime(Long id) {
			return trackingCache.getCampaignLastCached(id);
		}

		@Override
		public List<Campaign> getFromDb(Long... ids) {
			return trackingDao.getCampaign(ids);
		}
	};
	
	private ObjectUpdater<Account> accountUpdater = new ObjectUpdater<Account>(){

		@Override
		public String getName() {
			return "账户";
		}

		@Override
		public void setCache(Account obj) {
			trackingCache.setAccount(obj);
			
		}
		
		@Override
		public Account getCache(Long id) {
			return trackingCache.getAccount(id);
		}

		@Override
		public long getPreUpdateTime(Long id) {
			return trackingCache.getAccountLastCached(id);
		}

		@Override
		public List<Account> getFromDb(Long... ids) {
			return trackingDao.getAccount(ids);
		}
		
	};
	

	/**
	 * @param trackingCache the trackingCache to set
	 */
	public void setTrackingCache(TrackingCache trackingCache) {
		this.trackingCache = trackingCache;
	}

	/**
	 * @param trackingDao the trackingDao to set
	 */
	public void setTrackingDao(TrackingDao trackingDao) {
		this.trackingDao = trackingDao;
	}

}
