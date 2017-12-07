package com.xyx.x.cache;

import java.util.List;

import com.xyx.x.model.Account;
import com.xyx.x.model.Campaign;
import com.xyx.x.model.TrackingIds;

public interface TrackingCache {

	public void setCampaign(Campaign campaign);
	
	public Campaign getCampaign(Long id);
	
	public long getCampaignLastCached(Long id);
	
	public long getCampaignLastModified(Long id);
	
	public void setAccount(Account account);
	
	public Account getAccount(Long id);
	
	public long getAccountLastCached(Long id);
	
	public long getAccountLastModified(Long id);
	
	public void setIds(int hash,List<TrackingIds> ids);
	
	public List<TrackingIds> getIds(int hash);
}
