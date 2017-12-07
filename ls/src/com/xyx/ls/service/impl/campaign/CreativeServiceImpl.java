package com.xyx.ls.service.impl.campaign;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyx.ls.dao.campaign.CreativeMapper;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.service.campaign.CreativeService;
@Service
@Transactional
public class CreativeServiceImpl implements CreativeService{
	@Resource
	private CreativeMapper creativeMapper;
	
	public CreativeMapper getCreativeMapper() {
		return creativeMapper;
	}

	public void setCreativeMapper(CreativeMapper creativeMapper) {
		this.creativeMapper = creativeMapper;
	}

	@Override
	public void add(Creative e, Long accountId) {
		creativeMapper.add(e);
		creativeMapper.addCreativeAccount(accountId, e.getId());
	}

	@Override
	public void delete(String ids, Long accountId) {
		creativeMapper.deleteCreativeAccount(ids, accountId);
		creativeMapper.delete(ids);
	}

	@Override
	public void update(Creative creative) {
		creativeMapper.update(creative);
	}

	@Override
	public Creative findByIds(String ids) {
		return creativeMapper.findByIds(ids);
	}

	@Override
	public List<Creative> findAll(String name) {
		return creativeMapper.findAll(name);
	}

	@Override
	public Creative findByCampaignId(Long id) {
		return creativeMapper.findByCampaignId(id);
	}

}
