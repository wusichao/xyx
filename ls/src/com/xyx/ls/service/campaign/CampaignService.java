package com.xyx.ls.service.campaign;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;

import com.xyx.ls.bean.GridBean;
import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.model.campaign.OperateLog;
public interface CampaignService {
	void add(Campaign campaign,String ids,String channelIds, String inversionpointIds,HttpServletRequest request, HttpServletResponse response);
	void delete(String ids,HttpServletRequest request,HttpServletResponse response);
	void update(Campaign campaign,String ids,String channelIds, String inversionpointIds,Object[] oldCampaignData, HttpServletRequest request, HttpServletResponse response);
	Campaign findById(Long id);
	GridBean<Campaign> findAll(Long id, String status, int page, int rows, String name, String sidx, String sord);
	List<Creative> findByCampaignId(Long id);
	Date findCreation(Long id);
	Campaign findByName(String name, Long long1);
	Object findChannelByName(String name, Long id);
	Object findPointByName(String name, Long id);
	List<Channel> findNoChannelList(Long id, Long id2);
	boolean findCreativeByName(String filerealName);
	Creative findCreativeByName(String filerealName, Long campaignId);
	void addCreative(Creative creative);
	void deleteCreative(String id);
	String findCreativeById(String id);
	List<Campaign> findByAccountAndName(String name, Long id);
	List<Creative> findCreativeByCampaign(Long id);
	List<ReportBean> isCampaignData(String ids);
	void falseDelete(String ids);
	List<Creative> isCreativeDtata(String ids);
	GridBean<OperateLog> getOperateLogData(Long id, int page, int rows,
			String sidx, String sord);
	GridBean<OperateLog> getOperate(Long id, int page, int rows, String sidx,
			String sord);
	String findCreativeNameById(String id);
	List<Campaign> getCampaignListByconversion(Long conversionId);
	void byChannelDownload(Account account, List<Creative> creativeList, Long iid, int channelId, HttpServletResponse response);
	List<Creative> findByCampaignIdNoPath(Long id);
	List<Creative> findCreativeNoPath(Long id);
	List<Campaign> findByAccount(Long id);
	int findCampaignType(long id);
}
