package com.xyx.x.servlet.inputter;

import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletInputter;

public class ServletActionInputter implements XServletInputter<Action> {

	private ServletUserInputter servletUserInputter;

	private ServletGeoInputter servletGeoInputter;

	private ServletAgentInputter servletAgentInputter;

	@Override
	public void input(ServletReqResp source, Action action) {

		if (servletUserInputter != null) {
			servletUserInputter.input(source, action);
		}
		
		if (servletGeoInputter != null) {
			servletGeoInputter.input(source, action);
		}
		
		if (servletAgentInputter != null) {
			servletAgentInputter.input(source, action);
		}

	}

	/**
	 * @param servletUserInputter
	 *            the servletUserInputter to set
	 */
	public void setServletUserInputter(ServletUserInputter servletUserInputter) {
		this.servletUserInputter = servletUserInputter;
	}

	/**
	 * @param servletGeoInputter
	 *            the servletGeoInputter to set
	 */
	public void setServletGeoInputter(ServletGeoInputter servletGeoInputter) {
		this.servletGeoInputter = servletGeoInputter;
	}

	/**
	 * @param servletAgentInputter
	 *            the servletAgentInputter to set
	 */
	public void setServletAgentInputter(ServletAgentInputter servletAgentInputter) {
		this.servletAgentInputter = servletAgentInputter;
	}

}
