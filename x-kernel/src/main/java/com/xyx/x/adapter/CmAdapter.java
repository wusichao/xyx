package com.xyx.x.adapter;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.model.kernel.action.CookieMapping;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.param.parser.IdsParser;
import com.xyx.x.servlet.XServletAdapter;
import com.xyx.x.utils.ServletUtils;

public class CmAdapter implements XServletAdapter<CookieMapping>{
	private static final Logger logger = LoggerFactory.getLogger(CmAdapter.class);
	private IdsParser<ServletReqResp> idsParser;
	@Override
	public CookieMapping adapter(ServletReqResp source) {
		CookieMapping action = new CookieMapping();
		try {
			Long dspId = Long.parseLong(source.getRequest().getParameter("d"));
			if(dspId!=0){
				action.setDspUrl(getDspUrl(dspId));
			}
		} catch (Exception e) {
			logger.warn("CmAdapter适配失败 "+ServletUtils.getServiceUri(source.getRequest())+";原因:"+e.getLocalizedMessage());
			action.setError(true);
			return action;
		}
		return action;
	}
	
	//模拟dsp数据库
	private String getDspUrl(Long dspId) {
		Map<Long,String> map = new HashMap<Long,String>();
		map.put(1L, "https://cm.ipinyou.com/tanx/cms.gif?XID=");
		return map.get(dspId);
	}

	public IdsParser<ServletReqResp> getIdsParser() {
		return idsParser;
	}

	public void setIdsParser(IdsParser<ServletReqResp> idsParser) {
		this.idsParser = idsParser;
	}

}
