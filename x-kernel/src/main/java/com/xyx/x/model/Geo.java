package com.xyx.x.model;

public class Geo extends Identifiable<Long> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5665008911194550013L;

	/**
	 * ipv4
	 */
	private String ip;
	
	/**
	 * ipv6
	 */
	private String ipv6;
	
	/**
	 * parentId
	 */
	private long parentId;
	
	/**
	 * 偏移分钟
	 */
	private int offsetMinutes = 480;

	/**
	 * @return the ipv4
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ipv4 the ipv4 to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the ipv6
	 */
	public String getIpv6() {
		return ipv6;
	}

	/**
	 * @param ipv6 the ipv6 to set
	 */
	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the offsetMinutes
	 */
	public int getOffsetMinutes() {
		return offsetMinutes;
	}

	/**
	 * @param offsetMinutes the offsetMinutes to set
	 */
	public void setOffsetMinutes(int offsetMinutes) {
		this.offsetMinutes = offsetMinutes;
	}
	
}
