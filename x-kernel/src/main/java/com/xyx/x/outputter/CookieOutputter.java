package com.xyx.x.outputter;

import java.util.Set;

import com.xyx.x.model.enu.ActionType;
import com.xyx.x.model.kernel.action.IcTracking;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletOutputter;
import com.xyx.x.utils.Constant;
import com.xyx.x.utils.CookieIdsUtils;
import com.xyx.x.utils.ServletUtils;

public class CookieOutputter implements XServletOutputter<IcTracking> {

	@Override
	public void output(IcTracking action, ServletReqResp dist) {
		
		if(action.getCampaign() != null && action.getAccountId() != null&&action.getActionType()==ActionType.REACH){
			//生成浏览追溯cookie
			ServletUtils.setCookie("r" + action.getAccountId(), CookieIdsUtils.getCookieValue(action), -1,dist);
			//生成用于点击转化回溯的cookie
			Set<Long> cvIds=action.getCampaign().getConvertionIdSet();
			cvIds.forEach( cvId ->{
			ServletUtils.setCookie("x" + action.getAccountId()+"x"+cvId, CookieIdsUtils.getCookieValue(action), Constant.CACHED_COOKIE_EXPIRE_SECONDS,dist);
			});
			
		}
	}

}
