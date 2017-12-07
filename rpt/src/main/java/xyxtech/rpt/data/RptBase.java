package xyxtech.rpt.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RptBase {

	private long campaignId;
	
	private Date day;

	private long imp;
	
	private long click;
	
	private long reach;
	
	private long conversion;
	
	private long visit;
	
	private long accountId;
	
	private long impIp;
	
	private long clickIp;
	
	private long reachIp;
	
	private long visitIp;
	
	private long conversionIp;
	
	private long impUser;
	
	private long clickUser;
	
	private long reachUser;
	
	private long visitUser;
	
	private long conversionUser;
	
	
	public long getImpIp() {
		return impIp;
	}

	public void setImpIp(long impIp) {
		this.impIp = impIp;
	}

	public long getClickIp() {
		return clickIp;
	}

	public void setClickIp(long clickIp) {
		this.clickIp = clickIp;
	}

	public long getReachIp() {
		return reachIp;
	}

	public void setReachIp(long reachIp) {
		this.reachIp = reachIp;
	}

	public long getVisitIp() {
		return visitIp;
	}

	public void setVisitIp(long visitIp) {
		this.visitIp = visitIp;
	}

	public long getConversionIp() {
		return conversionIp;
	}

	public void setConversionIp(long conversionIp) {
		this.conversionIp = conversionIp;
	}

	public long getImpUser() {
		return impUser;
	}

	public void setImpUser(long impUser) {
		this.impUser = impUser;
	}

	public long getClickUser() {
		return clickUser;
	}

	public void setClickUser(long clickUser) {
		this.clickUser = clickUser;
	}

	public long getReachUser() {
		return reachUser;
	}

	public void setReachUser(long reachUser) {
		this.reachUser = reachUser;
	}

	public long getVisitUser() {
		return visitUser;
	}

	public void setVisitUser(long visitUser) {
		this.visitUser = visitUser;
	}

	public long getConversionUser() {
		return conversionUser;
	}

	public void setConversionUser(long conversionUser) {
		this.conversionUser = conversionUser;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (int) (campaignId ^ (campaignId >>> 32));
//		result = prime * result + ((day == null) ? 0 : Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(day)));
//		return result;
//	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		RptBase other = (RptBase) obj;
//		if (campaignId != other.campaignId)
//			return false;
//		if (day == null) {
//			if (other.day != null)
//				return false;
//		} else if (Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(day))!=Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(other.day)))
//			return false;
//		
//		return true;
//	}

	/**
	 * @return the campaignId
	 */
	public long getCampaignId() {
		return campaignId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accountId ^ (accountId >>> 32));
		result = prime * result + (int) (campaignId ^ (campaignId >>> 32));
		result = prime * result + ((day == null) ? 0 : Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(day)));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RptBase other = (RptBase) obj;
		if (accountId != other.accountId)
			return false;
		if (campaignId != other.campaignId)
			return false;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(day))!=Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(other.day)))
			return false;
		return true;
	}

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * @return the day
	 */
	public Date getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(Date day) {
		this.day = day;
	}

	/**
	 * @return the imp
	 */
	public long getImp() {
		return imp;
	}

	/**
	 * @param imp the imp to set
	 */
	public void setImp(long imp) {
		this.imp = imp;
	}

	/**
	 * @return the click
	 */
	public long getClick() {
		return click;
	}

	/**
	 * @param click the click to set
	 */
	public void setClick(long click) {
		this.click = click;
	}

	/**
	 * @return the reach
	 */
	public long getReach() {
		return reach;
	}

	/**
	 * @param reach the reach to set
	 */
	public void setReach(long reach) {
		this.reach = reach;
	}

	/**
	 * @return the conversion
	 */
	public long getConversion() {
		return conversion;
	}

	/**
	 * @param conversion the conversion to set
	 */
	public void setConversion(long conversion) {
		this.conversion = conversion;
	}

	public long getVisit() {
		return visit;
	}

	public void setVisit(long visit) {
		this.visit = visit;
	}
	
}
