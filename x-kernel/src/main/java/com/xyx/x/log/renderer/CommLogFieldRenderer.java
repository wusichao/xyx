package com.xyx.x.log.renderer;

import com.xyx.x.XRenderer;
import com.xyx.x.log.enu.CommLogField;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.utils.TimeUtils;

public class CommLogFieldRenderer implements XRenderer<Action, Object[]> {

	@Override
	public Object[] render(Action action) {
		Object[] ret = new Object[CommLogField.values().length];
		
		if(action != null){
			ret[CommLogField.ACTION_ID.ordinal()] = action.getActionId();
			ret[CommLogField.ACTION_PRE_ID.ordinal()] = action.getActionPreId();
			ret[CommLogField.REQUEST_TIME.ordinal()] = TimeUtils.formatMills(action.getRequestTime());
			ret[CommLogField.RESPONSE_TIME.ordinal()] = TimeUtils.formatMills(action.getResponseTime());
			ret[CommLogField.SESSION_ID.ordinal()] = action.getSessionId();
			ret[CommLogField.ACTION_URI.ordinal()] = action.getActionUri();
			ret[CommLogField.ACTION_TYPE.ordinal()] = action.getActionType();
			ret[CommLogField.ACCOUNT_ID.ordinal()] = action.getAccountId();
			
			if(action.getUser() != null){
				ret[CommLogField.USER_ID.ordinal()] = action.getUser().getId();
			}
			
			if(action.getAgent() != null){
				ret[CommLogField.AGENT_URL.ordinal()] = ServletUtils.encode(action.getAgent().getUrl());
				ret[CommLogField.AGENT_REFERER.ordinal()] = ServletUtils.encode(action.getAgent().getReferer());
				ret[CommLogField.AGENT_UA.ordinal()] = action.getAgent().getUa();
			}
			
			if(action.getGeo() != null){
				ret[CommLogField.GEO_IP.ordinal()] = action.getGeo().getIp();
				ret[CommLogField.GEO_OFFSET_MINUTES.ordinal()] = action.getGeo().getOffsetMinutes();
				ret[CommLogField.GEO_ID.ordinal()] = action.getGeo().getId();
			}
		}
		
		return ret;
	}

}
