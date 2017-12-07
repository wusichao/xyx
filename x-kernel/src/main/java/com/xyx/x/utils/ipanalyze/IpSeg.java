package com.xyx.x.utils.ipanalyze;

import java.io.Serializable;

public class IpSeg implements Serializable {
	private static final long serialVersionUID = 5854190680823417016L;
	private long segStart;
	private long segEnd;
	private int idCity;
	public long getSegStart() {
		return segStart;
	}
	public void setSegStart(long segStart) {
		this.segStart = segStart;
	}
	public long getSegEnd() {
		return segEnd;
	}
	public void setSegEnd(long segEnd) {
		this.segEnd = segEnd;
	}
	public int getIdCity() {
		return idCity;
	}
	public void setIdCity(int idCity) {
		this.idCity = idCity;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (segStart ^ (segStart >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IpSeg other = (IpSeg) obj;
		if (segStart != other.segStart)
			return false;
		return true;
	}
	
}
