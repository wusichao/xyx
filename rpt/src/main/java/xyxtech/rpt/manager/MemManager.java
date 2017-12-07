package xyxtech.rpt.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.xyx.x.model.enu.ActionType;

import redis.clients.jedis.JedisCluster;
import xyxtech.rpt.dao.DiveDao;
import xyxtech.rpt.dao.LogDetailDao;
import xyxtech.rpt.dao.RptDao;
import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptBase;
import xyxtech.rpt.data.RptChannel;
import xyxtech.rpt.data.RptChannelCreative;
import xyxtech.rpt.data.RptCreative;
import xyxtech.rpt.data.RptGeo;
import xyxtech.rpt.data.RptHour;
import xyxtech.rpt.data.collector.reporter.Reporter;

public class MemManager implements Manager{
	
	private Reporter<RptChannelCreative> ccReporter;
	
	private Reporter<RptBase> acReporter;
	
	private Reporter<RptChannel> accReporter;
	
	private Reporter<RptCreative> aceReporter;
	
	private Reporter<RptBase> aReporter;
	
	private Reporter<RptHour> hourReporter;
	
	private Reporter<RptGeo> geoReporter;
	
	private List<LogField> clickDetail = new ArrayList<>();
	
	private List<LogField> cvtDetail = new ArrayList<>();
	
	private List<LogField> reachDetail = new ArrayList<>();
	
	private RptDao rptDao;
	
	private LogDetailDao logDetailDao;
	
	private DiveDao diveDao;
	
	private Map<String,Integer> dataHZ  = new HashMap<>();
	public void collect(LogField log){
		
		if(log.getActionType()==null)
			return;
		else if(log.getActionType() == ActionType.CONVERSION)
			cvtDetail.add(log);
		else if(log.getActionType() == ActionType.CLICK)
			clickDetail.add(log);
		else if(log.getActionType() == ActionType.REACH||log.getActionType() == ActionType.VISIT)
			reachDetail.add(log);
		ccReporter.process(log);
		hourReporter.process(log);
		geoReporter.process(log);
		acReporter.process(log);
		accReporter.process(log);
		aceReporter.process(log);
		aReporter.process(log);
	}

	@Override
	public void flush() {
		
		//写入DB
		rptDao.saveRptCcs(ccReporter.report());
		rptDao.saveRptHour(hourReporter.report());
		rptDao.saveRptGeo(geoReporter.report());
		rptDao.saveRptAc(acReporter.report());
		rptDao.saveRptAcc(accReporter.report());
		rptDao.saveRptAce(aceReporter.report());
		rptDao.saveRptA(aReporter.report());
		//写入成功则清空
		ccReporter.clear();
		hourReporter.clear();
		geoReporter.clear();
		acReporter.clear();
		accReporter.clear();
		aceReporter.clear();
		aReporter.clear();
		
		logDetailDao.saveCvtLogDetail(cvtDetail);
		cvtDetail.clear();
		
		logDetailDao.saveClickLogDetail(clickDetail);
		clickDetail.clear();
		
		logDetailDao.saveReachLogDetail(reachDetail);
		reachDetail.clear();
		
	}
	
	@Override
	public boolean needFlush() {
		return clickDetail.size()>600 || cvtDetail.size()>600 || ccReporter.needFlush() || hourReporter.needFlush() ||geoReporter.needFlush();
	}

	/**
	 * @param ccReporter the ccReporter to set
	 */
	public void setCcReporter(Reporter<RptChannelCreative> ccReporter) {
		this.ccReporter = ccReporter;
	}

	/**
	 * @param hourReporter the hourReporter to set
	 */
	public void setHourReporter(Reporter<RptHour> hourReporter) {
		this.hourReporter = hourReporter;
	}

	/**
	 * @param geoReporter the geoReporter to set
	 */
	public void setGeoReporter(Reporter<RptGeo> geoReporter) {
		this.geoReporter = geoReporter;
	}

	/**
	 * @param clickDetail the clickDetail to set
	 */
	public void setClickDetail(List<LogField> clickDetail) {
		this.clickDetail = clickDetail;
	}

	/**
	 * @param rptDao the rptDao to set
	 */
	public void setRptDao(RptDao rptDao) {
		this.rptDao = rptDao;
	}

	/**
	 * @param logDetailDao the logDetailDao to set
	 */
	public void setLogDetailDao(LogDetailDao logDetailDao) {
		this.logDetailDao = logDetailDao;
	}

	/**
	 * @param diveDao the diveDao to set
	 */
	public void setDiveDao(DiveDao diveDao) {
		this.diveDao = diveDao;
	}
	public void setAcReporter(Reporter<RptBase> acReporter) {
		this.acReporter = acReporter;
	}
	public void setAccReporter(Reporter<RptChannel> accReporter) {
		this.accReporter = accReporter;
	}
	public void setAceReporter(Reporter<RptCreative> aceReporter) {
		this.aceReporter = aceReporter;
	}
	public void setaReporter(Reporter<RptBase> aReporter) {
		this.aReporter = aReporter;
	}

	@Override
	public void collectHZ(TreeSet<String> set,JedisCluster jedis) {
		for(String str:set){
			String key =jedis.get(str);
			Integer value = dataHZ.get(key);
			if(value==null){
				dataHZ.put(key, 1);
			}else{
				dataHZ.put(key, value++);
			}
			
		}
		
	}

	@Override
	public void flushHZ(String ache,int i) {
		String[] str = ache.split("_");
		String a = str[0].substring(str[0].lastIndexOf(":")+1);
		String c = str[1];
		String h=null;
		String e=null;
		if(i==2||i==3){
			h=str[2];
		}
		if(i==4||i==5){
			e=str[2];
		}
		if(i==6||i==7){
			h=str[2];
			e=str[3];
		}
		
	}

	@Override
	public void clear() {
		dataHZ.clear();
	}
}
