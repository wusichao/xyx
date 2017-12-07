package com.xyx.x.utils.ipanalyze;

import java.io.Serializable;

public class IpZone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1480074304440981767L;
	
	private boolean cityRecognized;
	private int cityId;
//	private int provinceId;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cityId;
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
		IpZone other = (IpZone) obj;
		if (cityId != other.cityId)
			return false;
		return true;
	}
	
	public boolean isCityRecognized() {
		return cityRecognized;
	}
	public void setCityRecognized(boolean cityRecognized) {
		this.cityRecognized = cityRecognized;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

}
