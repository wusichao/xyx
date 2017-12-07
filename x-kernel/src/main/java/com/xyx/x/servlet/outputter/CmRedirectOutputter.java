package com.xyx.x.servlet.outputter;


import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.model.kernel.action.CookieMapping;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletOutputter;

public class CmRedirectOutputter implements XServletOutputter<CookieMapping>{
	private static final Logger logger = LoggerFactory.getLogger(CmRedirectOutputter.class);

	@Override
	public void output(CookieMapping action, ServletReqResp dist) {
		try {
			if(action.isError() || action.getDspUrl()==null){
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
				dist.getResponse().sendRedirect(action.getDspUrl()+action.getUser().getId());
			}
		} catch (IOException e) {
			logger.warn("跳转失败",e);
		}
		action.setResponseTime(System.currentTimeMillis());
	
	}
	

}
