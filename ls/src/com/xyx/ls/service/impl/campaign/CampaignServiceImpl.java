package com.xyx.ls.service.impl.campaign;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyx.ls.bean.GridBean;
import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.dao.campaign.CampaignMapper;
import com.xyx.ls.dao.campaign.ChannelMapper;
import com.xyx.ls.dao.campaign.InversionpointMapper;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.model.campaign.Inversionpoint;
import com.xyx.ls.model.campaign.OperateLog;
import com.xyx.ls.service.campaign.CampaignService;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.utils.XidUtils;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Service
public class CampaignServiceImpl implements CampaignService {
	@Resource
	private CampaignMapper campaignMapper;
	@Resource
	private InversionpointMapper inversionpointMapper;
	@Resource
	private ChannelMapper channelMapper;

	public InversionpointMapper getInversionpointMapper() {
		return inversionpointMapper;
	}

	public void setInversionpointMapper(InversionpointMapper inversionpointMapper) {
		this.inversionpointMapper = inversionpointMapper;
	}

	public CampaignMapper getCampaignMapper() {
		return campaignMapper;
	}

	public void setCampaignMapper(CampaignMapper campaignMapper) {
		this.campaignMapper = campaignMapper;
	}
	
	@Override
	public void add(Campaign campaign, String creativeIds, String channelIds,
			String inversionpointIds, HttpServletRequest request,
			HttpServletResponse response) {
		campaign.setCreation(new Date());
		campaign.setLast_modified(new Date());
		campaignMapper.add(campaign);
		String changeItem ="";
		String changeBefore="";
		String changeAfter="";
		SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd");
			changeItem = changeItem + ",活动名称";
			changeAfter=changeAfter+","+campaign.getName();
			changeItem = changeItem + ",活动花费";
			changeAfter=changeAfter+","+campaign.getCost();
			changeItem = changeItem + ",活动开始时间";
			changeAfter=changeAfter+","+times.format(campaign.getBegin_date());
			changeItem = changeItem + ",活动结束时间";
			changeAfter=changeAfter+","+times.format(campaign.getEnd_date());
			changeItem = changeItem + ",分析类型";
			changeAfter=changeAfter+","+campaign.getMonitor_type();
			changeItem = changeItem + ",活动结算单价";
			changeAfter=changeAfter+","+campaign.getUnit_price();
			changeItem = changeItem + ",活动结算类型";
			changeAfter=changeAfter+","+campaign.getCost_type();
			changeItem = changeItem + ",活动到达地址";
			changeAfter=changeAfter+","+campaign.getTarget_url();
			if (!"".equals(channelIds) && channelIds != null) {
				String after="";
			String[] id_Array = channelIds.split(",");
			for (String check_id : id_Array) {
				long l = Long.parseLong(check_id.trim());
				campaignMapper.addCampaignChannel(campaign.getId(), l);
				Channel channel = channelMapper.findByIds(l+"");
				after=after+channel.getName()+" ";
			}
			changeItem=changeItem+",渠道";
			changeAfter=changeAfter+","+after;
			}
		if (!"".equals(inversionpointIds) && inversionpointIds != null) {
			String[] id_Array1 = inversionpointIds.split(",");
			String after="";
			for (String check_id : id_Array1) {
				long l = Long.parseLong(check_id.trim());
				campaignMapper.addCampaignInversionpoint(campaign.getId(), l);
				Inversionpoint inversionpoint=inversionpointMapper.findByIds(l+"");
				after=after+inversionpoint.getName()+" ";
			}
			changeItem=changeItem+",转化点";
			changeAfter=changeAfter+","+after;
		}
		if (!"".equals(creativeIds) && creativeIds != null) {
			String after="";
			String[] id_Array2 = creativeIds.split(",");
			for (String check_id : id_Array2) {
				long l = Long.parseLong(check_id.trim());
				campaignMapper.addCampaignCreative(campaign.getId(), l);
				Creative creative= campaignMapper.findCreativeByIds(l+"");
				after=after+creative.getName()+" ";
			}
			changeItem=changeItem+",创意";
			changeAfter=changeAfter+","+after;
			}
		String ip = ServletUtils.getRemoteAddr(request);
		String xid = ServletUtils.getCookie("XID",request);
		if(xid == null){
			xid = XidUtils.generateUserId();
			ServletUtils.setCookie("XID", xid,new ServletReqResp(request, response));
		}
		OperateLog operateLog = new OperateLog();
		operateLog.setCampaignId(campaign.getId());
		operateLog.setOperateTime(new Date());
		operateLog.setIP(ip);
		operateLog.setUserId(xid);
		operateLog.setOperateType("添加");
		operateLog.setChangeItem(changeItem);
		operateLog.setChangeBefore(changeBefore);
		operateLog.setChangeAfter(changeAfter);
		campaignMapper.savaOperateLog(operateLog);
	}
	@Override
	@Transactional
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response) {
		String ip = ServletUtils.getRemoteAddr(request);
		String xid = ServletUtils.getCookie("XID",request);
		if(xid == null){
			xid = XidUtils.generateUserId();
			ServletUtils.setCookie("XID", xid,new ServletReqResp(request, response));
		}
		campaignMapper.deleteCampaignChannel(ids);
		campaignMapper.deleteCampaignCreative(ids);
		campaignMapper.deleteCampaignInversionpoint(ids);
		campaignMapper.delete(ids);
		OperateLog operateLog = new OperateLog();
		operateLog.setCampaignId(Long.valueOf(ids));
		operateLog.setOperateTime(new Date());
		operateLog.setIP(ip);
		operateLog.setUserId(xid);
		operateLog.setOperateType("删除");
		campaignMapper.savaOperateLog(operateLog);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Campaign campaign, String creativeIds,
			String channelIds, String inversionpointIds,Object[] oldCampaignData,
			HttpServletRequest request, HttpServletResponse response) {
		String changeItem ="";
		String changeBefore="";
		String changeAfter="";
		//活动基本信息
		if(campaignIsChange((Campaign)oldCampaignData[0],campaign,changeItem,changeBefore,changeAfter)){
			SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd");
			Campaign object=(Campaign)oldCampaignData[0];
			if (!object.getName().equals(campaign.getName())) {
				changeItem = changeItem + ",活动名称";
				changeBefore=changeBefore+","+object.getName();
				changeAfter=changeAfter+","+campaign.getName();
			}
			;
			if (object.getCost() != campaign.getCost()) {
				changeItem = changeItem + ",活动花费";
				changeBefore=changeBefore+","+object.getCost();
				changeAfter=changeAfter+","+campaign.getCost();
			}
			if (!object.getBegin_date().equals(campaign.getBegin_date())) {
				changeItem = changeItem + ",活动开始时间";
				changeBefore=changeBefore+","+times.format(object.getBegin_date());
				changeAfter=changeAfter+","+times.format(campaign.getBegin_date());
			}
			if (!object.getEnd_date().equals(campaign.getEnd_date())) {
				changeItem = changeItem + ",活动结束时间";
				changeBefore=changeBefore+","+times.format(object.getEnd_date());
				changeAfter=changeAfter+","+times.format(campaign.getEnd_date());
			}
			if (object.getMonitor_type() != campaign.getMonitor_type()) {
				changeItem = changeItem + ",分析类型";
				changeBefore=changeBefore+","+object.getMonitor_type();
				changeAfter=changeAfter+","+campaign.getMonitor_type();
			}
			if (object.getUnit_price() != campaign.getUnit_price()) {
				changeItem = changeItem + ",活动结算单价";
				changeBefore=changeBefore+","+object.getUnit_price();
				changeAfter=changeAfter+","+campaign.getUnit_price();
			}
			if (!object.getCost_type().equals(campaign.getCost_type())) {
				changeItem = changeItem + ",活动结算类型";
				changeBefore=changeBefore+","+object.getCost_type();
				changeAfter=changeAfter+","+campaign.getCost_type();
			}
			if (!object.getTarget_url().equals(campaign.getTarget_url())) {
				changeItem = changeItem + ",活动到达地址";
				changeBefore=changeBefore+","+object.getTarget_url();
				changeAfter=changeAfter+","+campaign.getTarget_url();
			}
			campaign.setLast_modified(new Date());
			campaignMapper.update(campaign);
		}
		//转化点
		if(inversionpointIsChange((List<Inversionpoint>) oldCampaignData[1],inversionpointIds)){
			campaignMapper
			.deleteCampaignInversionpoint(campaign.getId().toString());
			String after="";
			if(!"".equals(inversionpointIds)&&inversionpointIds!=null){
				String[] id_Array1 = inversionpointIds.split(",");
				for (String check_id : id_Array1) {
					long l = Long.parseLong(check_id.trim());
					campaignMapper.addCampaignInversionpoint(campaign.getId(), l);
					Inversionpoint inversionpoint=inversionpointMapper.findByIds(l+"");
					after=after+inversionpoint.getName()+" ";
				}
			}
			
			changeItem=changeItem+",转化点";
			String before="";
			for(Inversionpoint inversionpoint:(List<Inversionpoint>) oldCampaignData[1]){
				 before=before+inversionpoint.getName()+" ";
			}
			changeBefore=changeBefore+","+before;
			changeAfter=changeAfter+","+after;
			
		}
		//渠道
		if(channelIsChange((List<Channel>) oldCampaignData[2],channelIds)){
			campaignMapper.deleteCampaignChannel(campaign.getId().toString());
			String[] id_Array = channelIds.split(",");
			String after="";
			for (String check_id : id_Array) {
				long l = Long.parseLong(check_id.trim());
				campaignMapper.addCampaignChannel(campaign.getId(), l);
				Channel channel = channelMapper.findByIds(l+"");
				after=after+channel.getName()+" ";
			}
			String before="";
			for(Channel channel:(List<Channel>) oldCampaignData[2]){
				before=before+channel.getName()+" ";
			}
			changeItem=changeItem+",渠道";
			changeBefore=changeBefore+","+before;
			changeAfter=changeAfter+","+after;
		}
		//创意
		if(creativeIsChange((List<Creative>) oldCampaignData[3],creativeIds)){
			campaignMapper.deleteCampaignCreative(campaign.getId().toString());
			String after="";
			if (!"".equals(creativeIds) && creativeIds != null) {
				String[] id_Array2 = creativeIds.split(",");
				for (String check_id : id_Array2) {
					long l = Long.parseLong(check_id.trim());
					campaignMapper.addCampaignCreative(campaign.getId(), l);
					Creative creative= campaignMapper.findCreativeByIds(l+"");
					after=after+creative.getName()+" ";
				}}
			String before="";
			for(Creative creative:(List<Creative>) oldCampaignData[3]){
				before=before+creative.getName()+" ";
			}
			changeItem=changeItem+",创意";
			changeBefore=changeBefore+","+before;
			changeAfter=changeAfter+","+after;
		}
		// campaignMapper.deleteCreativeByCampaign(campaign.getId());
		String ip = ServletUtils.getRemoteAddr(request);
		
		String xid = ServletUtils.getCookie("XID",request);
		if(xid == null){
			xid = XidUtils.generateUserId();
			ServletUtils.setCookie("XID", xid,new ServletReqResp(request, response));
		}
		OperateLog operateLog = new OperateLog();
		operateLog.setCampaignId(campaign.getId());
		operateLog.setOperateTime(new Date());
		operateLog.setIP(ip);
		operateLog.setUserId(xid);
		operateLog.setOperateType("修改");
		operateLog.setChangeItem(changeItem);
		operateLog.setChangeBefore(changeBefore);
		operateLog.setChangeAfter(changeAfter);
		if(!"".equals(changeItem)){
			campaignMapper.savaOperateLog(operateLog);
			campaign.setLast_modified(new Date());
			campaignMapper.update(campaign);
		}
		
	}
	//创意
	private boolean creativeIsChange(List<Creative> object, String creativeIds) {
		if (object.size() > 0) {
			String oldCreativeIds = "";
			for (Creative creative : object) {
				oldCreativeIds = oldCreativeIds + creative.getId() + ",";
			}
			oldCreativeIds = oldCreativeIds.substring(0,
					oldCreativeIds.length() - 1);
			if (oldCreativeIds.equals(creativeIds)) {
				return false;
			}
		}
		if("".equals(creativeIds)||creativeIds==null){
			return true;
		}
		return true;
	}
//渠道
private boolean channelIsChange(List<Channel> object, String channelIds) {
	if (object.size() > 0) {
		String oldChannelIds = "";
		for (Channel channel : object) {
			oldChannelIds = oldChannelIds + channel.getId() + ",";
		}
		oldChannelIds = oldChannelIds.substring(0,
				oldChannelIds.length() - 1);
		if (oldChannelIds.equals(channelIds)) {
			return false;
		} else {
			return true;
		}
	}
	return true;
	}
//转化点
private boolean inversionpointIsChange(List<Inversionpoint> object, String inversionpointIds) {
	if (object.size() > 0) {
		String oldInversionpointIds = "";
		for (Inversionpoint inversionpoint : object) {
			oldInversionpointIds = oldInversionpointIds + inversionpoint.getId() + ",";
		}
		oldInversionpointIds = oldInversionpointIds.substring(0,
				oldInversionpointIds.length() - 1);
		if (oldInversionpointIds.equals(inversionpointIds)) {
			return false;
		} else {
			return true;
		}
	}
	return true;
	}

	//活动基本数据是否修改
	private boolean campaignIsChange(Campaign object, Campaign campaign,
			String changeItem, String changeBefore, String changeAfter) {
		if (!object.getName().equals(campaign.getName())) {
			changeItem = changeItem + ",活动名称";
			changeBefore=changeBefore+","+object.getName();
			changeAfter=changeAfter+","+campaign.getName();
		}
		;
		if (object.getCost() != campaign.getCost()) {
			changeItem = changeItem + ",活动花费";
			changeBefore=changeBefore+","+object.getCost();
			changeAfter=changeAfter+","+campaign.getCost();
		}
		if (!object.getBegin_date().equals(campaign.getBegin_date())) {
			changeItem = changeItem + ",活动开始时间";
			changeBefore=changeBefore+","+object.getBegin_date();
			changeAfter=changeAfter+","+campaign.getBegin_date();
		}
		if (!object.getEnd_date().equals(campaign.getEnd_date())) {
			changeItem = changeItem + ",活动结束时间";
			changeBefore=changeBefore+","+object.getEnd_date();
			changeAfter=changeAfter+","+campaign.getEnd_date();
		}
		if (object.getMonitor_type()!= campaign.getMonitor_type()) {
			changeItem = changeItem + ",分析类型";
			changeBefore=changeBefore+","+object.getMonitor_type();
			changeAfter=changeAfter+","+campaign.getMonitor_type();
		}
		if (object.getUnit_price() != campaign.getUnit_price()) {
			changeItem = changeItem + ",活动结算单价";
			changeBefore=changeBefore+","+object.getUnit_price();
			changeAfter=changeAfter+","+campaign.getUnit_price();
		}
		if (!object.getCost_type().equals(campaign.getCost_type())) {
			changeItem = changeItem + ",活动结算类型";
			changeBefore=changeBefore+","+object.getCost_type();
			changeAfter=changeAfter+","+campaign.getCost_type();
		}
		if (!object.getTarget_url().equals(campaign.getTarget_url())) {
			changeItem = changeItem + ",活动到达地址";
			changeBefore=changeBefore+","+object.getTarget_url();
			changeAfter=changeAfter+","+campaign.getTarget_url();
		}
		if (!"".equals(changeItem)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Campaign findById(Long id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			now = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Campaign campaign=campaignMapper.findById(id);
		if (campaign.getBegin_date().getTime() > now.getTime()) {
			campaign.setStatus("未开始");
		} else if (campaign.getEnd_date().getTime() >= now.getTime()) {
			campaign.setStatus("进行中");
		} else {
			campaign.setStatus("已结束");
		}
		String beginDate = sdf.format(campaign.getBegin_date());
		String endDate = sdf.format(campaign.getEnd_date());
		campaign.setActivity_cycle(beginDate + "至" + endDate);
		return campaign;
	}

	@Override
	public GridBean<Campaign> findAll(Long id, String status, int page,
			int rows, String name, String sidx, String sord) {
		String state;
		if (status.equals("未开始")) {
			state = "DATEDIFF(begin_date,now())>0";
		} else if (status.equals("进行中")) {
			state = "DATEDIFF(begin_date,now())<=0 and DATEDIFF(end_date,now())>=0";
		} else if (status.equals("已结束")) {
			state = "DATEDIFF(end_date,now())<0";
		} else {
			state = "1=1";
		}
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		GridBean<Campaign> gridBean = new GridBean<Campaign>();
		HashMap<String, String> map = new HashMap<String, String>();
		if ("activity_cycle".equals(sidx)) {
			sidx = "begin_date";
		}
		map.put("id", id.toString());
		map.put("state", state);
		map.put("sidx", sidx);
		map.put("sord", sord);
		map.put("name", name);
		map.put("start", start + "");
		map.put("rowpage", rowpage + "");
		int records = campaignMapper.getRecords(name, state, id);
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}

		List<Campaign> campaignList = campaignMapper.findAll(map);
//		List<Campaign> newcampaignList = new ArrayList<Campaign>();
		// 当前时间
		// long now = getNowDateShort().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			now = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (Campaign campaign : campaignList) {
			campaign.setTarget_url(ServletUtils.decode(campaign.getTarget_url()));
			if (campaign.getBegin_date().getTime() > now.getTime()) {
				campaign.setStatus("未开始");
			} else if (campaign.getEnd_date().getTime() >= now.getTime()) {
				campaign.setStatus("进行中");
			} else {
				campaign.setStatus("已结束");
			}
			String delete = "<a style='text-decoration:underline' href='javascript:deleteActivity("
					+ campaign.getId() + ")'>删除</a>";
			String TrackingCode = "<a style='text-decoration:underline' href='javascript:TrackingCode("
					+ campaign.getId() + ")'>活动代码</a>";
			String effect = "<a style='text-decoration:underline' href='javascript:effect(" + campaign.getId()
					+ ")'>效果</a>";
			String actionlog = "<a style='text-decoration:underline' href='javascript:actionlog(" + campaign.getId()
					+ ")'>查看</a>";
			// String edit =
			// "<a href='javascript:edit("+campaign.getId()+")'>   编辑</a>";
			campaign.setOperation(TrackingCode + "          " + delete);
			String beginDate = sdf.format(campaign.getBegin_date());
			String endDate = sdf.format(campaign.getEnd_date());
			campaign.setActivity_cycle(beginDate + "至" + endDate);
			campaign.setName("<a style='text-decoration:underline' href='javascript:edit(" + campaign.getId()
					+ ")'>"+campaign.getName() + "</a>");
			campaign.setOperateLog(actionlog);
			if(campaign.getMonitor_type()==0){
				campaign.setMonitorType("到访分析");
			}
			if(campaign.getMonitor_type()==1){
				campaign.setMonitorType("全程分析");
			}
//			newcampaignList.add(campaign);
		}

		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(campaignList);
		return gridBean;
	}

	public Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	@Override
	public List<Creative> findByCampaignId(Long id) {
		return campaignMapper.findByCampaignId(id);
	}

	@Override
	public Date findCreation(Long id) {

		return campaignMapper.findCreation(id);
	}

	@Override
	public Campaign findByName(String name, Long long1) {
		
		Campaign campaign= campaignMapper.findByName(name, long1);
		if(campaign!=null){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			now = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (campaign.getBegin_date().getTime() > now.getTime()) {
			campaign.setStatus("未开始");
		} else if (campaign.getEnd_date().getTime() >= now.getTime()) {
			campaign.setStatus("进行中");
		} else {
			campaign.setStatus("已结束");
		}
		String beginDate = sdf.format(campaign.getBegin_date());
		String endDate = sdf.format(campaign.getEnd_date());
		campaign.setActivity_cycle(beginDate + "至" + endDate);}
		return campaign;
	}

	@Override
	public Channel findChannelByName(String name, Long id) {
		return campaignMapper.findChannelByName(name, id);
	}

	@Override
	public Inversionpoint findPointByName(String name, Long id) {
		return campaignMapper.findPointByName(name, id);
	}

	@Override
	public List<Channel> findNoChannelList(Long id, Long id2) {
		return campaignMapper.findNoChannelList(id, id2);
	}

	@Override
	public boolean findCreativeByName(String filerealName) {
		return false;
	}

	@Override
	public Creative findCreativeByName(String filerealName, Long campaignId) {
		return campaignMapper.findCreativeByName(filerealName, campaignId);
	}

	@Override
	public void addCreative(Creative creative) {
		campaignMapper.addCreative(creative);
	}

	@Override
	public void deleteCreative(String id) {
		campaignMapper.deleteCampaignCreativeId(id);
		campaignMapper.deleteCreativeByCampaign(id);
	}

	@Override
	public String findCreativeById(String id) {
		return campaignMapper.findCreativeById(id);

	}

	@Override
	public List<Campaign> findByAccountAndName(String name, Long id) {
		return campaignMapper.findByAccountAndName(name, id);
	}

	@Override
	public List<Creative> findCreativeByCampaign(Long id) {
		return campaignMapper.findByCampaignId(id);
	}

	@Override
	public List<ReportBean> isCampaignData(String ids) {
		return campaignMapper.isCampaignData(ids);
	}

	@Override
	public void falseDelete(String ids) {
		campaignMapper.falseDelete(ids);
	}

	@Override
	public List<Creative> isCreativeDtata(String ids) {
		return campaignMapper.isCreativeDtata(ids);
	}

	@Override
	public GridBean<OperateLog> getOperateLogData(Long id, int page,
			int rows, String sidx, String sord) {
		GridBean<OperateLog> gridBean = new GridBean<OperateLog>();
		HashMap<String, String> map = new HashMap<String, String>();
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		map.put("id", id.toString());
		map.put("sidx", sidx);
		map.put("sord", sord);
		map.put("start", start + "");
		map.put("rowpage", rowpage + "");
		int records = campaignMapper.getOperateLogDataRecords(id);
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		List<OperateLog> operateLogList=  campaignMapper.getOperateLogData(map);
		
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(operateLogList);
		return gridBean;
	}

	public void setBaseData(String sqlChannel1) {
		campaignMapper.setBaseData(sqlChannel1);
		
	}

	public void setGeoData(String sqlbeijing) {
		campaignMapper.setGeoData(sqlbeijing);
	}

	public void setHourData(String sqla) {
		campaignMapper.setHourData(sqla);
	}

	@Override
	public GridBean<OperateLog> getOperate(Long id, int page, int rows,
			String sidx, String sord) {
		GridBean<OperateLog> gridBean = new GridBean<OperateLog>();
		HashMap<String, String> map = new HashMap<String, String>();
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		map.put("id", id.toString());
		map.put("sidx", sidx);
		map.put("sord", sord);
		map.put("start", start + "");
		map.put("rowpage", rowpage + "");
		OperateLog operateLog = campaignMapper.getOperate(id);
		if(!"".equals(operateLog.getChangeItem())&&operateLog.getChangeItem()!=null){
		List<OperateLog> operateLogList = new ArrayList<OperateLog>();
		String[] item=null;
		String[] before=null;
		String[] after=null;
		if(!"".equals(operateLog.getChangeItem())&&operateLog.getChangeItem()!=null){item =operateLog.getChangeItem().substring(1).split(",");}
		if(!"".equals(operateLog.getChangeBefore())&&operateLog.getChangeBefore()!=null&&!",".equals(operateLog.getChangeBefore())){before =operateLog.getChangeBefore().substring(1).split(",");}
		if(!"".equals(operateLog.getChangeAfter())&&operateLog.getChangeAfter()!=null){after =operateLog.getChangeAfter().substring(1).split(",");}
		for(int i=0;i<item.length;i++){
			OperateLog newOperateLog = new OperateLog();
			newOperateLog.setChangeItem(item[i]);
			if("分析类型".equals(newOperateLog.getChangeItem())){
				try {
					if(!"".equals(operateLog.getChangeBefore())&&operateLog.getChangeBefore()!=null&&!",".equals(operateLog.getChangeBefore())){newOperateLog.setChangeBefore(before[i].equals("0")?"到访分析":"全程分析");}
				} catch (Exception e) {
					newOperateLog.setChangeBefore("");
				}
				try {
					if(!"".equals(operateLog.getChangeAfter())&&operateLog.getChangeAfter()!=null){newOperateLog.setChangeAfter(after[i].equals("0")?"到访分析":"全程分析");}
				} catch (Exception e) {
					newOperateLog.setChangeAfter("");
				}}else{
			if("活动到达地址".equals(newOperateLog.getChangeItem())){
				try {
					if(!"".equals(operateLog.getChangeBefore())&&operateLog.getChangeBefore()!=null&&!",".equals(operateLog.getChangeBefore())){newOperateLog.setChangeBefore(ServletUtils.decode(before[i]));}
				} catch (Exception e) {
					newOperateLog.setChangeBefore("");
				}
				try {
					if(!"".equals(operateLog.getChangeAfter())&&operateLog.getChangeAfter()!=null){newOperateLog.setChangeAfter(ServletUtils.decode(after[i]));}
				} catch (Exception e) {
					newOperateLog.setChangeAfter("");
				}
			}else{
				try {
					if(!"".equals(operateLog.getChangeBefore())&&operateLog.getChangeBefore()!=null&&!",".equals(operateLog.getChangeBefore())){newOperateLog.setChangeBefore(before[i]);}
				} catch (Exception e) {
					newOperateLog.setChangeBefore("");
				}
				try {
					if(!"".equals(operateLog.getChangeAfter())&&operateLog.getChangeAfter()!=null){newOperateLog.setChangeAfter(after[i]);}
				} catch (Exception e) {
					newOperateLog.setChangeAfter("");
				}
			}}
			
			operateLogList.add(newOperateLog);
		}
		int records =operateLogList.size();
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(operateLogList);}
		return gridBean;
	}

	@Override
	public String findCreativeNameById(String id) {
		
		return campaignMapper.findCreativeNameById(id);
	}

	@Override
	public List<Campaign> getCampaignListByconversion(Long conversionId) {
		return campaignMapper.getCampaignListByconversion(conversionId);
	}

	@Override
	public void byChannelDownload(Account account,List<Creative> creativeList,Long campaignId, int channelId, HttpServletResponse response) {
		SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd");
		Campaign campaign=campaignMapper.findById(campaignId);
		campaign.setTarget_url(ServletUtils.decode(campaign.getTarget_url()));
		Channel channel = channelMapper.findByIds(channelId+"");
		String name=campaign.getName()+"-"+channel.getName()+"-"+times.format(new Date())+".xls";
		OutputStream os;
		try {
			os = response.getOutputStream();
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8")); 
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet(channel.getName(), 0);
			CellView cellView = new CellView();  
			cellView.setAutosize(true); //设置自动大小  
			String showCode="";
			String clickCode="";
			String reachCode = null;
			if(creativeList.size()==0){
				sheet.setColumnView(0, cellView);//根据内容自动设置列宽
				sheet.setColumnView(1, cellView);//根据内容自动设置列宽
				if(campaign.getMonitor_type()==0){
					sheet.addCell(new Label(0, 0, "到达代码"));
					showCode = "";
					if(campaign.getTarget_url().indexOf("?")==-1){
						reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
								+ channelId;
					}else{
						reachCode = campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
								+ channelId;
					}
					sheet.addCell(new Label(0, 1, reachCode));
				}else{
					sheet.addCell(new Label(0, 0, "展示代码"));
					sheet.addCell(new Label(1, 0, "点击代码"));
					sheet.addCell(new Label(2, 0, "到达代码"));
				showCode = "http://"+account.getDomain()+"/i?c=" + campaignId + "&h="
						+ channelId;
				clickCode = "http://"+account.getDomain()+"/c?c=" + campaignId + "&h="
						+ channelId;
				if(campaign.getTarget_url().indexOf("?")==-1){
					reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
							+ channelId;
				}else{
					reachCode = campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
							+ channelId;
				}
				sheet.addCell(new Label(0, 1, showCode));
				sheet.addCell(new Label(1, 1,clickCode));
				sheet.addCell(new Label(2, 1,reachCode));
				}
				
			}else{
				sheet.setColumnView(1, cellView);//根据内容自动设置列宽
				int i =1;
				for(Creative creative:creativeList){
					if(campaign.getMonitor_type()==0){
						sheet.addCell(new Label(0, 0, "创意"));
						sheet.addCell(new Label(1, 0, "到达代码"));
						showCode = "";
						if(campaign.getTarget_url().indexOf("?")==-1){
							reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
									+ channelId + "&xyx_e=" + creative.getId();
						}else{
							reachCode = campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
									+ channelId + "&xyx_e=" + creative.getId();
						}
						sheet.addCell(new Label(0, i, creative.getName()));
						sheet.addCell(new Label(1, i, reachCode));
						i++;
					}else{
						sheet.addCell(new Label(0, 0, "创意"));
						sheet.addCell(new Label(1, 0, "展示代码"));
						sheet.addCell(new Label(2, 0, "点击代码"));
						sheet.addCell(new Label(3, 0, "到达代码"));
					showCode = "http://"+account.getDomain()+"/i?c=" + campaignId + "&h="
							+ channelId + "&e=" + creative.getId();
					clickCode = "http://"+account.getDomain()+"/c?c=" + campaignId + "&h="
							+ channelId + "&e=" + creative.getId();
					if(campaign.getTarget_url().indexOf("?")==-1){
						reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
								+ channelId + "&xyx_e=" + creative.getId();
					}else{
						reachCode = campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
								+ channelId + "&xyx_e=" + creative.getId();
					}
					sheet.addCell(new Label(0, i, creative.getName()));
					sheet.addCell(new Label(1, i, showCode));
					sheet.addCell(new Label(2, i, clickCode));
					sheet.addCell(new Label(3, i, reachCode));
					i++;
					}
					
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
		}// 取得输出流
	}
	
	@Override
	public List<Creative> findByCampaignIdNoPath(Long id) {
		return campaignMapper.findByCampaignIdNoPath(id);
	}

	@Override
	public List<Creative> findCreativeNoPath(Long id) {
		return campaignMapper.findCreativeNoPath(id);
	}

	@Override
	public List<Campaign> findByAccount(Long id) {
		return campaignMapper.findByAccount(id);
	}

	@Override
	public int findCampaignType(long id) {
		return campaignMapper.findCampaignType(id);
	}

}
