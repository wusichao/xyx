package xyxtech.rpt.data.collector.reporter;

import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptMedia;

public class MediaReporter extends BaseReporter<RptMedia> {

	@Override
	public RptMedia generateRpt(LogField log) {
		RptMedia rpt = new RptMedia();
		rpt.setCampaignId(log.getCampaignId());
		rpt.setMediaId(log.getMediaId());
		return rpt;
	}

	@Override
	public RptMedia userAndIpRpt(RptMedia rpt,LogField log) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
