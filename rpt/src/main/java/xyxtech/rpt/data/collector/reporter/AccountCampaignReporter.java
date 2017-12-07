package xyxtech.rpt.data.collector.reporter;

import com.xyx.x.utils.TimeUtils;

import redis.clients.jedis.JedisCluster;
import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptBase;
import xyxtech.rpt.utils.DateUtils;

public class AccountCampaignReporter extends BaseReporter<RptBase>{
	private JedisCluster jedis;
	@Override
	public RptBase generateRpt(LogField log) {
		RptBase rpt = new RptBase();
		rpt.setCampaignId(log.getCampaignId());
		return rpt;
	}

	@Override
	public RptBase userAndIpRpt(RptBase rpt, LogField log) {
		switch (log.getActionType()) {
		case IMPRESSION:
			if(notSameIp(log)){
				rpt.setImpIp(1);
			}
			if(notSameUser(log)){
				rpt.setImpUser(1);
			}
			break;
		case CLICK:
			if(notSameIp(log)){
				rpt.setClickIp(1);
			}
			if(notSameUser(log)){
				rpt.setClickUser(1);
			}
			break;
		case REACH:
			if(notSameIp(log)){
				rpt.setReachIp(1);
			}
			if(notSameUser(log)){
				rpt.setReachUser(1);
			}
			if(notSameIpVisit(log)){
				rpt.setVisitIp(1);
			}
			if(notSameUserVisit(log)){
				rpt.setVisitUser(1);
			}
			break;
		case CONVERSION:
			if(notSameIp(log)){
				rpt.setConversionIp(1);
			}
			if(notSameUser(log)){
				rpt.setConversionUser(1);
			}
			break;
		case VISIT:
			if(notSameIp(log)){
				rpt.setVisitIp(1);
			}
			if(notSameUser(log)){
				rpt.setVisitUser(1);
			}
			break;
		default:
			break;
		}
		return rpt;
	}

	private boolean notSameUser(LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":user:"+log.getActionType()+":ac:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+log.getUserId();
		String value = jedis.get(key);
		if(value==null){
			jedis.setex(key, DateUtils.expireTime, 1+"");
			return true;
		}else{
			jedis.incr(key);
			return false;
		}
		
	}

	private boolean notSameIp(LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":ip:"+log.getActionType()+":ac:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+log.getIp();
		String value = jedis.get(key);
		if(value==null){
			jedis.setex(key, DateUtils.expireTime, 1+"");
			return true;
		}else{
			jedis.incr(key);
			return false;
		}
	}
	
	private boolean notSameUserVisit(LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":user:"+"VISIT"+":ac:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+log.getUserId();
		String value = jedis.get(key);
		if(value==null){
			jedis.setex(key, DateUtils.expireTime, 1+"");
			return true;
		}else{
			jedis.incr(key);
			return false;
		}
		
	}

	private boolean notSameIpVisit(LogField log) {
		String key = TimeUtils.sdf.format(log.getRequestTime())+":ip:"+"VISIT"+":ac:"+log.getAccountId()+"_"+log.getCampaignId()+"_"+log.getIp();
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
