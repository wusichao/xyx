package com.xyx.x.servlet.inputter;

import com.xyx.x.model.Agent;
import com.xyx.x.model.enu.ActionType;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletInputter;
import com.xyx.x.utils.ServletUtils;

public class ServletAgentInputter implements XServletInputter<Action> {

	@Override
	public void input(ServletReqResp source, Action action) {
		if (action.getAgent() == null) {
			action.setAgent(new Agent());
		}
		
		action.getAgent().setUa(ServletUtils.getUserAgent(source.getRequest()));
		//当时访问类型时，referer,url,取前端传来参数，不自己获取
		if(action.getActionType()!=ActionType.REACH && action.getActionType()!=ActionType.VISIT){
			action.getAgent().setUrl(ServletUtils.getServiceUri(source.getRequest()));
			action.getAgent().setReferer((ServletUtils.getReferer(source.getRequest())));
		}
		
		
		
	}

}
