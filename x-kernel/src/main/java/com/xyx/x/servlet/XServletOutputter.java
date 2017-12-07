/**
 * 
 */
package com.xyx.x.servlet;

import com.xyx.x.XOutputter;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;

public interface XServletOutputter<S extends Action> extends XOutputter<S,ServletReqResp> {

}
