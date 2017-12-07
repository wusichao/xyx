package com.xyx.x.model;

public class User extends Identifiable<String> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5454644294525376462L;
	
	/**
	 * 新用户
	 */
	private boolean newUser;
	
	/**
	 * 是否不采集用户信息
	 */
	private boolean notCollect;

	/**
	 * @return the newUser
	 */
	public boolean isNewUser() {
		return newUser;
	}

	/**
	 * @param newUser the newUser to set
	 */
	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	/**
	 * @return the notCollect
	 */
	public boolean isNotCollect() {
		return notCollect;
	}

	/**
	 * @param notCollect the notCollect to set
	 */
	public void setNotCollect(boolean notCollect) {
		this.notCollect = notCollect;
	}
	
	
	
}
