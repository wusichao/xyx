package com.xyx.x.processor;

import com.xyx.x.XProcessor;
import com.xyx.x.model.kernel.action.Action;

public class ResponseTimeProcessor implements XProcessor<Action>{

	@Override
	public void process(Action action) {
		action.setResponseTime(System.currentTimeMillis());
	}

}
