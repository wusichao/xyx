package xyxtech.rpt.data.collector.reporter;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.xyx.x.model.enu.ActionType;

import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptChannelCreative;

public class ChannelCreativeReporterTest {

	@Test
	public void test() {
		ChannelCreativeReporter reporter = new ChannelCreativeReporter();
		LogField logField = new LogField();
		logField.setCampaignId(1L);
		logField.setChannelId(2L);
		logField.setCreativeId(3L);
		logField.setActionType(ActionType.IMPRESSION);
		
		LogField logField2 = new LogField();
		logField2.setCampaignId(1L);
		logField2.setChannelId(2L);
		logField2.setCreativeId(3L);
		logField2.setActionType(ActionType.IMPRESSION);
		
		reporter.process(logField);
		reporter.process(logField2);
		
		List<RptChannelCreative> list = reporter.report();
		
		assertEquals(1, list.size());
		assertEquals(2, list.get(0).getImp());
	}

}
