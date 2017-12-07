package com.xyx.ls.dao.campaign;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.model.campaign.Inversionpoint;
import com.xyx.ls.model.campaign.OperateLog;

public interface CampaignMapper {
	void add(Campaign campaign);
	void delete(String ids);
	void update(Campaign campaign);
	Campaign findById(Long ids);
	List<Campaign> findAll(HashMap<String, String> map);
	int getRecords(String name, String state, Long id);
	void addCampaignChannel(Long id, Long channelId);
	void deleteCampaignChannel(String ids);
	void addCampaignInversionpoint(Long id, long l);
	void addCampaignCreative(Long id, long l);
	void deleteCampaignCreative(String ids);
	void deleteCampaignInversionpoint(String ids);
	void addCreative(Creative creative);
	List<Creative> findByCampaignId(Long id);
	Date findCreation(Long id);
	Campaign findByName(String name, Long long1);
	Channel findChannelByName(String name, Long id);
	Inversionpoint findPointByName(String name, Long id);
	void deleteCreativeByCampaign(String id);
	List<Channel> findNoChannelList(Long id, Long id2);
	Creative findCreativeByName(String filerealName, Long campaignId);
	String findCreativeById(String id);
	void deleteCampaignCreativeId(String id);
	List<Campaign> findByAccountAndName(String name, Long id);
	Creative findCreativeByIds(String string);
	List<ReportBean> isCampaignData(String ids);
	void falseDelete(String ids);
	List<Creative> isCreativeDtata(String ids);
	void savaOperateLog(OperateLog operateLog);
	void setBaseData(String sqlChannel1);
	void setGeoData(String sqlbeijing);
	void setHourData(String sqla);
	int getOperateLogDataRecords(Long id);
	List<OperateLog> getOperateLogData(HashMap<String, String> map);
	OperateLog getOperate(Long id);
	String findCreativeNameById(String id);
	List<Campaign> getCampaignListByconversion(Long conversionId);
	List<Creative> findByCampaignIdNoPath(Long id);
	List<Creative> findCreativeNoPath(Long id);
	List<Campaign> findByAccount(Long id);
	int findCampaignType(long id);
}
