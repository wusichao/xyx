package com.xyx.ls.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.xyx.ls.model.account.Account;

public class LsFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 从session中获取登录者实体
		Account account = (Account) request.getSession()
				.getAttribute("account");
		String uri = request.getRequestURI();
		if (uri.indexOf("login") != -1 || uri.indexOf("addAccount") != -1
				|| uri.indexOf("register") != -1
				|| uri.indexOf("alidata") != -1
				|| uri.indexOf("sendCode") != -1
				|| uri.indexOf("resetPassword") != -1
				|| uri.indexOf("InvitationCode") != -1) {
			filterChain.doFilter(request, response);
		} else {
			if (null == account) {
				String loginUrl = request.getContextPath()
						+ "/loginUI.do";
				//判断是否是异步请求
				if (request.getHeader("x-requested-with") != null&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
					response.addHeader("sessionstatus","timeOut");
					response.addHeader("loginPath",loginUrl);
						//filterchain.doFilter(request, response);
				} else {//同步请求
					String str = "<script language='javascript'>alert('由于您过长时间未操作，为保证账户安全，请重新登录!');"
							+ "window.top.location.href='"
							+ loginUrl
							+ "';</script>";
					response.setContentType("text/html;charset=UTF-8");// 解决中文乱码 try
					PrintWriter writer = response.getWriter();
					writer.write(str);
					writer.flush();
					writer.close();
				}
				
//				response.sendRedirect("/ls/loginUI.do");
//				response.sendRedirect("/ls/html/login.html");
			} else {
				if (account.getEmail().equals("admin")) {
					filterChain.doFilter(request, response);
				} else {
					if (uri.indexOf("approv") == -1) {
						filterChain.doFilter(request, response);
					} else {
						response.sendRedirect("/ls/loginUI.do");
					}
				}

			}
		}

	}

}
