package com.xyx.x.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.center.CaCenter;
import com.xyx.x.model.Campaign;
import com.xyx.x.model.TrackingIds;
import com.xyx.x.model.enu.ActionType;
import com.xyx.x.model.kernel.action.IcTracking;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.param.parser.IdsParser;
import com.xyx.x.servlet.XServletAdapter;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.utils.XidUtils;

public class IcAdapter implements XServletAdapter<IcTracking>{
	
	private static final Logger logger = LoggerFactory.getLogger(IcAdapter.class);

	private CaCenter caCenter;
	
	private IdsParser<ServletReqResp> idsParser;
	
	private ActionType actionType;

	@Override
	public IcTracking adapter(ServletReqResp source) {
		
		IcTracking action = new IcTracking();
		action.setActionType(actionType);
		action.setActionId(XidUtils.generateUuid());
		action.setRequestTime(System.currentTimeMillis());
		action.setActionUri(ServletUtils.getServiceUri(source.getRequest()));
		action.setSessionId(ServletUtils.getSessionId(source));
		
		//TODO 可以考虑把inputter提前，这样便于在idParse出错时提示更多有用信息
		
		TrackingIds ids = null;
		try {
			ids = idsParser.parse(source);
			
			if(ids.getCampaignId()!=0 && caCenter!=null){
				Campaign ca=caCenter.hitCampaign(ids.getCampaignId());
				
				if(ca!=null && ca.getAccount() != null){
					action.setCampaign(ca);
					
					//账户ID
					action.setAccountId(ca.getAccountId());
					action.setDomainType(ca.getAccount().getDomainType());
					
					//渠道ID
					if(ca.getChannelIdSet() != null
							&& ca.getChannelIdSet().contains(ids.getChannelId())){
						action.setChannelId(ids.getChannelId());
					}
					
					//创意ID
					if(ca.getCreativeIdSet() != null
							&& ca.getCreativeIdSet().contains(ids.getCreativeId())){
						action.setCreativeId(ids.getCreativeId());
					}
					
					// 媒体ID
					if(ca.getMediaIdSet() != null
							&& ca.getMediaIdSet().contains(ids.getMediaId())){
						action.setMediaId(ids.getMediaId());
					}
				}
			}
		} catch (Exception e) {
			logger.warn("IcAdapter适配失败.url:"+action.getActionUri()+";原因:"+e.getLocalizedMessage());
			action.setError(true);
			return action;
		}
		return action;
	}

	/**
	 * @param caCenter the caCenter to set
	 */
	public void setCaCenter(CaCenter caCenter) {
		this.caCenter = caCenter;
	}

	/**
	 * @param idsParser the idsParser to set
	 */
	public void setIdsParser(IdsParser<ServletReqResp> idsParser) {
		this.idsParser = idsParser;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
	
	
}
