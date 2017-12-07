package com.xyx.x.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Extensible implements Serializable,Cloneable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7429652076967451156L;

	private Map<String, Object> extendData;
	
	public void setExtendItem(String key,Object item){
		if(extendData != null){
			extendData = new HashMap<>();
		}
		extendData.put(key, item);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Extensible clone = (Extensible)super.clone();
		Map<String,Object> exData = new HashMap<>();
		exData.putAll(this.extendData);
		clone.setExtendData(exData);
		return super.clone();
	}

	/**
	 * @return the extendData
	 */
	public Map<String, Object> getExtendData() {
		return extendData;
	}

	/**
	 * @param extendData the extendData to set
	 */
	public void setExtendData(Map<String, Object> extendData) {
		this.extendData = extendData;
	}

	
}
