package com.xyx.ls.service.impl.campaign;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyx.ls.dao.campaign.InversionpointMapper;
import com.xyx.ls.model.campaign.Inversionpoint;
import com.xyx.ls.service.campaign.InversionpointService;
@Service
@Transactional
public class InversionpointServiceImpl implements InversionpointService{
	@Resource
	private InversionpointMapper inversionpointMapper;
	
	public InversionpointMapper getInversionpointMapper() {
		return inversionpointMapper;
	}

	public void setInversionpointMapper(InversionpointMapper inversionpointMapper) {
		this.inversionpointMapper = inversionpointMapper;
	}

	@Override
	public void add(Inversionpoint inversionpoint, Long accountId) {
		inversionpoint.setCreation(new Date());
		inversionpoint.setLast_modified(new Date());
		inversionpointMapper.add(inversionpoint);
		inversionpointMapper.addInversionpointAccount(accountId,inversionpoint.getId());
	}

	@Override
	public void delete(String ids, Long accountId) {
		inversionpointMapper.deleteInversionpointCampaign(ids);
		inversionpointMapper.deleteInversionpointAccount(ids, accountId);
		inversionpointMapper.delete(ids);
	}

	@Override
	public void update(Inversionpoint inversionpoint) {
		inversionpoint.setLast_modified(new Date());
		inversionpointMapper.update(inversionpoint);
	}

	@Override
	public Inversionpoint findByIds(String ids) {
		return inversionpointMapper.findByIds(ids);
	}

	@Override
	public List<Inversionpoint> findAll(String name) {
		return inversionpointMapper.findAll(name);
	}

	@Override
	public List<Inversionpoint> findByCampaignId(Long id) {
		return inversionpointMapper.findByCampaignId(id);
	}

	@Override
	public List<Inversionpoint> findAllByAccountId(Long id, String name) {
		return inversionpointMapper.findAllByAccountId(id,name);
	}

	@Override
	public Inversionpoint findByName(String name, Long id) {
		return inversionpointMapper.findByName(name,id);
	}

	@Override
	public boolean findCampaignByConvertion(String id) {
		if(inversionpointMapper.findCampaignByConvertion(id)!=0){
			return false;
		}
		
		return true;
	}

}
