/**
 * 
 */
package com.xyx.x.servlet;

import com.xyx.x.XAdapter;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;

public interface XServletAdapter<D extends Action> extends XAdapter<ServletReqResp, D> {

}
