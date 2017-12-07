/**
 * 
 */
package com.xyx.x.cache;

public interface SimpleCache {

	
	public String get(String key);
	
	public void set(String key,String value);
	
	public void set(String key,int expireSeconds,String value);
	
	
}
