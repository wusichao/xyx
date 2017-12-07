package com.xyx.x;

import com.xyx.x.model.kernel.action.Action;

public interface XProcessor<T extends Action> {

	public void process(T action);
	
}
