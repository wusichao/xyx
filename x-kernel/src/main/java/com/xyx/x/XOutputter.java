package com.xyx.x;

import com.xyx.x.model.kernel.action.Action;

public interface XOutputter<S extends Action,D> {

	public void output(S source,D dist);
}
