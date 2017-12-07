package com.xyx.ls.model.order;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	 `id` BIGINT NOT NULL AUTO_INCREMENT,
//	  `order_number` VARCHAR(25) NOT NULL,
//	  `account_id` BIGINT(20) NOT NULL,
//	  `product_id` BIGINT(20) NOT NULL,
//	  `begin_date` DATE NOT NULL,
//	  `end_date` DATE NOT NULL,
//	  `creation` DATETIME NOT NULL,
//	  `removed` BIT NOT NULL DEFAULT b'0',
//	  `last_modified` DATETIME NOT NULL,
//	  `order_type` VARCHAR(20) NOT NULL,
	  private Long id;
	  private String orderNumber;
	  private Long accountId;
	  private String companyName;
	  private String functionsName;
	  private String serviceLife;
	  private String orderType;
	  private Date creation;
	  private Date lastModified;
	  private Date begin_date;
	  private Date end_date;
	  private String email;
	  
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getFunctionsName() {
		return functionsName;
	}
	public void setFunctionsName(String functionsName) {
		this.functionsName = functionsName;
	}
	public String getServiceLife() {
		return serviceLife;
	}
	public void setServiceLife(String serviceLife) {
		this.serviceLife = serviceLife;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Date getCreation() {
		return creation;
	}
	public void setCreation(Date creation) {
		this.creation = creation;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	  
}
