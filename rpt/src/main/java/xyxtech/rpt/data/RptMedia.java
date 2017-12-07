package xyxtech.rpt.data;

public class RptMedia extends RptBase{

	private long mediaId;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (mediaId ^ (mediaId >>> 32));
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
		RptMedia other = (RptMedia) obj;
		if (mediaId != other.mediaId)
			return false;
		return true;
	}

	/**
	 * @return the mediaId
	 */
	public long getMediaId() {
		return mediaId;
	}

	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}
	
	
}