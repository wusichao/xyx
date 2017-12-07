/**
 * 
 */
package com.xyx.x.servlet.outputter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletOutputter;

public class PixelOutputter implements XServletOutputter<Action> {

	/**
	 * 1*1GIF透明图片
	 */
	public static byte[] GIF_1X1 = new byte[] { 0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x01, 0x00, 0x01, 0x00,
			(byte) 0x80, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00, 0x21, (byte) 0xF9, 0x04,
			0x01, 0x00, 0x00, 0x00, 0x00, 0x2C, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x44,
			0x01, 0x00, 0x3B };
	/**
	 * MIME:GIF
	 */
	public static String MIME_TYPE_IMG_GIF = "image/gif";
	
	@Override
	public void output(Action action, ServletReqResp dist) {
		HttpServletResponse response = dist.getResponse();
		response.setContentType(MIME_TYPE_IMG_GIF);
		try {
			OutputStream os = response.getOutputStream();
			os.write(GIF_1X1);
			os.flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		
	}

}
