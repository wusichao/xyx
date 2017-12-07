package com.xyx.x.model.kernel.action;

import com.xyx.x.model.Agent;
import com.xyx.x.model.Extensible;
import com.xyx.x.model.Geo;
import com.xyx.x.model.User;
import com.xyx.x.model.enu.ActionType;

public class Action extends Extensible {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1450656457682726417L;

	/**
	 * 类型
	 */
	private ActionType actionType;
	
	/**
	 * 账户ID
	 */
	private Long accountId;
	
	/**
	 * 唯一id
	 */
	private String actionId;
	
	/**
	 * 前一流程id
	 */
	private String actionPreId;
	
	/**
	 * SessionId
	 */
	private String sessionId;
	
	/**
	 * 请求URI
	 */
	private String actionUri;
	
	/**
	 * 请求时间
	 */
	private long requestTime;
	
	/**
	 * 响应时间
	 */
	private long responseTime;
	
	/**
	 * 用户
	 */
	private User user;
	
	/**
	 * 客户端
	 */
	private Agent agent;
	
	/**
	 * 地域
	 */
	private Geo geo;
	
	/**
	 * 活动未开始，或者已经过期
	 */
	private boolean error;
	
	private int domainType;

	
	/**
	 * the actionId to get
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId;
	}

	/**
	 * the actionId to set
	 * @param actionId the actionId to set
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	/**
	 * the actionPreId to get
	 * @return the actionPreId
	 */
	public String getActionPreId() {
		return actionPreId;
	}

	/**
	 * the actionPreId to set
	 * @param actionPreId the actionPreId to set
	 */
	public void setActionPreId(String actionPreId) {
		this.actionPreId = actionPreId;
	}

	/**
	 * the user to get
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * the user to set
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}


	/**
	 * the agent to get
	 * @return the agent
	 */
	public Agent getAgent() {
		return agent;
	}

	/**
	 * the agent to set
	 * @param agent the agent to set
	 */
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the requestTime
	 */
	public long getRequestTime() {
		return requestTime;
	}

	/**
	 * @param requestTime the requestTime to set
	 */
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * @return the responseTime
	 */
	public long getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the actionUri
	 */
	public String getActionUri() {
		return actionUri;
	}

	/**
	 * @param actionUri the actionUri to set
	 */
	public void setActionUri(String actionUri) {
		this.actionUri = actionUri;
	}

	/**
	 * @return the actionType
	 */
	public ActionType getActionType() {
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	public int getDomainType() {
		return domainType;
	}
	
	public void setDomainType(int t) {
		this.domainType = t;
	}

}
