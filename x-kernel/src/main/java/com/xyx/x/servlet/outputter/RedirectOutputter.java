package com.xyx.x.servlet.outputter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.model.kernel.action.IcTracking;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletOutputter;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.model.Campaign;

public class RedirectOutputter implements XServletOutputter<IcTracking> {
	private static final Logger logger = LoggerFactory.getLogger(RedirectOutputter.class);
	
	@Override
	public void output(IcTracking action, ServletReqResp dist) {
		try {
			if(action.isError() || action.getCampaign()==null){
				HttpServletResponse response = dist.getResponse();
				response.setContentType(PixelOutputter.MIME_TYPE_IMG_GIF);
				try {
					OutputStream os = response.getOutputStream();
					os.write(PixelOutputter.GIF_1X1);
					os.flush();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}else{
				Campaign campaign = action.getCampaign();
				String url;
				String target = ServletUtils.decode(campaign.getTargetUrl());
				if (action.getCreativeId() != null) {
						if(target.indexOf("?")==-1){
							url = target+"?xyx_c=" + campaign.getId() + "&xyx_h="
									+ action.getChannelId() + "&xyx_e=" + action.getCreativeId()+"&xyx_o="+action.getActionId();
						}else{
							url =target+"&xyx_c=" + campaign.getId() + "&xyx_h="
									+ action.getChannelId() + "&xyx_e=" + action.getCreativeId()+"&xyx_o="+action.getActionId();
						}
						
					}
					else{
						if(target.indexOf("?")==-1){
							url = target+"?xyx_c=" + campaign.getId() + "&xyx_h="
									+ action.getChannelId()+"&xyx_o="+action.getActionId();
						}else{
							url =target+"&xyx_c=" + campaign.getId() + "&xyx_h="
									+ action.getChannelId()+"&xyx_o="+action.getActionId();
						}
					}
				dist.getResponse().sendRedirect(url);
			}
		} catch (IOException e) {
			logger.warn("跳转失败",e);
		}
		action.setResponseTime(System.currentTimeMillis());
	
	}

}
