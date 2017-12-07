package com.xyx.ls.model.campaign;

import java.io.Serializable;
import java.util.Date;

//渠道
public class Channel implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	`id` bigint(20) NOT NULL AUTO_INCREMENT,
//	`name` varchar(50) NOT NULL,
//	`creation` datetime NOT NULL,
//	`removed` bit(1) NOT NULL DEFAULT b'0',
//	`last_modified` datetime NOT NULL,
	private Long id;
	private String name;
	private Date creation;
	private boolean removed;
	private Date last_modified;
	private int isData;
	
	
	public int getIsData() {
		return isData;
	}
	public void setIsData(int isData) {
		this.isData = isData;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
}
