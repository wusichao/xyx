package xyxtech.rpt.data;

public class RptGeo extends RptBase{

	private int geoId;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + geoId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RptGeo other = (RptGeo) obj;
		if (geoId != other.geoId)
			return false;
		return true;
	}

	/**
	 * @return the geoId
	 */
	public int getGeoId() {
		return geoId;
	}

	/**
	 * @param geoId the geoId to set
	 */
	public void setGeoId(int geoId) {
		this.geoId = geoId;
	}
	
	
}
