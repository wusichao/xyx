package com.xyx.x;

import com.xyx.x.model.kernel.action.Action;

public interface XRenderer<S extends Action,D> {

	public D render(S action);
}
