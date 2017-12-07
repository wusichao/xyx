/**
 * 
 */
package com.xyx.x;

import com.xyx.x.model.kernel.action.Action;

public interface XAdapter<S,D extends Action> {

	public D adapter(S source);
}
