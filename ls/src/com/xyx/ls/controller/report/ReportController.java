package com.xyx.ls.controller.report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xyx.ls.bean.GridBean;
import com.xyx.ls.bean.ReportAnalyzeBean;
import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.bean.RptImpClick;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.service.campaign.CampaignService;
import com.xyx.ls.service.campaign.ChannelService;
import com.xyx.ls.service.report.ReportService;
import com.xyx.ls.util.SortListUtil;
import com.xyx.ls.util.Write2Response;

@Controller
public class ReportController {
	@Resource
	private ReportService reportService;
	@Resource
	private CampaignService campaignService;
	@Resource
	private ChannelService channelService;

	// 通过id查找活动
	@RequestMapping("getCampaignById")
	public void getCampaignById(Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Campaign campaign = campaignService.findById(id);
		JSON jsonArray = (JSON) JSON.toJSON(campaign);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	// 活动列表
	@RequestMapping("getCampaignList")
	public void getCampaignList(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		if(name==null){name="";}
		List<Campaign> campaignList = campaignService.findByAccountAndName(
				name, account.getId());
		JSON jsonArray = (JSON) JSON.toJSON(campaignList);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	@RequestMapping("getCampaignListByAccount")
	public void getCampaignListByAccount(HttpServletRequest request,
			HttpServletResponse response){
		Account account = (Account) request.getSession()
				.getAttribute("account");
		List<Campaign> campaignList = campaignService.findByAccount(
				 account.getId());
		JSON jsonArray = (JSON) JSON.toJSON(campaignList);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	@RequestMapping("campaignFindByName")
	public void campaignFindByName(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign = campaignService.findByName(name, account.getId());
		JSON jsonArray = (JSON) JSON.toJSON(campaign);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	// 根据活动查找渠道
	@RequestMapping("getChannelByCampaign")
	public void getChannelByCampaign(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign = campaignService.findByName(name, account.getId());
		if (campaign != null) {
			List<Channel> channelList = channelService
					.findByCanpaignId(campaign.getId());
			JSON jsonArray = (JSON) JSON.toJSON(channelList);
			Write2Response.write2Res(response, jsonArray.toString());
		}

	}

	// 根据活动查找创意
	@RequestMapping("getCreativeByCampaign")
	public void getCreativeByCampaign(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign = campaignService.findByName(name, account.getId());
		if (campaign != null) {
			List<Creative> creativeList = campaignService
					.findCreativeByCampaign(campaign.getId());
			JSON jsonArray = (JSON) JSON.toJSON(creativeList);
			Write2Response.write2Res(response, jsonArray.toString());
		}
	}

	// 根据条件获取数据
	@RequestMapping("getReportData")
	public void getReportData(String name, String channelIds,
			String creativeIds, Date startDate, Date endDate,
			HttpServletRequest request, HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign ;
		if("".equals(name)||name==null){
			campaign= new Campaign();
			campaign.setId(0L);
		}else if("all".equals(name)){
			campaign= new Campaign();
			campaign.setId(-1L);
		}else{
			campaign = campaignService.findByName(name, account.getId());
		}
		ReportBean reportBean = null;
		List<ReportBean> reportList = new ArrayList<ReportBean>();
		if (campaign != null) {
			reportBean = reportService.findConvertionData(account.getId(),campaign.getId(),
					channelIds, creativeIds, startDate, endDate,
					campaign.getCost_type(), campaign.getUnit_price());
			reportList = reportService.findExportExcel(account.getId(),campaign.getId(),
					channelIds, creativeIds, campaign.getCost_type(),
					campaign.getUnit_price(), startDate, endDate);
			SortListUtil.sortList(reportList, "day", "asc");
			Object[] obj = { campaign, reportBean, reportList };
			JSON jsonArray = (JSON) JSON.toJSON(obj);
			Write2Response.write2Res(response, jsonArray.toString());
		} else {
			Object[] obj = { campaign, reportBean, reportList };
			JSON jsonArray = (JSON) JSON.toJSON(obj);
			Write2Response.write2Res(response, jsonArray.toString());
		}
	}

	@RequestMapping("getReportGridBean")
	public void getReportGridBean(String Dimension, String level, String name,
			String channelIds, String creativeIds, Date startDate,
			Date endDate, int page, int rows, String sidx, String sord,
			HttpServletRequest request, HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign ;
		if("".equals(name)||name==null){
			campaign= new Campaign();
			campaign.setId(0L);
		}
		else if("all".equals(name)){
			campaign= new Campaign();
			campaign.setId(-1L);
		}else{
			 campaign = campaignService.findByName(name, account.getId());
		}
		GridBean<ReportBean> reportGrid = new GridBean<ReportBean>();
		if (campaign != null) {
			reportGrid = reportService.findReportGrid(account.getId(),campaign.getId(),
					channelIds, creativeIds, startDate, endDate, page, rows,
					sidx, sord, campaign.getCost_type(),
					campaign.getUnit_price());
			JSON jsonArray = (JSON) JSON.toJSON(reportGrid);
			Write2Response.write2Res(response, jsonArray.toString());
		} else {
			JSON jsonArray = (JSON) JSON.toJSON(reportGrid);
			Write2Response.write2Res(response, jsonArray.toString());
		}
	}

	// 维度排名数据
	@RequestMapping("getDimensionRank")
	public void getDimensionRank(String Dimension, String name, String level,
			Date startDate, Date endDate, Integer page, Integer rows,
			String sidx, String sord, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign ;
		if("".equals(name)||name==null){
			campaign= new Campaign();
			campaign.setId(0L);
		}else{
			 campaign = campaignService.findByName(name, account.getId());
		}
		List<ReportBean> reportList = new ArrayList<ReportBean>();
		ReportBean bean= new ReportBean();
		if (campaign != null) {
			reportList = reportService.findDimensionRank(account.getId(),Dimension,
					campaign.getId(), campaign.getCost_type(),
					campaign.getUnit_price(), level, startDate, endDate,
					request);
			bean = reportService.findDimension(account.getId(),Dimension,
					campaign.getId(), campaign.getCost_type(),
					campaign.getUnit_price(), level, startDate, endDate,
					request);
			try {
				bean.setDimension("合计");
				double sum =0;
				for(ReportBean beans:reportList){
					sum= sum+beans.getCost();
				}
				bean.setCost(Double.parseDouble(new DecimalFormat("#.00").format(sum)));
			} catch (Exception e) {
			}
			
			if (!"".equals(sidx) && sidx != null) {
				if("dimension".equals(sidx)){
					sidx="hour";
				}
				SortListUtil.sortList(reportList, sidx, sord);
				int start = (page - 1) * rows;
				int rowpage = rows * page;
				if (rowpage > reportList.size()) {
					rowpage = reportList.size();
				}
				GridBean<ReportBean> reportGrid = reportService.jointGrid(page,
						rows, sidx, sord, reportList.size(),
						reportList.subList(start, rowpage));
				reportGrid.setUserdata(bean);
				JSON jsonArray = (JSON) JSON.toJSON(reportGrid);
				Write2Response.write2Res(response, jsonArray.toString());
			} else {
				JSON jsonArray = (JSON) JSON.toJSON(reportList);
				Write2Response.write2Res(response, jsonArray.toString());
			}
		} else {
			JSON jsonArray = (JSON) JSON.toJSON(reportList);
			Write2Response.write2Res(response, jsonArray.toString());
		}
	}

	// 报表数据导出
	@RequestMapping("exportExcel")
	public void exportExcel(String name, String channelIds, String creativeIds,
			Date startDate, Date endDate, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign ;
		if("".equals(name)||name==null){
			campaign= new Campaign();
			campaign.setId(0L);
			campaign.setName("自然流量");
		}else if("all".equals(name)){
			campaign= new Campaign();
			campaign.setName("所有流量");
			campaign.setId(-1L);
		}else{
			campaign = campaignService.findByName(name, account.getId());
		}
		List<ReportBean> reportList = new ArrayList<ReportBean>();
		if (campaign != null) {
			reportList = reportService.findExportExcel(account.getId(),campaign.getId(),
					channelIds, creativeIds, campaign.getCost_type(),
					campaign.getUnit_price(), startDate, endDate);
			reportService.exportExcel(campaign.getMonitor_type(),campaign.getName(), startDate, endDate,
					"日期", reportList, response);
		} else {
			reportService.exportExcel(null,"", startDate, endDate, "日期", reportList,
					response);
		}
	}

	// 趋势线性图
	@RequestMapping("linearData")
	public void linearData(String name, String channelIds, String creativeIds,
			Date startDate, Date endDate, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign ;
		if("".equals(name)||name==null){
			campaign= new Campaign();
			campaign.setId(0L);
		}else{
			 campaign = campaignService.findByName(name, account.getId());
		}
		List<ReportBean> reportList = new ArrayList<ReportBean>();
		if (campaign != null) {
			reportList = reportService.findExportExcel(account.getId(),campaign.getId(),
					channelIds, creativeIds, campaign.getCost_type(),
					campaign.getUnit_price(), startDate, endDate);
			SortListUtil.sortList(reportList, "day", "desc");
			JSON jsonArray = (JSON) JSON.toJSON(reportList);
			Write2Response.write2Res(response, jsonArray.toString());
		} else {
			JSON jsonArray = (JSON) JSON.toJSON(reportList);
			Write2Response.write2Res(response, jsonArray.toString());
		}
	}

	// 维度排名导出
	@RequestMapping("rankExport")
	public void rankExport(String Dimension, String name, Date startDate,
			Date endDate, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign ;
		if("".equals(name)||name==null){
			campaign= new Campaign();
			campaign.setId(0L);
			campaign.setName("自然流量");
		}else{
			 campaign = campaignService.findByName(name, account.getId());
		}
		List<ReportBean> reportList = new ArrayList<ReportBean>();
		if (campaign != null) {
			String level = null;
			if ("geo".equals(Dimension)) {
				level = "city";
			}
			reportList = reportService.findDimensionRank(account.getId(),Dimension,
					campaign.getId(), campaign.getCost_type(),
					campaign.getUnit_price(), level, startDate, endDate,
					request);
			SortListUtil.sortList(reportList, "cost", "desc");
			reportService.exportExcel(campaign.getMonitor_type(),campaign.getName(), startDate, endDate,
					Dimension, reportList, response);
		} else {
			reportService.exportExcel(null,"", startDate, endDate, Dimension,
					reportList, response);
		}
	}

	// 点击分析
	@RequestMapping("getClick_Log")
	public void getClick_Log(Long who,Integer clickNum ,String name, Long channelId, Date date, int startHour,
			int endHour, Integer page, Integer rows, String sidx, String sord,
			HttpServletRequest request, HttpServletResponse response) {
		if(!"".equals(name)&&name!=null&&channelId!=null){
//		if("clickTime".equals(sidx)){
//			sidx="req_time";
//			}
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Campaign campaign = campaignService.findByName(name, account.getId());
		GridBean<ReportAnalyzeBean> gridBean = reportService.getClick_Log(who,clickNum,campaign.getId(),channelId, date,
				startHour, endHour, page,rows,sidx,sord,request,response);
		if(gridBean.getRecords()==-1){Write2Response.write2Res(response,"false");}
		else{
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());}
		}
	}
	// 转化分析
	@RequestMapping("getConversion_Log")
	public void getConversion_Log(Integer gap,Long conversionId,Long campaignId, Long channelId, Date date, int startHour,
			int endHour, Integer page, Integer rows, String sidx, String sord,
			HttpServletRequest request, HttpServletResponse response){
//		sidx="req_time";
		Account account = (Account) request.getSession()
				.getAttribute("account");
		GridBean<ReportAnalyzeBean> gridBean = reportService.getConversion_Log(account.getId(),gap,conversionId,campaignId,channelId, date,
				startHour, endHour, page,rows,sidx,sord,request);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//点击分析导出
	@RequestMapping("clickExport")
	public void clickExport(String name, Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		if(!"".equals(name)&&name!=null&&channelId!=null){
			Account account = (Account) request.getSession()
					.getAttribute("account");
			Campaign campaign = campaignService.findByName(name, account.getId());
			reportService.clickExport(campaign.getId(),channelId, date,
				startHour, endHour,request,response);}
	}
	//转化导出
	@RequestMapping("conversionExport")
	public void conversionExport(Long conversionId,Long campaignId, Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		Account account = (Account) request.getSession()
				.getAttribute("account");
		reportService.conversionExport(account.getId(),conversionId,campaignId,channelId, date,
				startHour, endHour,request,response);
	}
	//验证记录数
	@RequestMapping("isClickSoLong")
	public void isClickSoLong(String name, Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		if(!"".equals(name)&&name!=null&&channelId!=null){
			Account account = (Account) request.getSession()
					.getAttribute("account");
			Campaign campaign = campaignService.findByName(name, account.getId());
			boolean b =reportService.isClickSoLong(campaign.getId(),channelId, date,
				startHour, endHour,request,response);
			if(b){
				Write2Response.write2Res(response,"true");
			}else{Write2Response.write2Res(response,"false");}
			}
	}
	//验证转化记录数
	@RequestMapping("isConversionSoLong")
	public void isConversionSoLong(Long conversionId,Long campaignId, Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		Account account = (Account) request.getSession()
				.getAttribute("account");
		boolean b = reportService.isConversionSoLong(account.getId(),conversionId,campaignId,channelId, date,
				startHour, endHour,request,response);
		if(b){
			Write2Response.write2Res(response,"true");
		}else{Write2Response.write2Res(response,"false");}
	}
	
	//转化分析，有数据的列
	@RequestMapping("cvtHasDataList")
	public void cvtHasDataList(Long conversionId,Long campaignId, Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		Account account = (Account) request.getSession()
				.getAttribute("account");
		List<ReportAnalyzeBean> reportAnalyzeList = reportService.cvtHasDataLists(account.getId(),conversionId,campaignId,channelId, date,
				startHour, endHour,request,response);
		ReportAnalyzeBean analyzeBean =reportService.cvtHasDataList(reportAnalyzeList);
		JSON jsonArray = (JSON) JSON.toJSON(analyzeBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//点击频次分析
	@RequestMapping("clickHZ")
	public void clickHZ(Long who,String name, Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		if(!"".equals(name)&&name!=null&&channelId!=null){
			Account account = (Account) request.getSession()
					.getAttribute("account");
			Campaign campaign = campaignService.findByName(name, account.getId());
			List<ReportAnalyzeBean> reportAnalyzeList =reportService.clickHZuser(who,campaign.getId(),channelId, date,
				startHour, endHour,request,response);
			JSON jsonArray = (JSON) JSON.toJSON(reportAnalyzeList);
			Write2Response.write2Res(response, jsonArray.toString());
			}
	}
	//点转分析
	@RequestMapping("cvtHZ")
	public void cvtHZ(Long conversionId,Long campaignId, Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		Account account = (Account) request.getSession()
				.getAttribute("account");
		List<ReportAnalyzeBean> reportAnalyzeList =reportService.cvtHZ(account.getId(),conversionId,campaignId,channelId, date,
				startHour, endHour,request,response);
		JSON jsonArray = (JSON) JSON.toJSON(reportAnalyzeList);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//到访分析
	@RequestMapping("visitLog")
	public void visitLog(Long campaignId,Long channelId, Date date, int startHour,
			int endHour, Integer page, Integer rows, String sidx, String sord,HttpServletRequest request, HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		GridBean<ReportAnalyzeBean> gridBean = reportService.visitLog(account.getId(),campaignId,channelId, date,
				startHour, endHour, page,rows,sidx,sord,request);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//到访分析导出
	@RequestMapping("visitExport")
	public void visitExport(Long campaignId,Long channelId, Date date, int startHour,
			int endHour,HttpServletRequest request, HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		reportService.visitExport(account.getId(),campaignId,channelId, date,
				startHour, endHour,request,response);
	}
	//曝光点击重合度分析
	@RequestMapping("rptImpClick")
	public void rptImpClick(String name,Long channelId, Date startDate, Date endDate,HttpServletRequest request,Integer page, Integer rows, String sidx, String sord, HttpServletResponse response ){
		Account account = (Account) request.getSession().getAttribute("account");
		Campaign campaign = campaignService.findByName(name, account.getId());
		GridBean<RptImpClick> gridBean= reportService.rptImpClick(campaign.getId(),channelId,startDate,endDate,page,rows,sidx,sord,request,response);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	@RequestMapping("rptImpHZ")
	public void rptImpHZ(Long id,long who,HttpServletRequest request,HttpServletResponse response){
		RptImpClick clicks = reportService.rptImpHZ(id,who);
		JSON jsonArray = (JSON) JSON.toJSON(clicks);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	@RequestMapping("rptImpHZExport")
	public void rptImpHZExport(String name,Long channelId, Date startDate, Date endDate,HttpServletRequest request, HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		Campaign campaign = campaignService.findByName(name, account.getId());
		reportService.rptImpHZExport(campaign.getId(),channelId,startDate,endDate,request,response);
	}
	@RequestMapping("visitInfo")
	public void visitInfo(String userId,Long campaignId,Long channelId, Date date,
		 Integer page, Integer rows, String sidx, String sord,HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		GridBean<ReportAnalyzeBean> gridBean = reportService.visitLogInfo(userId,account.getId(),campaignId,channelId, date,
				 page,rows,sidx,sord,request);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	@RequestMapping("visitDateInfo")
	public void visitDateInfo(String userId,Long campaignId,Long channelId, Date date,
		 Integer page, Integer rows, String sidx, String sord,HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		List<ReportAnalyzeBean> analyzeList = reportService.visitDateInfo(userId,account.getId(),campaignId,channelId, date,
				 page,rows,sidx,sord,request);
		JSON jsonArray = (JSON) JSON.toJSON(analyzeList);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	@RequestMapping("visitInfoExport")
	public void visitInfoExport(String userId,Long campaignId,Long channelId, Date date,
		 Integer page, Integer rows, String sidx, String sord,HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		reportService.visitInfoExport(userId,account.getId(),campaignId,channelId, date,
				 page,rows,sidx,sord,request,response);
	}
	@RequestMapping("impInfo")
	public void impInfo(Long campaignId,Long channelId,Long creativeId, Date date,long who,
			HttpServletRequest request, HttpServletResponse response){
		List<RptImpClick> list =  reportService.impInfo(campaignId,channelId,creativeId,date,who);
		SortListUtil.sortList(list, "impNum", "desc");
		JSON jsonArray = (JSON) JSON.toJSON(list);
		Write2Response.write2Res(response, jsonArray.toString());
	}
}
