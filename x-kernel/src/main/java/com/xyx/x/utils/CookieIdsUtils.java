package com.xyx.x.utils;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.xyx.x.model.TrackingIds;
import com.xyx.x.model.kernel.action.IcTracking;

public class CookieIdsUtils {

	private static final String SEP = "_";
	
	private static final int COOKIE_VALUE_LEN = 6;

	/**
	 * 生成点击cookie<br>
	 * 格式：actionId_campaignId_channelId_creativeId_mediaId_clickTime
	 * @param action IcTracking
	 * @return cookie的值
	 */
	public static String getCookieValue(IcTracking action) {
		StringBuilder ret = new StringBuilder(action.getActionId());
		
		ret.append(SEP).append(action.getCampaign().getId())
			.append(SEP).append(action.getChannelId() == null ? "" : action.getChannelId())
			.append(SEP).append(action.getCreativeId() == null ? "" : action.getCreativeId())
			.append(SEP).append(action.getMediaId() == null ? "" : action.getMediaId())
			.append(SEP).append(action.getRequestTime() == 0 ? "" : action.getRequestTime());

		return ret.toString();
	}

	/**
	 * 获取cookie的值，用来获取点击时的活动、渠道、创意、媒体等信息
	 * @param cookieValue cookie值
	 * @return id集合
	 */
	public static TrackingIds getIdsFromCookieValue(String cookieValue){
		TrackingIds ids = new TrackingIds();
		
		String[] strs = StringUtils.splitByWholeSeparatorPreserveAllTokens(cookieValue, SEP);
		if(strs == null || strs.length < COOKIE_VALUE_LEN){
			//cookie格式错误
			return ids;
		}

		int i = 0;
		ids.setActionPreId(strs[0]);
		ids.setCampaignId(Long.parseLong(strs[++i]));
		if(StringUtils.isNotBlank(strs[++i])){
			ids.setChannelId(Long.parseLong(strs[i]));
		}
		if(StringUtils.isNotBlank(strs[++i])){
			ids.setCreativeId(Long.parseLong(strs[i]));
		}
		if(StringUtils.isNotBlank(strs[++i])){
			ids.setMediaId(Long.parseLong(strs[i]));
		}
		if(StringUtils.isNotBlank(strs[++i])){
			ids.setClickTime(Long.parseLong(strs[i]));
		}
		
		return ids;
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.asList(StringUtils.split("kayf86@--@9@--@@--@5r43987@!@!%%^&^$%@!@!%-@@*&%$*(&^$%@!@!%--@", "@--@"))); 
		System.out.println(Arrays.asList(StringUtils.splitByWholeSeparator("kayf86@--@@--@9@--@5r43987@!@!%%^&^$%@!@!%-@@*&%$*(&^$%@!@!%--@", "@--@")));
		System.out.println(Arrays.asList(StringUtils.splitPreserveAllTokens("kayf86@--@@--@9@--@5r43987@!@!%%^&^$%@!@!%-@@*&%$*(&^$%@!@!%--@", "@--@")));
		System.out.println(Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens("kayf86@--@@--@9@--@5r43987@!@!%%^&^$%@!@!%-@@*&%$*(&^$%@!@!%--@", "@--@"))); 
	}
	
}
