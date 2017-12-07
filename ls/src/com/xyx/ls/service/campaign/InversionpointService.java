package com.xyx.ls.service.campaign;

import java.util.List;

import com.xyx.ls.base.BaseService;
import com.xyx.ls.model.campaign.Inversionpoint;

public interface InversionpointService extends BaseService<Inversionpoint>{

	List<Inversionpoint> findByCampaignId(Long id);

	List<Inversionpoint> findAllByAccountId(Long id, String name);

	Inversionpoint findByName(String name, Long id);

	boolean findCampaignByConvertion(String ids);

}
