package com.xyx.x.param.parser;

import com.xyx.x.model.TrackingIds;

public interface IdsParser<S> {
	public TrackingIds parse(S s);
}
