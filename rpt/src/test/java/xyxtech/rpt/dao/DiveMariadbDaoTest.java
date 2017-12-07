package xyxtech.rpt.dao;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import xyxtech.rpt.data.LogField;

@ContextConfiguration(locations = {"classpath*:content/datasource-test.xml"})
public class DiveMariadbDaoTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private DataSource dataSource;
	
	@Test
	public void test() {
		DiveMariadbDao dao = new DiveMariadbDao();
		dao.setDataSource(dataSource);
		
		LogField log1 = new LogField();
		log1.setUserId("ikNvdzPvrz1");
		log1.setRequestTime(new java.util.Date());
		
		List<LogField> logs = new ArrayList<>();
		logs.add(log1);
		dao.findClickLog(logs);
		if(logs.size() > 0){
			LogField log = logs.get(0);
			System.out.println(log.getActionId());
			System.out.println(log.getUserId());
			System.out.println(log.getRequestTime());
		}
	}

}
