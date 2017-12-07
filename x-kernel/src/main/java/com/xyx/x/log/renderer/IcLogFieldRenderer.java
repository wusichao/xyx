package com.xyx.x.log.renderer;

import com.xyx.x.XRenderer;
import com.xyx.x.log.enu.IcLogField;
import com.xyx.x.model.kernel.action.IcTracking;

public class IcLogFieldRenderer implements XRenderer<IcTracking, Object[]> {

	@Override
	public Object[] render(IcTracking action) {
		Object[] ret = new Object[IcLogField.values().length];

		if (action != null && action.getCampaign() != null) {
			ret[IcLogField.CAMPAIGN_ID.ordinal()] = action.getCampaign().getId();
			ret[IcLogField.CHANNEL_ID.ordinal()] = action.getChannelId();
			ret[IcLogField.CREATIVE_ID.ordinal()] = action.getCreativeId();
			ret[IcLogField.MEDIA_ID.ordinal()] = action.getMediaId();
		}

		return ret;
	}

}
