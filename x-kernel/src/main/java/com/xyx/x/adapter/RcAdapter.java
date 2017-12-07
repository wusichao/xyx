package com.xyx.x.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.center.CaCenter;
import com.xyx.x.model.Agent;
import com.xyx.x.model.Campaign;
import com.xyx.x.model.TrackingIds;
import com.xyx.x.model.enu.ActionType;
import com.xyx.x.model.kernel.action.IcTracking;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.param.parser.IdsParser;
import com.xyx.x.servlet.XServletAdapter;
import com.xyx.x.utils.CookieIdsUtils;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.utils.XidUtils;

public class RcAdapter implements XServletAdapter<IcTracking> {

	private static final Logger logger = LoggerFactory.getLogger(RcAdapter.class);

	private IdsParser<ServletReqResp> idsParser;
	private CaCenter caCenter;

	@Override
	public IcTracking adapter(ServletReqResp source) {

		IcTracking action = new IcTracking();
		action.setActionType(ActionType.VISIT);
		action.setActionId(XidUtils.generateUuid());
		action.setRequestTime(System.currentTimeMillis());
		action.setActionUri(ServletUtils.getServiceUri(source.getRequest()));
		action.setSessionId(ServletUtils.getSessionId(source));

		TrackingIds ids = null;
		try {
			ids = idsParser.parse(source);
			// 通过前端t参数获取优化类型,及访问类型
			if (ids.getAccountId() != 0) {
				action.setAccountId(ids.getAccountId());
				if (action.getAgent() == null) {
					action.setAgent(new Agent());
				}
				action.getAgent().setReferer(ids.getReferer());
				action.getAgent().setUrl(ids.getUrl());
				if ("v".equals(ids.getType()) || "r".equals(ids.getType())) {
					String cv = ids.getCheId();
					String cookieKey = "r" + String.valueOf(ids.getAccountId());
					String cvc = ServletUtils.getCookie(cookieKey, source.getRequest());
					TrackingIds idsFromCookie = null;
					if (cv != null) {
						idsFromCookie = CookieIdsUtils.getIdsFromCookieValue(cv);
					} else if (cvc != null) {
						idsFromCookie = CookieIdsUtils.getIdsFromCookieValue(cvc);
					}
					if (idsFromCookie != null && idsFromCookie.getCampaignId() != 0 && idsFromCookie.getChannelId() != 0
							&& caCenter != null) {
						Campaign ca = caCenter.hitCampaign(idsFromCookie.getCampaignId());
						if (ca != null && ca.getAccount() != null) {
							action.setCampaign(ca);
							// 渠道ID
							if (ca.getChannelIdSet() != null
									&& ca.getChannelIdSet().contains(idsFromCookie.getChannelId())) {
								action.setChannelId(idsFromCookie.getChannelId());
								if ("r".equals(ids.getType())) {
									action.setActionType(ActionType.REACH);
								}
							}

							// 创意ID
							if (ca.getCreativeIdSet() != null
									&& ca.getCreativeIdSet().contains(idsFromCookie.getCreativeId())) {
								action.setCreativeId(idsFromCookie.getCreativeId());
							}

							// 媒体ID
							if (ca.getMediaIdSet() != null && ca.getMediaIdSet().contains(idsFromCookie.getMediaId())) {
								action.setMediaId(idsFromCookie.getMediaId());
							}
						}
						if (action.getActionType() == ActionType.REACH) {
							action.setActionPreId(idsFromCookie.getActionPreId());// 点击id
						}
					}

				}
			}

		} catch (Exception e) {
			logger.warn("RcAdapter适配失败 " + action.getActionUri()+";原因:"+e.getLocalizedMessage());
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

	public void setCaCenter(CaCenter caCenter) {
		this.caCenter = caCenter;
	}

}
