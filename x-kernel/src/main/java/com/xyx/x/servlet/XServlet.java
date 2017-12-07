/**
 * 
 */
package com.xyx.x.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyx.x.XProcessor;
import com.xyx.x.model.enu.ActionType;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.utils.Constant;
import com.xyx.x.utils.ServletUtils;

public class XServlet extends BaseServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1565743194692592298L;
	
	private XServletAdapter<? extends Action> adapter;
	
	private List<XServletInputter<? extends Action>> inputters = new ArrayList<>();
	
	private List<XProcessor<? extends Action>> processors = new ArrayList<>();
	
	private List<XServletOutputter<? extends Action>> outputters = new ArrayList<>();
	
	/* (non-Javadoc)
	 * @see com.x.servlet.BaseServlet#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) {
		
		ServletReqResp reqResp = new ServletReqResp(req, resp);
		
		Action action = null;
		
		if(adapter == null){
			action = new Action();
		}else{
			action  = adapter.adapter(reqResp);
		}
		
		if(action.getActionType()==ActionType.IMPRESSION || action.getActionType()==ActionType.CLICK){
			if(action.getDomainType()==2 && !ServletUtils.getRootDomain(req).equals(Constant.DOMAIN)){
				try{
					resp.sendRedirect(req.getScheme()+"://t"+Constant.DOMAIN+ServletUtils.getServiceUri(req));
				}catch(IOException e){
				}
				return ;
			}
		}
		
		//输入器
		for(XServletInputter inputter : inputters){
			inputter.input(reqResp, action);
		}
		
		//处理器
		for(XProcessor processor : processors){
			processor.process(action);
		}
		
		//输出器
		for(XServletOutputter outputter : outputters){
			outputter.output(action, reqResp);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.x.servlet.BaseServlet#initBean()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initBean() {
		String adapterBean = this.getInitParameter("adapter");
		if (adapterBean != null) {
			adapter = getBean(adapterBean, XServletAdapter.class);
		}
		String[] is = getBeanArray("inputters");
		for (String i : is) {
			inputters.add(getBean(i.trim(), XServletInputter.class));
		}
		String[] ps = getBeanArray("processors");
		for (String p : ps) {
			processors.add(getBean(p.trim(), XProcessor.class));
		}
		String[] os = getBeanArray("outputters");
		for (String o : os) {
			outputters.add(getBean(o.trim(), XServletOutputter.class));
		}
	}

}
