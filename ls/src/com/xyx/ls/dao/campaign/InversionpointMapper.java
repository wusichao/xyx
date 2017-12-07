package com.xyx.ls.dao.campaign;

import java.util.List;

import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Inversionpoint;

public interface InversionpointMapper {
	void add(Inversionpoint inversionpoint);
	void delete(String ids);
	void update(Inversionpoint inversionpoint);
	Inversionpoint findByIds(String ids);
	List<Inversionpoint> findAll(String name);
	void addInversionpointAccount(Long accountId, Long id);
	void deleteInversionpointAccount(String ids, Long accountId);
	List<Inversionpoint> findByCampaignId(Long id);
	List<Inversionpoint> findAllByAccountId(Long id, String name);
	void deleteInversionpointCampaign(String ids);
	Inversionpoint findByName(String name, Long id);
	int findCampaignByConvertion(String id);
}
