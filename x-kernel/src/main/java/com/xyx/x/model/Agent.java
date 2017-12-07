package com.xyx.x.model;

public class Agent extends Extensible {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8999202938298640181L;
	
	private String ua;
	
	private String url;
	
	private String referer;
	
	/**
	 * @return the ua
	 */
	public String getUa() {
		return ua;
	}

	/**
	 * @param ua the ua to set
	 */
	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

}
