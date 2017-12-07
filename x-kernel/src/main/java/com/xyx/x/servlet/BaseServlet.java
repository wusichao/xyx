/**
 * 
 */
package com.xyx.x.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;

public abstract class BaseServlet extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7122232827906109599L;
	
	/**
	 * Spring context
	 */
	private WebApplicationContext wac;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
	protected <T> T getBean(String beanId, Class<T> beanClass) {
		return wac.getBean(beanId, beanClass);
	}
	
	protected String[] getBeanArray(String paramName) {
		String value = this.getInitParameter(paramName);
		if (value == null) {
			return new String[0];
		}
		return value.replaceAll("\r", "").replaceAll("\n", "").replaceAll(",", ";").split(";");
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		wac = (WebApplicationContext) this.getServletContext().getAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		initBean();
	}
	
	
	public abstract void process(HttpServletRequest req,HttpServletResponse resp);

	public abstract void initBean();

	
}
