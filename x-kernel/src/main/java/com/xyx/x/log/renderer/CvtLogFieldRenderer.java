package com.xyx.x.log.renderer;

import com.xyx.x.XRenderer;
import com.xyx.x.log.enu.CvtLogField;
import com.xyx.x.model.kernel.action.CvtTracking;
import com.xyx.x.utils.ServletUtils;

public class CvtLogFieldRenderer implements XRenderer<CvtTracking, Object[]> {

	@Override
	public Object[] render(CvtTracking action) {
		Object[] ret = new Object[CvtLogField.values().length];

		if (action != null) {
			ret[CvtLogField.CVT_ID.ordinal()] = action.getCvtId();
			ret[CvtLogField.CLICK_TIME.ordinal()] = action.getClickTime();
			//编码
			if(action.getOther()!=null){
				ret[CvtLogField.CVT_OTHER.ordinal()] = ServletUtils.encode(action.getOther());
			}
			
		}
		return ret;
	}

}
