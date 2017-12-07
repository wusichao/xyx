package com.xyx.ls.service.campaign;

import com.xyx.ls.base.BaseService;
import com.xyx.ls.model.campaign.Creative;

public interface CreativeService extends BaseService<Creative> {

	Creative findByCampaignId(Long id);

}
