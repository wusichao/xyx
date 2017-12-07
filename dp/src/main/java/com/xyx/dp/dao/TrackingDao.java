package com.xyx.dp.dao;

import java.util.List;

import com.xyx.dp.model.TrackingIdsWithTimes;
import com.xyx.x.model.Account;
import com.xyx.x.model.Campaign;

public interface TrackingDao {

	/**
	 * 获取活动
	 * @param ids id集合
	 * @return 活动列表
	 */
	public List<Campaign> getCampaign(Long... ids);
	
	/**
	 * 获取账户
	 * @param ids id集合
	 * @return 账户列表
	 */
	public List<Account> getAccount(Long... ids);
	
	/**
	 * 获取在投活动
	 * @return
	 */
	public List<TrackingIdsWithTimes> getTrackingIds();
}
