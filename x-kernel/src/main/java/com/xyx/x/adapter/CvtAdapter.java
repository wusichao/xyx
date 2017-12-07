package com.xyx.x.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.model.Campaign;
import com.xyx.x.model.TrackingIds;
import com.xyx.x.model.enu.ActionType;
import com.xyx.x.model.kernel.action.CvtTracking;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.param.parser.IdsParser;
import com.xyx.x.servlet.XServletAdapter;
import com.xyx.x.utils.CookieIdsUtils;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.utils.XidUtils;

public class CvtAdapter implements XServletAdapter<CvtTracking> {
	
	private static final Logger logger = LoggerFactory.getLogger(CvtAdapter.class);
	
	private IdsParser<ServletReqResp> idsParser;

	@Override
	public CvtTracking adapter(ServletReqResp source) {

		CvtTracking action = new CvtTracking();
		action.setActionType(ActionType.CONVERSION);
		action.setActionId(XidUtils.generateUuid());
		action.setRequestTime(System.currentTimeMillis());
		action.setActionUri(ServletUtils.getServiceUri(source.getRequest()));
		action.setSessionId(ServletUtils.getSessionId(source));
		
		TrackingIds ids = null;
		try {
			ids = idsParser.parse(source);
			
			if(ids.getAccountId()!=0){
				action.setAccountId(ids.getAccountId());
				
				//转化ID
				if(ids.getCvtId() != 0){
					action.setCvtId(ids.getCvtId());
					action.setOther(ids.getOther());
					String cookieKey = "x" + ids.getAccountId()+"x"+ids.getCvtId();
					String cv = ServletUtils.getCookie(cookieKey, source.getRequest());
					if(cv!=null){
						TrackingIds idsFromCookie = CookieIdsUtils.getIdsFromCookieValue(cv);
						
						if(idsFromCookie.getCampaignId()!=0){
							action.setActionPreId(idsFromCookie.getActionPreId());//点击id
							action.setCampaign(new Campaign(idsFromCookie.getCampaignId()));
							action.setChannelId(idsFromCookie.getChannelId());
							action.setCreativeId(idsFromCookie.getCreativeId());
							action.setMediaId(idsFromCookie.getMediaId());
							action.setClickTime(idsFromCookie.getClickTime());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.warn("CvtAdapter适配失败 "+action.getActionUri()+";原因:"+e.getLocalizedMessage());
			action.setError(true);
			return action;
		}
		
		return action;
	}

	/**
	 * @param idsParser
	 *            the idsParser to set
	 */
	public void setIdsParser(IdsParser<ServletReqResp> idsParser) {
		this.idsParser = idsParser;
	}

}
