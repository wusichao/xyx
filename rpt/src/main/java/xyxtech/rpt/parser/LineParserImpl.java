package xyxtech.rpt.parser;

import org.apache.commons.lang.StringUtils;

import com.xyx.x.log.enu.CommLogField;
import com.xyx.x.log.enu.CvtLogField;
import com.xyx.x.log.enu.IcLogField;
import com.xyx.x.model.enu.ActionType;
import com.xyx.x.utils.TimeUtils;

import xyxtech.rpt.data.LogField;

public class LineParserImpl implements LineParser {

	@Override
	public LogField parseLine(String line) {
		LogField ret = new LogField();

		String[] columns = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "\t");
		
		if (columns.length < CommLogField.values().length) {
			return ret;
		}
		
		ret.setAccountId(com.xyx.x.utils.StringUtils.toLong(columns[CommLogField.ACCOUNT_ID.ordinal()]));
		if(ret.getAccountId()==0)
			return ret;

		ret.setActionId(columns[CommLogField.ACTION_ID.ordinal()]);
		ret.setActionPreId(columns[CommLogField.ACTION_PRE_ID.ordinal()]);
		ret.setRequestTime(TimeUtils.getDateFromStr(columns[CommLogField.REQUEST_TIME.ordinal()]));
		ret.setActionType(ActionType.valueOf(columns[CommLogField.ACTION_TYPE.ordinal()]));
		ret.setUserId(columns[CommLogField.USER_ID.ordinal()]);
		ret.setGeoId(com.xyx.x.utils.StringUtils.toInt(columns[CommLogField.GEO_ID.ordinal()]));
		ret.setIp(columns[CommLogField.GEO_IP.ordinal()]);
		ret.setUrl(columns[CommLogField.AGENT_URL.ordinal()]);
		ret.setReferer(columns[CommLogField.AGENT_REFERER.ordinal()]);

		int commLen = CommLogField.values().length;
		ret.setCampaignId(com.xyx.x.utils.StringUtils.toLong(columns[commLen + IcLogField.CAMPAIGN_ID.ordinal()]));
		ret.setChannelId(com.xyx.x.utils.StringUtils.toLong(columns[commLen + IcLogField.CHANNEL_ID.ordinal()]));
		if(ret.getCampaignId()==0 || ret.getChannelId()==0){
			if(ret.getActionType() != ActionType.CONVERSION&&ret.getActionType() != ActionType.REACH &&ret.getActionType() != ActionType.VISIT)
				ret.setActionType(null);
		}else{
			ret.setCreativeId(com.xyx.x.utils.StringUtils.toLong(columns[commLen + IcLogField.CREATIVE_ID.ordinal()]));
			ret.setMediaId(com.xyx.x.utils.StringUtils.toLong(columns[commLen + IcLogField.MEDIA_ID.ordinal()]));
		}
		
		//转化日志
		if(ret.getActionType() == ActionType.CONVERSION){
			ret.setCvtId(com.xyx.x.utils.StringUtils.toLong(columns[commLen + IcLogField.values().length + CvtLogField.CVT_ID.ordinal()]));
			ret.setClickTime(new java.util.Date(com.xyx.x.utils.StringUtils.toLong(columns[commLen + IcLogField.values().length + CvtLogField.CLICK_TIME.ordinal()])));
			//解码
			if(columns[commLen + IcLogField.values().length + CvtLogField.CVT_OTHER.ordinal()]!=null){
				ret.setOther(columns[commLen + IcLogField.values().length + CvtLogField.CVT_OTHER.ordinal()]);
			}
			
			if(ret.getCvtId()==0)
				ret.setActionType(null);
		}
		
		return ret;
	}
	
}
