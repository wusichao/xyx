package xyxtech.rpt.data.collector.reporter;

import com.xyx.x.model.enu.ActionType;
import com.xyx.x.utils.TimeUtils;

import redis.clients.jedis.JedisCluster;
import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptHour;
import xyxtech.rpt.utils.DateUtils;

public class HourReporter extends BaseReporter<RptHour> {
	private JedisCluster jedis;
	@Override
	public RptHour generateRpt(LogField log) {
		RptHour rpt = new RptHour();
		rpt.setCampaignId(log.getCampaignId());
		if(log.getActionType()==ActionType.CONVERSION && log.getCampaignId()!=0)
			rpt.setHour(TimeUtils.getHourOfDay(log.getClickTime().getTime()));
		else
			rpt.setHour(TimeUtils.getHourOfDay(log.getRequestTime().getTime()));
		return rpt;
	}

	@Override
	public RptHour userAndIpRpt(RptHour rpt,LogField log) {
		switch (log.getActionType()) {
		case IMPRESSION:
			if(notSameIp(rpt,log)){
				rpt.setImpIp(1);
			}
			if(notSameUser(rpt,log)){
				rpt.setImpUser(1);
			}
			break;
		case CLICK:
			if(notSameIp(rpt,log)){
				rpt.setClickIp(1);
			}
			if(notSameUser(rpt,log)){
				rpt.setClickUser(1);
			}
			break;
		case REACH:
			if(notSameIp(rpt,log)){
				rpt.setReachIp(1);
			}
			if(notSameUser(rpt,log)){
				rpt.setReachUser(1);
			}
			if(notSameIpVisit(rpt,log)){
				rpt.setVisitIp(1);
			}
			if(notSameUserVisit(rpt,log)){
				rpt.setVisitUser(1);
			}
			break;
		case CONVERSION:
			if(notSameIp(rpt,log)){
				rpt.setConversionIp(1);
			}
			if(notSameUser(rpt,log)){
				rpt.setConversionUser(1);
			}
			break;
		case VISIT:
			if(notSameIp(rpt,log)){
				rpt.setVisitIp(1);
			}
			if(notSameUser(rpt,log)){
				rpt.setVisitUser(1);
			}
			break;
		default:
			break;
		}
		return rpt;
	}

	private boolean notSameIp(RptHour rpt, LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":ip:"+log.getActionType()+":aco:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+rpt.getHour()+"_"+log.getIp();
		String value = jedis.get(key);
		if(value==null){
			jedis.setex(key, DateUtils.expireTime, 1+"");
			return true;
		}else{
			jedis.incr(key);
			return false;
		}
	}

	private boolean notSameUser(RptHour rpt, LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":user:"+log.getActionType()+":aco:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+rpt.getHour()+"_"+log.getUserId();
		String value = jedis.get(key);
		if(value==null){
			jedis.setex(key, DateUtils.expireTime, 1+"");
			return true;
		}else{
			jedis.incr(key);
			return false;
		}
		
	}
	
	private boolean notSameIpVisit(RptHour rpt, LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":ip:"+"VISIT"+":aco:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+rpt.getHour()+"_"+log.getIp();
		String value = jedis.get(key);
		if(value==null){
			jedis.setex(key, DateUtils.expireTime, 1+"");
			return true;
		}else{
			jedis.incr(key);
			return false;
		}
	}

	private boolean notSameUserVisit(RptHour rpt, LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":user:"+"VISIT"+":aco:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+rpt.getHour()+"_"+log.getUserId();
		String value = jedis.get(key);
		if(value==null){
			jedis.setex(key, DateUtils.expireTime, 1+"");
			return true;
		}else{
			jedis.incr(key);
			return false;
		}
		
	}

	public JedisCluster getJedis() {
		return jedis;
	}

	public void setJedis(JedisCluster jedis) {
		this.jedis = jedis;
	}
	
}
