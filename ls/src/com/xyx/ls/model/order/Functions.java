package com.xyx.ls.model.order;

import java.io.Serializable;
import java.sql.Date;

public class Functions implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	`id` BIGINT NOT NULL AUTO_INCREMENT,
//	  `name` VARCHAR(60) NOT NULL,
//	  `function` VARCHAR(500) NOT NULL,
//	  `creation` DATETIME NOT NULL,
//	  `last_modified` DATETIME NOT NULL,
//	  `removed` BIT NOT NULL DEFAULT b'0',
//	  PRIMARY KEY (`id`)
	private Long id;
	private String name;
	private Date creation;
	private Date lastModified;
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
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
}
