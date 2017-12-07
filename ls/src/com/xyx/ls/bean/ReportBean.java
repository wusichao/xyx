package com.xyx.ls.bean;

import java.io.Serializable;

public class ReportBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long imp;
	private Long click;
	private Long reach;
	private Long convertion;
	private double cost;
	private String clickRate;
	private String reachRate;
	private String convertionRate;
	private Double cpm;
	private Double cpc;
	private Double cpl;
	private Double cpa;
	private String Dimension;
	private String day;
	private String province;
	private String city;
	private Long channel_id;
	private Long creative_id;
	private int hour;
	private Long geo_id;
	private Long visit;
	private Double cpd;
	private long impIp;
	private long impUser;
	private long clickIp;
	private long clickUser;
	private long reachIp;
	private long reachUser;
	private long visitIp;
	private long visitUser;
	private long conversionIp;
	private long conversionUser;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getImpIp() {
		return impIp;
	}
	public void setImpIp(long impIp) {
		this.impIp = impIp;
	}
	public long getImpUser() {
		return impUser;
	}
	public void setImpUser(long impUser) {
		this.impUser = impUser;
	}
	public long getClickIp() {
		return clickIp;
	}
	public void setClickIp(long clickIp) {
		this.clickIp = clickIp;
	}
	public long getClickUser() {
		return clickUser;
	}
	public void setClickUser(long clickUser) {
		this.clickUser = clickUser;
	}
	public long getReachIp() {
		return reachIp;
	}
	public void setReachIp(long reachIp) {
		this.reachIp = reachIp;
	}
	public long getReachUser() {
		return reachUser;
	}
	public void setReachUser(long reachUser) {
		this.reachUser = reachUser;
	}
	public long getVisitIp() {
		return visitIp;
	}
	public void setVisitIp(long visitIp) {
		this.visitIp = visitIp;
	}
	public long getVisitUser() {
		return visitUser;
	}
	public void setVisitUser(long visitUser) {
		this.visitUser = visitUser;
	}
	public long getConversionIp() {
		return conversionIp;
	}
	public void setConversionIp(long conversionIp) {
		this.conversionIp = conversionIp;
	}
	public long getConversionUser() {
		return conversionUser;
	}
	public void setConversionUser(long conversionUser) {
		this.conversionUser = conversionUser;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Long getVisit() {
		return visit;
	}
	public void setVisit(Long visit) {
		this.visit = visit;
	}
	public Double getCpd() {
		return cpd;
	}
	public void setCpd(Double cpd) {
		this.cpd = cpd;
	}
	public Long getGeo_id() {
		return geo_id;
	}
	public void setGeo_id(Long geo_id) {
		this.geo_id = geo_id;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public Long getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(Long channel_id) {
		this.channel_id = channel_id;
	}
	public Long getCreative_id() {
		return creative_id;
	}
	public void setCreative_id(Long creative_id) {
		this.creative_id = creative_id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDimension() {
		return Dimension;
	}
	public void setDimension(String dimension) {
		Dimension = dimension;
	}
	public Double getCpm() {
		return cpm;
	}
	public void setCpm(Double cpm) {
		this.cpm = cpm;
	}
	public Double getCpc() {
		return cpc;
	}
	public void setCpc(Double cpc) {
		this.cpc = cpc;
	}
	public Double getCpl() {
		return cpl;
	}
	public void setCpl(Double cpl) {
		this.cpl = cpl;
	}
	public Double getCpa() {
		return cpa;
	}
	public void setCpa(Double cpa) {
		this.cpa = cpa;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	
	
	public String getClickRate() {
		return clickRate;
	}
	public void setClickRate(String clickRate) {
		this.clickRate = clickRate;
	}
	public String getReachRate() {
		return reachRate;
	}
	public void setReachRate(String reachRate) {
		this.reachRate = reachRate;
	}
	public String getConvertionRate() {
		return convertionRate;
	}
	public void setConvertionRate(String convertionRate) {
		this.convertionRate = convertionRate;
	}
	public Long getImp() {
		return imp;
	}
	public void setImp(Long imp) {
		this.imp = imp;
	}
	public Long getClick() {
		return click;
	}
	public void setClick(Long click) {
		this.click = click;
	}
	public Long getReach() {
		return reach;
	}
	public void setReach(Long reach) {
		this.reach = reach;
	}
	public Long getConvertion() {
		return convertion;
	}
	public void setConvertion(Long convertion) {
		this.convertion = convertion;
	}
	
}
