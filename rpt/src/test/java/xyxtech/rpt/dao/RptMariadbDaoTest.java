package xyxtech.rpt.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import xyxtech.rpt.data.RptChannelCreative;
import xyxtech.rpt.data.RptGeo;
import xyxtech.rpt.data.RptHour;
import xyxtech.rpt.data.RptMedia;

@ContextConfiguration(locations = {"classpath*:content/datasource-test.xml"})
public class RptMariadbDaoTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private DataSource dataSource;
	
	@Test
	public void test() {
		RptMariadbDao dao = new RptMariadbDao();
		dao.setDataSource(dataSource);
		
		
		//base
		RptChannelCreative rcc = new RptChannelCreative();
		rcc.setCampaignId(1);
		rcc.setChannelId(0);
		rcc.setDay(new java.sql.Date(System.currentTimeMillis()));
		rcc.setImp(100);
		rcc.setClick(1);
		rcc.setReach(1);
		rcc.setConversion(1);
		
		RptChannelCreative rcc2 = new RptChannelCreative();
		rcc2.setCampaignId(2);
		rcc2.setChannelId(0);
		rcc2.setDay(new java.sql.Date(System.currentTimeMillis()));
		rcc2.setImp(100);
		rcc2.setClick(1);
		rcc2.setReach(1);
		rcc2.setConversion(1);
		
		
		
		List<RptChannelCreative> list = new ArrayList<>();
		list.add(rcc);
		list.add(rcc2);
		dao.saveRptCcs(list);
		
		//hour
		RptHour rh = new RptHour();
		rh.setCampaignId(1);
		rh.setDay(new java.sql.Date(System.currentTimeMillis()));
		rh.setHour(13);
		rh.setImp(100);
		rh.setClick(1);
		rh.setReach(1);
		rh.setConversion(1);
		
		
		RptHour rh2 = new RptHour();
		rh2.setCampaignId(2);
		rh2.setDay(new java.sql.Date(System.currentTimeMillis()));
		rh2.setHour(13);
		rh2.setImp(100);
		rh2.setClick(1);
		rh2.setReach(1);
		rh2.setConversion(1);
		
		
		List<RptHour> hourList = new ArrayList<>();
		hourList.add(rh);
		hourList.add(rh2);
		dao.saveRptHour(hourList);
		
		//media
		RptMedia rm = new RptMedia();
		rm.setCampaignId(1);
		rm.setDay(new java.sql.Date(System.currentTimeMillis()));
		rm.setMediaId(1);
		rm.setImp(100);
		rm.setClick(1);
		rm.setReach(1);
		rm.setConversion(1);
		
		RptMedia rm2 = new RptMedia();
		rm2.setCampaignId(2);
		rm2.setDay(new java.sql.Date(System.currentTimeMillis()));
		rm2.setMediaId(1);
		rm2.setImp(100);
		rm2.setClick(1);
		rm2.setReach(1);
		rm2.setConversion(1);
		
		List<RptMedia> mediaList = new ArrayList<>();
		mediaList.add(rm);
		mediaList.add(rm2);
		
		dao.saveRptMedia(mediaList);
		
		//geo
		RptGeo rg = new RptGeo();
		rg.setCampaignId(1);
		rg.setGeoId(1);
		rg.setDay(new java.sql.Date(System.currentTimeMillis()));
		rg.setImp(100);
		rg.setClick(1);
		rg.setReach(1);
		rg.setConversion(1);
		
		RptGeo rg2 = new RptGeo();
		rg2.setCampaignId(2);
		rg2.setGeoId(1);
		rg2.setDay(new java.sql.Date(System.currentTimeMillis()));
		rg2.setImp(100);
		rg2.setClick(1);
		rg2.setReach(1);
		rg2.setConversion(1);
		
		List<RptGeo> geoList = new ArrayList<>();
		geoList.add(rg);
		geoList.add(rg2);
		dao.saveRptGeo(geoList);
		
		
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	

}
