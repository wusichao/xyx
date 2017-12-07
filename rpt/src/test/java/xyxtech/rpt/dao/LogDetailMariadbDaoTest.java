package xyxtech.rpt.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.xyx.x.model.enu.ActionType;

import xyxtech.rpt.data.LogField;

@ContextConfiguration(locations = {"classpath*:content/datasource-test.xml"})
public class LogDetailMariadbDaoTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private DataSource dataSource;
	
	@Test
	public void test() {
		
		LogDetailMariadbDao dao = new LogDetailMariadbDao();
		dao.setDataSource(dataSource);
		
		LogField log = new LogField();
		log.setActionId("actionId");
		log.setActionType(ActionType.CLICK);
		log.setCampaignId(1);
		log.setAccountId(1);
		log.setCreativeId(1);
		log.setChannelId(1);
		log.setMediaId(1);
		log.setGeoId(1);
		log.setRequestTime(new java.util.Date(System.currentTimeMillis()));
		
		List<LogField> list = new ArrayList<>();
		list.add(log);
		
		dao.saveClickLogDetail(list);
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	
}
