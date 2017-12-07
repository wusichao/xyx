package com.xyx.x.log.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.XProcessor;
import com.xyx.x.XRenderer;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.utils.StringUtils;

public class LogProcessor implements XProcessor<Action>{
	
	private Logger logger;
	
	private List<XRenderer<Action, Object[]>> renderers;
	
	private char colSpea = '\t';
	
	@Override
	public void process(Action action) {
		if (action == null) {
			return;
		}
		StringBuilder buf = new StringBuilder();
		for (XRenderer<Action, Object[]> fr : renderers) {
			Object[] fields = fr.render(action);
			if (fields != null && fields.length > 0) {
				for (Object field : fields) {
					buf.append(StringUtils.toLogSafeString(field)).append(colSpea);
				}
			}
		}
		
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		String log = buf.toString();
		logger.info(log);
		
	}
	
	public void setLogger(String logger){
		this.logger = LoggerFactory.getLogger(logger);
	}

	/**
	 * @param renderers the renderers to set
	 */
	public void setRenderers(List<XRenderer<Action, Object[]>> renderers) {
		this.renderers = renderers;
	}

	
}
