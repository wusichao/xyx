package xyxtech.rpt.data.collector.reporter.merge;



import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xyx.x.model.enu.ActionType;
import com.xyx.x.utils.TimeUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptBase;

public class Merger {
	public static void mergeLog(RptBase rpt,LogField log){
		rpt.setAccountId(log.getAccountId());
		
		if(log.getActionType() == ActionType.CONVERSION && log.getCampaignId()!=0)
			rpt.setDay(log.getClickTime());
		else
			rpt.setDay(log.getRequestTime());
		
		switch (log.getActionType()) {
		case IMPRESSION:
			rpt.setImp(1);
			break;
		case CLICK:
			rpt.setClick(1);
			break;
		case REACH:
			rpt.setReach(1);
			rpt.setVisit(1);
			break;
		case CONVERSION:
			rpt.setConversion(1);
			break;
		case VISIT:
			rpt.setVisit(1);
			break;
		default:
			break;
		}
	};
	


	public static void mergRpt(RptBase src,RptBase dest){
		dest.setImp(dest.getImp() + src.getImp());
		dest.setClick(dest.getClick() + src.getClick());
		dest.setReach(dest.getReach() + src.getReach());
		dest.setConversion(dest.getConversion() + src.getConversion());
		dest.setVisit(dest.getVisit() + src.getVisit());
		dest.setImpIp(dest.getImpIp()+src.getImpIp());
		dest.setClickIp(dest.getClickIp()+src.getClickIp());
		dest.setReachIp(dest.getReachIp()+src.getReachIp());
		dest.setVisitIp(dest.getVisitIp()+src.getVisitIp());
		dest.setConversionIp(dest.getConversionIp()+src.getConversionIp());
		dest.setImpUser(dest.getImpUser()+src.getImpUser());
		dest.setClickUser(dest.getClickUser()+src.getClickUser());
		dest.setReachUser(dest.getReachUser()+src.getReachUser());
		dest.setVisitUser(dest.getVisitUser()+src.getVisitUser());
		dest.setConversionUser(dest.getConversionUser()+src.getConversionUser());
	}
}
