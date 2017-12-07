package xyxtech.rpt.data;

public class RptCreative extends RptBase{
	private long creativeId;

	public long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (creativeId ^ (creativeId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RptCreative other = (RptCreative) obj;
		if (creativeId != other.creativeId)
			return false;
		return true;
	}
	
}
