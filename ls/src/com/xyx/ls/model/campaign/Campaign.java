package com.xyx.ls.model.campaign;

import java.io.Serializable;
import java.util.Date;

import com.xyx.x.utils.ServletUtils;
//活动
public class Campaign implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long account_id;
	private String name;
	private Date begin_date;
	private Date end_date;
	private String cost_type;
	private String target_url;
	private Date creation;
	private boolean removed;
	private Date last_modified;
	private double cost;
	private String activity_cycle;//活动周期
	private String status;//状态
	private String operation;//操作
	private double unit_price;//单价
	private String operateLog;
	private Integer monitor_type;
	private String monitorType;
	
	
	public String getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
	public Integer getMonitor_type() {
		return monitor_type;
	}
	public void setMonitor_type(Integer monitor_type) {
		this.monitor_type = monitor_type;
	}
	public String getOperateLog() {
		return operateLog;
	}
	public void setOperateLog(String operateLog) {
		this.operateLog = operateLog;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAccount_id() {
		return account_id;
	}
	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}
	public String getCost_type() {
		return cost_type;
	}
	public void setCost_type(String cost_type) {
		this.cost_type = cost_type;
	}
	public Date getCreation() {
		return creation;
	}
	public void setCreation(Date creation) {
		this.creation = creation;
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	public Date getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getActivity_cycle() {
		return activity_cycle;
	}
	public void setActivity_cycle(String activity_cycle) {
		this.activity_cycle = activity_cycle;
	}
	public String getTarget_url() {
		return target_url;
	}
	public void setTarget_url(String target_url) {
		this.target_url = target_url;
	}
	
}
