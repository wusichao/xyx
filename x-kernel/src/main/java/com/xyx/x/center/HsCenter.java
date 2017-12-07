package com.xyx.x.center;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.cache.TrackingCache;
import com.xyx.x.model.Account;
import com.xyx.x.model.Campaign;
import com.xyx.x.model.Identifiable;
import com.xyx.x.model.TrackingIds;

public class HsCenter implements CaCenter {
	
	private static final Logger logger = LoggerFactory.getLogger(HsCenter.class);

	private TrackingCache cache;
	
	private volatile TkMap tkMap;
	
	class TkMap{
		private volatile Map<Long, Campaign> caMap = new HashMap<>();
		private volatile Map<Long, Account> acMap = new HashMap<>();
		
		/**
		 * 对象加载器
		 */
		abstract class ObjLoader<T extends Identifiable<K>,K> {
			public T load(K id, Map<K, T> newmap, Map<K, T> oldmap) {
				T newobj = newmap.get(id);
				if (newobj == null) {
					T oldobj = null;
					if (oldmap != null) {
						oldobj = oldmap.get(id);
					}
					try {
						long time = getLastCached(id);
						if (oldobj == null || time > oldobj.getLastCached()) {
							T obj = getObjFromCache(id);
							if (obj != null) {
								newobj = obj;
								newobj.setLastCached(time);
								logger.info(getMsg(), id);
							}
						} else {
							newobj = oldobj;
						}
					} catch (Exception e) {
						logger.warn(getMsg().concat("失败"), id, e);
					}
					if (newobj != null) {
						newmap.put(id, newobj);
					}
				}
				return newobj;
			}

			public abstract long getLastCached(K id);

			public abstract T getObjFromCache(K id);

			public abstract String getMsg();
		}
		
		/**
		 * 活动加载器
		 */
		private ObjLoader<Campaign,Long> campaignLoader = new ObjLoader<Campaign,Long>() {
			public String getMsg() {
				return "载入变更活动[{}]";
			}

			public Campaign getObjFromCache(Long id) {
				return cache.getCampaign(id);
			}

			public long getLastCached(Long id) {
				return cache.getCampaignLastCached(id);
			}
		};
		/**
		 * 账户加载器
		 */
		private ObjLoader<Account, Long> accountLoader = new ObjLoader<Account, Long>() {

			@Override
			public long getLastCached(Long id) {
				return cache.getAccountLastCached(id);
			}

			@Override
			public Account getObjFromCache(Long id) {
				return cache.getAccount(id);
			}

			@Override
			public String getMsg() {
				return "载入变更账户[{}]";
			}
		};
		
		public List<TrackingIds> getIds(){
			return cache.getIds(0);
		}
		
		public TkMap(TkMap oldMap){
			
			logger.info("----更新缓存开始----");
			List<TrackingIds> ids = getIds();
			Map<Long, Campaign> tcaMap = new HashMap<>();
			Map<Long, Account> tacMap = new HashMap<>();
			
			for(TrackingIds id: ids){
				Campaign campaign = campaignLoader.load(id.getCampaignId(), tcaMap, oldMap == null ? null : oldMap.caMap);
				Account account = accountLoader.load(id.getAccountId(), tacMap, oldMap == null ? null : oldMap.acMap);
				
				if(campaign!=null &&account!=null){
					tcaMap.put(campaign.getId(), campaign);
					tacMap.put(account.getId(), account);
					campaign.setAccount(account);
				}
				
			}
			
			this.caMap = tcaMap;
			this.acMap = tacMap;
			
			logger.info("----更新缓存结束----");
		}
	}
	
	@Override
	public Campaign hitCampaign(Long CampaignId) {
		return tkMap.caMap.get(CampaignId);
	}
	
	public void update(){
		try{
			TkMap newMap = new TkMap(this.tkMap);
			this.tkMap = newMap;
		}catch(Exception e){
			logger.error("hsCenter.update()时出现错误:"+e.getLocalizedMessage());
		}
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(TrackingCache cache) {
		this.cache = cache;
	}

}
