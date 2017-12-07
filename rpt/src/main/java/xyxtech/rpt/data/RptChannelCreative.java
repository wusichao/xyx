package xyxtech.rpt.data;

public class RptChannelCreative extends RptBase{

	private long channelId;
	
	private long creativeId;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (channelId ^ (channelId >>> 32));
		result = prime * result + (int) (creativeId ^ (creativeId >>> 32));
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
		RptChannelCreative other = (RptChannelCreative) obj;
		if (channelId != other.channelId)
			return false;
		if (creativeId != other.creativeId)
			return false;
		return true;
	}

	/**
	 * @return the channelId
	 */
	public long getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the creativeId
	 */
	public long getCreativeId() {
		return creativeId;
	}

	/**
	 * @param creativeId the creativeId to set
	 */
	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}
	
	
}
