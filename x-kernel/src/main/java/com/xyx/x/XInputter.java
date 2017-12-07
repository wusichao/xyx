/**
 * 
 */
package com.xyx.x;

import com.xyx.x.model.kernel.action.Action;

public interface XInputter<S,D extends Action> {

	public void input(S source,D action);
}
