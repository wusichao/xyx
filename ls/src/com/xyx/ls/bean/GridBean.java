package com.xyx.ls.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GridBean<E> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int page;
	private int records;
	private int total;
	private List<E>rows;
	private E userdata;
	
	public E getUserdata() {
		return userdata;
	}
	public void setUserdata(E userdata) {
		this.userdata = userdata;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<E> getRows() {
		return rows;
	}
	public void setRows(List<E> rows) {
		this.rows = rows;
	}
	
	
}
