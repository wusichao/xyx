package com.xyx.x.servlet.inputter;

import com.xyx.x.model.User;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletInputter;
import com.xyx.x.utils.Constant;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.utils.XidUtils;

public class ServletUserInputter implements XServletInputter<Action> {

	@Override
	public void input(ServletReqResp source, Action action) {
		
		if(action.getUser() == null){
			action.setUser(new User());
		}
		
		boolean newUser = false;
		String xid = ServletUtils.getCookie(Constant.KEY_USER_ID, source.getRequest());
		
		if(xid == null){
			newUser = true;
			xid = XidUtils.generateUserId();
			ServletUtils.setCookie(Constant.KEY_USER_ID, xid, source);
		}
		
		action.getUser().setNewUser(newUser);
		action.getUser().setId(xid);
		
	}

}
