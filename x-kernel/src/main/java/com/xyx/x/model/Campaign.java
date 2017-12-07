package com.xyx.x.model;

import java.text.ParseException;
import java.util.Set;

import com.xyx.x.model.enu.CostType;
import com.xyx.x.utils.TimeUtils;

public class Campaign extends Identifiable<Long> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9125247410583707248L;

	/**
	 * 所属账户
	 */
	private Account Account;

	/**
	 * 账户ID
	 */
	private Long accountId;

	/**
	 * 活动名称
	 */
	private String name;

	/**
	 * 开始时间，格式yyyyMMdd
	 */
	private String beginDate;

	/**
	 * 开始日期索引
	 */
	private int beginDayIndex;

	/**
	 * 结束时间，格式yyyyMMdd
	 */
	private String endDate;

	/**
	 * 结束日期索引
	 */
	private int endDayIndex;

	/**
	 * 结算方式
	 */
	private CostType costType;

	/**
	 * 活动地址
	 */
	private String targetUrl;
	
	private Set<Long> creativeIdSet;
	
	private Set<Long> channelIdSet;
	
	private Set<Long> mediaIdSet;
	
	/**
	 * 转化点
	 */
	private Set<Long> convertionIdSet;

	/**
	 * Constructor
	 */
	public Campaign() {

	}

	/**
	 * 
	 * Constructor
	 *
	 * @param id
	 */
	public Campaign(Long id) {
		super(id);
	}

	/**
	 * the account to get
	 * 
	 * @return the account
	 */
	public Account getAccount() {
		return Account;
	}

	/**
	 * the account to set
	 * 
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		Account = account;
	}

	/**
	 * the name to get
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * the name to set
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * the beginDate to get
	 * 
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return beginDate;
	}

	/**
	 * the beginDate to set
	 * 
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
		if (beginDate == null) {
			beginDayIndex = -1;
		} else {
			try {
				beginDayIndex = TimeUtils.getDayIndex(beginDate);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * the endDate to get
	 * 
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * the endDate to set
	 * 
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
		if (endDate == null) {
			endDayIndex = -1;
		} else {
			try {
				endDayIndex = TimeUtils.getDayIndex(endDate);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * the costType to get
	 * 
	 * @return the costType
	 */
	public CostType getCostType() {
		return costType;
	}

	/**
	 * the costType to set
	 * 
	 * @param costType
	 *            the costType to set
	 */
	public void setCostType(CostType costType) {
		this.costType = costType;
	}

	/**
	 * the targetUrl to get
	 * 
	 * @return the targetUrl
	 */
	public String getTargetUrl() {
		return targetUrl;
	}

	/**
	 * the targetUrl to set
	 * 
	 * @param targetUrl
	 *            the targetUrl to set
	 */
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the beginDayIndex
	 */
	public int getBeginDayIndex() {
		return beginDayIndex;
	}

	/**
	 * @return the endDayIndex
	 */
	public int getEndDayIndex() {
		return endDayIndex;
	}

	/**
	 * @return the creativeIdSet
	 */
	public Set<Long> getCreativeIdSet() {
		return creativeIdSet;
	}

	/**
	 * @param creativeIdSet the creativeIdSet to set
	 */
	public void setCreativeIdSet(Set<Long> creativeIdSet) {
		this.creativeIdSet = creativeIdSet;
	}
	
	public Set<Long> getChannelIdSet() {
		return channelIdSet;
	}

	public void setChannelIdSet(Set<Long> channelIdSet) {
		this.channelIdSet = channelIdSet;
	}
	
	public Set<Long> getMediaIdSet() {
		return mediaIdSet;
	}

	public void setMediaIdSet(Set<Long> mediaIdSet) {
		this.mediaIdSet = mediaIdSet;
	}

	/**
	 * @return the convertionIdSet
	 */
	public Set<Long> getConvertionIdSet() {
		return convertionIdSet;
	}

	/**
	 * @param convertionIdSet the convertionIdSet to set
	 */
	public void setConvertionIdSet(Set<Long> convertionIdSet) {
		this.convertionIdSet = convertionIdSet;
	}

}
