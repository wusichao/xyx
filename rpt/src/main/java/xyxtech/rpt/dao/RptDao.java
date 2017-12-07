package xyxtech.rpt.dao;

import java.util.List;

import xyxtech.rpt.data.RptBase;
import xyxtech.rpt.data.RptChannel;
import xyxtech.rpt.data.RptChannelCreative;
import xyxtech.rpt.data.RptCreative;
import xyxtech.rpt.data.RptGeo;
import xyxtech.rpt.data.RptHour;
import xyxtech.rpt.data.RptMedia;

public interface RptDao {

	public void saveRptCcs(List<RptChannelCreative> list);
	
	public void saveRptHour(List<RptHour> list);
	
	public void saveRptMedia(List<RptMedia> list);
	
	public void saveRptGeo(List<RptGeo> list);

	public void saveRptAc(List<RptBase> report);

	public void saveRptAcc(List<RptChannel> report);

	public void saveRptAce(List<RptCreative> report);

	public void saveRptA(List<RptBase> report);
}
