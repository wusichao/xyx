package com.xyx.x.utils.ipanalyze;

import java.io.Serializable;

public class IpSegLowBit implements Serializable {
	private static final long serialVersionUID = -7417353100865685218L;
	private int segStart;
	private int segEnd;
	private int bnet;
	private int idCity;
	
	public int getSegStart() {
		return segStart;
	}
	public void setSegStart(int segStart) {
		this.segStart = segStart;
	}
	public int getSegEnd() {
		return segEnd;
	}
	public void setSegEnd(int segEnd) {
		this.segEnd = segEnd;
	}
	public int getIdCity() {
		return idCity;
	}
	public void setIdCity(int idCity) {
		this.idCity = idCity;
	}
	public int getBnet() {
		return bnet;
	}
	public void setBnet(int bnet) {
		this.bnet = bnet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + segStart;
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
		IpSegLowBit other = (IpSegLowBit) obj;
		if (segStart != other.segStart)
			return false;
		return true;
	}
	
}
