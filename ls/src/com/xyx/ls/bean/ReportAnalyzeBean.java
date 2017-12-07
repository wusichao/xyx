package com.xyx.ls.bean;

import java.io.Serializable;
import java.util.Date;

import com.xyx.x.utils.ServletUtils;

public class ReportAnalyzeBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date conversionTime;
	private String city;
	private String ip;
	private String userId;
	private String conversionName;
	private Date clickTime;
	private String gap;
	private String channelName;
	private String url;
	private String campaignName;
	private String creativeName;
	private Long geo;
	private String actionPreId;
	private String moneyp;
	private String phonep;
	private String emailp;
	private String dizhip;
	private String companyNamep;
	private String webSitep;
	private String peopleNamep;
	private String notep;
	private String sexp;
	private String agep;
	private String cityp;
	private String other;
	private Long clickNum;
	private Long useripNum;
	private String nowUrl;
	private String stopTime;
	private long visitNum;
	private Long cvtNum;
	private Date visitTime;
	
	public long getVisitNum() {
		return visitNum;
	}
	public void setVisitNum(long visitNum) {
		this.visitNum = visitNum;
	}
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	public String getNowUrl() {
		return nowUrl;
	}
	public void setNowUrl(String nowUrl) {
		this.nowUrl = nowUrl;
	}
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
	public Long getCvtNum() {
		return cvtNum;
	}
	public void setCvtNum(Long cvtNum) {
		this.cvtNum = cvtNum;
	}
	public Long getClickNum() {
		return clickNum;
	}
	public void setClickNum(Long clickNum) {
		this.clickNum = clickNum;
	}
	
	public Long getUseripNum() {
		return useripNum;
	}
	public void setUseripNum(Long useripNum) {
		this.useripNum = useripNum;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getEmailp() {
		return emailp;
	}
	public void setEmailp(String emailp) {
		this.emailp = emailp;
	}
	public String getDizhip() {
		return dizhip;
	}
	public void setDizhip(String dizhip) {
		this.dizhip = dizhip;
	}
	public String getCompanyNamep() {
		return companyNamep;
	}
	public void setCompanyNamep(String companyNamep) {
		this.companyNamep = companyNamep;
	}
	public String getWebSitep() {
		return webSitep;
	}
	public void setWebSitep(String webSitep) {
		this.webSitep = webSitep;
	}
	public String getPeopleNamep() {
		return peopleNamep;
	}
	public void setPeopleNamep(String peopleNamep) {
		this.peopleNamep = peopleNamep;
	}
	public String getNotep() {
		return notep;
	}
	public void setNotep(String notep) {
		this.notep = notep;
	}
	public String getSexp() {
		return sexp;
	}
	public void setSexp(String sexp) {
		this.sexp = sexp;
	}
	public String getCityp() {
		return cityp;
	}
	public void setCityp(String cityp) {
		this.cityp = cityp;
	}
	public String getGap() {
		return gap;
	}
	public void setGap(String gap) {
		this.gap = gap;
	}
	public String getActionPreId() {
		return actionPreId;
	}
	public void setActionPreId(String actionPreId) {
		this.actionPreId = actionPreId;
	}
	public Long getGeo() {
		return geo;
	}
	public void setGeo(Long geo) {
		this.geo = geo;
	}
	public Date getConversionTime() {
		return conversionTime;
	}
	public void setConversionTime(Date conversionTime) {
		this.conversionTime = conversionTime;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getConversionName() {
		return conversionName;
	}
	public void setConversionName(String conversionName) {
		this.conversionName = conversionName;
	}
	public Date getClickTime() {
		return clickTime;
	}
	public void setClickTime(Date clickTime) {
		this.clickTime = clickTime;
	}
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getCreativeName() {
		return creativeName;
	}
	public void setCreativeName(String creativeName) {
		this.creativeName = creativeName;
	}
	public String getMoneyp() {
		return moneyp;
	}
	public void setMoneyp(String moneyp) {
		this.moneyp = moneyp;
	}
	public String getPhonep() {
		return phonep;
	}
	public void setPhonep(String phonep) {
		this.phonep = phonep;
	}
	public String getAgep() {
		return agep;
	}
	public void setAgep(String agep) {
		this.agep = agep;
	}
	
}
