package com.xyx.x.param.parser;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.model.TrackingIds;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.utils.ServletUtils;


public class ServletParamsIdsParser implements IdsParser<ServletReqResp> {
	
	private static final Logger logger = LoggerFactory.getLogger(ServletParamsIdsParser.class);

	@Override
	public TrackingIds parse(ServletReqResp s) {
		TrackingIds ret =  new TrackingIds();
		
		HttpServletRequest req = s.getRequest();
		String accountId = req.getParameter("a");
		String creativeId = req.getParameter("e");
		String campaignId = req.getParameter("c");
		String channelId = req.getParameter("h");
		String mediaId = req.getParameter("m");
		String cvtId = req.getParameter("v");
		String other = req.getParameter("p");
		String type = req.getParameter("r");
		//获取访问referer ,url
		String referer = req.getParameter("f");
		String url = req.getParameter("u");
		String cheId = req.getParameter("i");
		
		if(StringUtils.isNotBlank(cheId)){
			try{
				ret.setCheId(cheId);
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时cheId参数类型错误！");
			}
		}
		
		if(StringUtils.isNotBlank(referer)){
			try{
				ret.setReferer(referer);
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时referer参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(url)){
			try{
				ret.setUrl(url);
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时url参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(type)){
			try{
				ret.setType(type);
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时type参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(other)){
			try{
				ret.setOther(other);
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时other参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(campaignId)){
			try{
				ret.setCampaignId(Long.parseLong(campaignId));
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时campaignId参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(accountId)){
			try{
				ret.setAccountId(Long.parseLong(accountId));
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时accountId参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(creativeId)){
			try{
				ret.setCreativeId(Long.parseLong(creativeId));
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时creativeId参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(channelId)){
			try{
				ret.setChannelId(Long.parseLong(channelId));
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时channelId参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(mediaId)){
			try{
				ret.setMediaId(Long.parseLong(mediaId));
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时mediaId参数类型错误！");
			}
		}
		if(StringUtils.isNotBlank(cvtId)){
			try{
				ret.setCvtId(Long.parseLong(cvtId));
			}catch(NumberFormatException nfe){
				logger.warn("解析url\""+ServletUtils.getServiceUri(s.getRequest())+"\"时cvtId参数类型错误！");
			}
		}
		
		return ret;
	}

}
