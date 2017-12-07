package com.xyx.ls.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xyx.ls.model.account.Account;

public class SessionInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// 从session中获取登录者实体
		String uri = request.getRequestURI();
				Account account = (Account) request.getSession()
						.getAttribute("account");
				if(null!=account)return true;
				
				else if (uri.indexOf("login") != -1 || uri.indexOf("addAccount") != -1
						|| uri.indexOf("register") != -1
						|| uri.indexOf("alidata") != -1
						|| uri.indexOf("sendCode") != -1
						|| uri.indexOf("resetPassword") != -1
						|| uri.indexOf("InvitationCode") != -1) 
					return true;
				else {
					response.sendRedirect("/ls/html/login.html");
					return false;
				}
	}

}
