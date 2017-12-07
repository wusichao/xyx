package com.xyx.ls.dao.campaign;

import java.util.List;

import com.xyx.ls.model.campaign.Creative;

public interface CreativeMapper {
	void add(Creative creative);
	void delete(String ids);
	void update(Creative creative);
	Creative findByIds(String ids);
	List<Creative> findAll(String name);
	void addCreativeAccount(Long accountId, Long id);
	void deleteCreativeAccount(String ids, Long accountId);
	Creative findByCampaignId(Long id);
}
