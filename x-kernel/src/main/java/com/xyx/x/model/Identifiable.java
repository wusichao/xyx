package com.xyx.x.model;

public abstract class Identifiable<T> extends Extensible {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7071574500224941796L;

	/**
	 * id
	 */
	private T id;
	
	
	public Identifiable() {
		super();
	}

	public Identifiable(T id) {
		super();
		this.id = id;
	}

	private boolean active;
	
	private boolean removed;
	
	private long lastModified;
	
	private long lastCached;


	/**
	 * @return the id
	 */
	public T getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(T id) {
		this.id = id;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the removed
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the lastCached
	 */
	public long getLastCached() {
		return lastCached;
	}

	/**
	 * @param lastCached the lastCached to set
	 */
	public void setLastCached(long lastCached) {
		this.lastCached = lastCached;
	}
	
	
}
