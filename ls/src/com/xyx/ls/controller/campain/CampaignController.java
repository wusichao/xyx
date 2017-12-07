package com.xyx.ls.controller.campain;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xyx.ls.bean.GridBean;
import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.model.campaign.Inversionpoint;
import com.xyx.ls.model.campaign.OperateLog;
import com.xyx.ls.service.campaign.CampaignService;
import com.xyx.ls.service.campaign.ChannelService;
import com.xyx.ls.service.campaign.InversionpointService;
import com.xyx.ls.util.ExportExcel;
import com.xyx.ls.util.ParameterCheckUtil;
import com.xyx.ls.util.Write2Response;
import com.xyx.x.utils.ServletUtils;

@Controller
public class CampaignController {
	@Resource
	private CampaignService campaignService;
	@Resource
	private InversionpointService inversionpointService;
	@Resource
	private ChannelService channelService;
	@RequestMapping("getcodeUI")
	public String getcodeUI(Long id, HttpServletRequest request) {
		request.getSession().setAttribute("wscid", id);
		return "getcode";
	}

	@RequestMapping("getcampaignid")
	public void getcampaignid(HttpServletRequest request,
			HttpServletResponse response) {
		Long iiid = (Long) request.getSession().getAttribute("wscid");
		if (iiid != null) {
			Write2Response.write2Res(response, iiid.toString());
		}
	}
//新增活动准备
	@RequestMapping("addCampaignDataUI")
	public void addCampaignDataUI(HttpServletRequest request,HttpServletResponse response){
		request.getSession().removeAttribute("creativeNamesss");
		request.getSession().removeAttribute("creativeList");
		Write2Response.write2Res(response, "");
	}
	/**
	 * 
	 * @param id
	 * @param request
	 * @param response
	 */
// 活动回显数据
	@RequestMapping("addCampaignData")
	public void addCampaignData(Long id, HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().removeAttribute("creativeNamesss");
		Campaign campaign = campaignService.findById(id);
		campaign.setTarget_url(ServletUtils.decode(campaign.getTarget_url()));
		List<Inversionpoint> inversionpointList = inversionpointService
				.findByCampaignId(id);
		List<Channel> channelList = channelService.findByCanpaignId(id);
		List<Creative> creativeList = campaignService.findByCampaignId(id);
		List<Creative> creativeListwsc = campaignService.findByCampaignId(id);
//		List<Creative> creativeListHasPath = campaignService.findByCampaignIdNoPath(id);
//		List<Creative> creativeListNoPath = campaignService.findCreativeNoPath(id);
		//保存旧数据验证重名
		request.getSession().setAttribute("creativeList", creativeList);
		//修改前保存旧创意数据
		request.getSession().setAttribute("creativeListwsc", creativeListwsc);
		Account account = (Account) request.getSession()
				.getAttribute("account");
		List<Channel> noChannelList = campaignService.findNoChannelList(id,
				account.getId());
		Object[] obj = { campaign, inversionpointList, channelList,
				creativeListwsc, noChannelList };
		JSON jsonArray = (JSON) JSON.toJSON(obj);
		Write2Response.write2Res(response, jsonArray.toString());
	}

// 监测代码数据
	@RequestMapping("trackingCodeData")
	public void trackingCodeData(Long campaignId, Long channelId,
			Long creativeId, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession().getAttribute("account");
		Campaign campaign = campaignService.findById(campaignId);
		campaign.setTarget_url(ServletUtils.decode(campaign.getTarget_url()));
		String showCode;
		String clickCode = "";
		String reachCode = "";
		if (creativeId != null) {
			if(campaign.getMonitor_type()==0){
				showCode = "";
				if(campaign.getTarget_url().indexOf("?")==-1){
					reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
							+ channelId + "&xyx_e=" + creativeId;
				}else{
					reachCode =campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
							+ channelId + "&xyx_e=" + creativeId;
				}
				
			}else{
				showCode = "http://"+account.getDomain()+"/i?c=" + campaignId + "&h="
						+ channelId + "&e=" + creativeId;
				clickCode = "http://"+account.getDomain()+"/c?c=" + campaignId + "&h="
						+ channelId + "&e=" + creativeId;
				if(campaign.getTarget_url().indexOf("?")==-1){
					reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
							+ channelId + "&xyx_e=" + creativeId;
				}else{
					reachCode =campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
							+ channelId + "&xyx_e=" + creativeId;
				}
			}
			
		} else {
			if(campaign.getMonitor_type()==0){
				showCode = "";
				if(campaign.getTarget_url().indexOf("?")==-1){
					reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
							+ channelId;
				}else{
					reachCode = campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
							+ channelId;
				}
				
			}else{
				showCode = "http://"+account.getDomain()+"/i?c=" + campaignId + "&h="
						+ channelId;
				clickCode = "http://"+account.getDomain()+"/c?c=" + campaignId + "&h="
						+ channelId;
				if(campaign.getTarget_url().indexOf("?")==-1){
					reachCode = campaign.getTarget_url()+"?xyx_c=" + campaignId + "&xyx_h="
							+ channelId;
				}else{
					reachCode =campaign.getTarget_url()+"&xyx_c=" + campaignId + "&xyx_h="
							+ channelId;
				}
			}
			
		}
		String[] code = { showCode, clickCode ,reachCode};
		JSON jsonArray = (JSON) JSON.toJSON(code);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	// 下载监测代码
	@RequestMapping("excelDomnload")
	public void excelDomnload(int i, Long campaignId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Account account = (Account) request.getSession().getAttribute("account");
		try {
			Campaign campaign = campaignService.findById(campaignId);
			campaign.setTarget_url(ServletUtils.decode(campaign.getTarget_url()));
			List<Channel> channelList = channelService
					.findByCanpaignId(campaignId);
			List<Creative> creativeList = campaignService
					.findByCampaignId(campaignId);
			List<String> iCode = new ArrayList<String>();
			List<String> cCode = new ArrayList<String>();
			if (channelList.size() > 0 && creativeList.size() == 0) {
				for (Channel channel : channelList) {
					iCode.add(SSplice(campaign,account,1, campaignId, channel.getId()));
					cCode.add(SSplice(campaign,account,2, campaignId, channel.getId()));
				}

			}
			if (channelList.size() > 0 && creativeList.size() > 0) {
				for (Channel channel : channelList) {
					for (Creative creative : creativeList) {
						iCode.add(Splice( campaign,account,1, campaignId, channel.getId(),
								creative.getId()));
						cCode.add(Splice(campaign, account,2, campaignId, channel.getId(),
								creative.getId()));
					}
				}
			}

			ExportExcel.exportExcel(i, campaign.getName().toString(),
					channelList, creativeList, iCode, cCode, response);
		} catch (Exception e) {
		}

	}

	private String SSplice(Campaign campaign,Account account,int i, Long campaignId, Long channelId) {
		String showCode;
		if (i == 1) {
			if(campaign.getMonitor_type()==0){
				showCode = "http://"+campaign.getTarget_url()+"/i?c=" + campaignId + "&h="
						+ channelId;
			}else{
				showCode = "http://"+account.getDomain()+"/i?c=" + campaignId + "&h="
						+ channelId;
			}
			
		} else {
			if(campaign.getMonitor_type()==0){
				showCode = "http://"+campaign.getTarget_url()+"/c?c=" + campaignId + "&h="
						+ channelId;
			}else{
				showCode = "http://"+account.getDomain()+"/c?c=" + campaignId + "&h="
						+ channelId;
			}
			
		}

		return showCode;
	}

	// 拼接监测代码
	private String Splice(Campaign campaign,Account account,int i, Long campaignId, Long channelId,
			Long creativeId) {
		String showCode;
		if (i == 1) {
			showCode = "http://"+account.getDomain()+"/i?c=" + campaignId + "&h="
					+ channelId + "&e=" + creativeId;
		} else {
			showCode = "http://"+account.getDomain()+"/c?c=" + campaignId + "&h="
					+ channelId + "&e=" + creativeId;
		}

		return showCode;
	}

	// 添加活动
	@RequestMapping("addCampaign")
	public void addCampaign(Campaign campaign, String creativeString,
			 String channelIds, String inversionpointIds,
			HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("creativeNamesss");
		request.getSession().removeAttribute("creativeList");
		if (checkData(campaign, request)) {
			// 获取当前用户
			Account account = (Account) request.getSession().getAttribute(
					"account");
			campaign.setAccount_id(account.getId());
			campaign.setTarget_url(ServletUtils.encode(campaign.getTarget_url()));
			campaignService.add(campaign, creativeString, channelIds,
					inversionpointIds, request, response);
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}
		// }

	}

	private boolean validata(List<Creative> creativeList, Long id) {
		if (creativeList.isEmpty()) {
			return true;
		}
		for (Creative creative : creativeList) {
			if (validataCrative(creative.getName(), id)) {
				return true;
			}
		}
		return false;

	}

	private boolean checkData(Campaign campaign, HttpServletRequest request) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		if (!ParameterCheckUtil.checkLength(campaign.getName(), 60)) {
			return false;
		}
		if (!ParameterCheckUtil.checkLength(campaign.getTarget_url(), 100)) {
			return false;
		}
		// if (campaignService.findByName(campaign.getName(),account.getId())!=
		// null) {
		// return false;
		// }if(campaignService.findChannelByName(campaign.getName(),account.getId())!=
		// null){
		// return false;
		// }
		// if(campaignService.findPointByName(campaign.getName(),account.getId())!=
		// null){
		// return false;
		// }
		// if(!campaignService.findCreativeByName(filerealName,campaignId)==null){
		// return false;
		// }
		return true;
	}

	// 真删删除活动
	@RequestMapping("deleteCampaign")
	public void deleteCampaign(String ids, HttpServletRequest request,
			HttpServletResponse response) {
		if (!"".equals(ids) && ids != null) {
			campaignService.delete(ids, request, response);
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}
	}
//判断活动是否产生数据
	@RequestMapping("isCampaignData")
	public void isCampaignData(String ids,HttpServletRequest request, HttpServletResponse response){
		if (campaignService.isCampaignData(ids).size()>0) {
			
			Write2Response.write2Res(response, "false");
		} else {
			Write2Response.write2Res(response, "true");
		}
	}
	//活动假删
	@RequestMapping("falseDeleteCampaign")
	public void falseDeleteCampaign(String ids, HttpServletRequest request,
			HttpServletResponse response) {
		if (!"".equals(ids) && ids != null) {
			campaignService.falseDelete(ids);
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}
	}
	
	// 更新活动
	@RequestMapping("updateCampaign")
	public void updateCampaign(Campaign campaign, String creativeString,
			 String channelIds, String inversionpointIds,
			HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("creativeNamesss");
		if (checkData(campaign, request)) {
			campaign.setTarget_url(ServletUtils.encode(campaign.getTarget_url()));
			//修改前数据
			Campaign oldCampaign = campaignService.findById(campaign.getId());
			List<Inversionpoint> oldInversionpointList = inversionpointService
					.findByCampaignId(campaign.getId());
			List<Channel> oldChannelList = channelService.findByCanpaignId(campaign.getId());
			@SuppressWarnings("unchecked")
			List<Creative> oldCreativeList = (List<Creative>) request.getSession().getAttribute("creativeListwsc");
//			List<Creative> oldCreativeList = campaignService.findByCampaignId(campaign.getId());
			Object[] oldCampaignData={oldCampaign,oldInversionpointList,oldChannelList,oldCreativeList};
			campaign.setCreation(campaignService.findCreation(campaign.getId()));
			campaignService.update(campaign, creativeString, channelIds,
					inversionpointIds,oldCampaignData, request, response);
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}
	}
//删除创意
	@RequestMapping("deleteCreativeInC")
	public void deleteCreativeInC(String id,HttpServletRequest request,HttpServletResponse response){
		Map<String, String> map=null;
	List<Creative> oldCreativeList=null;
	if (campaignService.isCreativeDtata(id).size()>0) {
		
		Write2Response.write2Res(response, "false");
	} else {
		String path=campaignService.findCreativeById(id);
		String creativeName=campaignService.findCreativeNameById(id);
		if(creativeName!=null){
		 oldCreativeList = (List<Creative>) request.getSession().getAttribute("creativeList");
		if(oldCreativeList!=null&&oldCreativeList.size()>1){
			for(Creative creative:oldCreativeList){
			if(creativeName.equals(creative.getName())){
				oldCreativeList.remove(creative);
				break;
			}
		}}
		if(oldCreativeList!=null&&oldCreativeList.size()==1){
			if(oldCreativeList.get(0).getName().equals(creativeName)){
				oldCreativeList.removeAll(oldCreativeList);
			}
		}
		 map=(Map<String, String>) request.getSession().getAttribute("creativeNamesss");
		if(map!=null&&map.size()>0){
			map.remove(creativeName);
		}
		}
		request.getSession().setAttribute("creativeNamesss",map);
		request.getSession().setAttribute("creativeList",oldCreativeList);
		campaignService.deleteCreative(id);
		if(path.equals("null")){
			Write2Response.write2Res(response, "true");
		}else{
			path=path.substring(3);
			String serverpath=request.getSession().getServletContext().getRealPath("/");
			String realpath=serverpath+path;
			DeleteFolder(realpath);
			Write2Response.write2Res(response, "true");
		}
	}}
	//删除创意图片本地
	public boolean DeleteFolder(String sPath) {  
	   File file = new File(sPath);  
	    // 判断目录或文件是否存在  
	   if (file.isFile() && file.exists()) {  
	        file.delete();  
	        return true;  
	    }
	return false;  
	    }  
	
	//判断创意是否产生报表数据
	@RequestMapping("isCreativeDtata")
	public void isCreativeDtata(String ids,HttpServletRequest request, HttpServletResponse response){
		if (campaignService.isCreativeDtata(ids).size()>0) {
			Write2Response.write2Res(response, "false");
		} else {
			Write2Response.write2Res(response, "true");
		}
		
	}
	
	// 展示活动列表
	@RequestMapping("finAllCampaign")
	public void finAllCampaign(String status, int page, int rows, String sidx,
			String sord, String name, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取当前用户
		Account account = (Account) request.getSession()
				.getAttribute("account");
		GridBean<Campaign> gridBean = campaignService.findAll(account.getId(),
				status, page, rows, name, sidx, sord);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	@RequestMapping("findCreativeByCampaign")
	public void findCreativeByCampaign(Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Long iid = (Long) request.getSession().getAttribute("wscid");
		List<Creative> creativeList = campaignService.findByCampaignId(iid);
		JSON jsonArray = (JSON) JSON.toJSON(creativeList);
		Write2Response.write2Res(response, jsonArray.toString());
		// request.getSession().removeAttribute("wscid");
	}

	@RequestMapping("validateCampaign")
	public void validateCampaign(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		if (campaignService.findByName(name, account.getId()) != null) {
			Write2Response.write2Res(response, "false");
		} else {
			Write2Response.write2Res(response, "true");
		}

	}

	@RequestMapping("validateChannel")
	public void validateChannel(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		if (campaignService.findChannelByName(name, account.getId()) != null) {
			Write2Response.write2Res(response, "false");
		} else {
			Write2Response.write2Res(response, "true");
		}

	}

	@RequestMapping("validateInversionpoint")
	public void validateInversionpoint(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		if (campaignService.findPointByName(name, account.getId()) != null) {
			Write2Response.write2Res(response, "false");
		}
		Write2Response.write2Res(response, "true");
	}

	public boolean validataCrative(String filerealName, Long campaignId) {
		if (campaignService.findCreativeByName(filerealName, campaignId) != null) {
			return false;
		}
		return true;
	}
	//获取活动操作日志
	@RequestMapping("getOperateLogData")
	public void getOperateLogData(Long id,int page,int rows,String sidx,String sord,HttpServletRequest request,HttpServletResponse response){
		GridBean<OperateLog > gridBean = campaignService.getOperateLogData(id,page,rows,"operate_time",sord);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//获取操作详细
	@RequestMapping("getOperate")
	public void getOperate(Long id,int page,int rows,String sidx,String sord,HttpServletRequest request,HttpServletResponse response){
		GridBean<OperateLog > gridBean = campaignService.getOperate(id,page,rows,"operate_time",sord);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//根据转化点找活动列表
	@RequestMapping("getCampaignListByconversion")
	public void getCampaignListByconversion(Long conversionId ,HttpServletRequest request,HttpServletResponse response){
		List<Campaign> list = campaignService.getCampaignListByconversion(conversionId);
		JSON jsonArray = (JSON) JSON.toJSON(list);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//获取客户网站
	@RequestMapping("getWebSite")
	public void getWebSite(String target_url ,HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
			Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk)",Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(target_url);
			Matcher matcher2 = p.matcher(account.getWeb_site());
			String a="";
			String c="";
			while (matcher.find()) {
				System.out.println(matcher.group());
				a=matcher.group();
			}
			while (matcher2.find()) {
				System.out.println(matcher2.group());
				c=matcher2.group();
			}
			
		boolean b=a.equals(c);
		if(b){
			Write2Response.write2Res(response, "true");
		}else{
			Write2Response.write2Res(response, "false");
		}
	}
	@RequestMapping("byChannelDownload")
	public void byChannelDownload(int channelId,HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		Long iid = (Long) request.getSession().getAttribute("wscid");
		List<Creative> creativeList = campaignService.findByCampaignId(iid);
		campaignService.byChannelDownload(account,creativeList,iid,channelId,response);
	}
	@RequestMapping("findCampaignType")
	public void findCampaignType(long campaignId,HttpServletRequest request,HttpServletResponse response){
		Integer type =campaignService.findCampaignType(campaignId);
		Write2Response.write2Res(response,type.toString() );
	}
	
}
