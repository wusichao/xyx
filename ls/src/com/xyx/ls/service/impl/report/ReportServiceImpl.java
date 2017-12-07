package com.xyx.ls.service.impl.report;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyx.ls.bean.GridBean;
import com.xyx.ls.bean.ReportAnalyzeBean;
import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.bean.RptImpClick;
import com.xyx.ls.dao.campaign.CampaignMapper;
import com.xyx.ls.dao.campaign.ChannelMapper;
import com.xyx.ls.dao.report.ReportMapper;
import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.model.campaign.OperateLog;
import com.xyx.ls.service.report.ReportService;
import com.xyx.ls.util.ReadCSV;
import com.xyx.ls.util.SortListUtil;
import com.xyx.ls.util.Tool;
import com.xyx.x.utils.ServletUtils;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
	@Resource
	private ReportMapper reportMapper;
	@Resource
	private ChannelMapper channelMapper;
	@Resource
	private CampaignMapper campaignMapper;

	public CampaignMapper getCampaignMapper() {
		return campaignMapper;
	}

	public void setCampaignMapper(CampaignMapper campaignMapper) {
		this.campaignMapper = campaignMapper;
	}

	public ChannelMapper getChannelMapper() {
		return channelMapper;
	}

	public void setChannelMapper(ChannelMapper channelMapper) {
		this.channelMapper = channelMapper;
	}

	public ReportMapper getReportMapper() {
		return reportMapper;
	}

	public void setReportMapper(ReportMapper reportMapper) {
		this.reportMapper = reportMapper;
	}

	// 趋势与漏斗数据
	@Override
	public ReportBean findConvertionData(Long accountId,Long id, String channelIds,
			String creativeIds, Date startDate, Date date, String type, double d) {
		SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd");
		String starts = null;
		String end = null;
		if (startDate != null) {
			starts = times.format(startDate);
		}
		if (date != null) {
			end = times.format(date);
		}
//		int cpdDay= cpdDayCount(startDate,date,id);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("accountId", accountId.toString());
		map.put("id", id.toString());
		String sql = "";
		if (channelIds != null && !"".equals(channelIds)) {
			sql = sql + " and channel_id in(" + channelIds + ")";
		}
		if (creativeIds != null && !"".equals(creativeIds)) {
			sql = sql + " and creative_id in(" + creativeIds + ")";
		}
		if (startDate != null && !"".equals(startDate)) {
			sql = sql + " and day >='" + starts + "'";
		}
		if (date != null && !"".equals(date)) {
			sql = sql + " and DATEDIFF('" + end + "',day) >= 0";
		}
		map.put("sql", sql);
		ReportBean reportBeans;
		int cpdDay = 0;
		List<ReportBean> reportList = null;
		if(id==-1){
			 reportList = reportMapper.findReportAllGrid(map);
			 reportBeans= reportMapper.findConverAlltionData(map);
		}
		else
			if(channelIds != null && !"".equals(channelIds)&&creativeIds != null && !"".equals(creativeIds)){
			 reportList = reportMapper.findReportGrid(map);
			 reportBeans= reportMapper.findConvertionData(map);
		}else if(!"".equals(channelIds)&&"".equals(creativeIds)){
			 reportList = reportMapper.findReportChannelGrid(map);
			 reportBeans= reportMapper.findConvertionChannelData(map);
		}else if("".equals(channelIds)&&!"".equals(creativeIds)){
			 reportList = reportMapper.findReportCreativeGrid(map);
			 reportBeans= reportMapper.findConvertionCreativeData(map);
		}else{
			 reportList = reportMapper.findReportCampaignGrid(map);
			 reportBeans= reportMapper.findConvertionCampaignData(map);
		}
			
			 cpdDay= reportList.size();
		if(reportBeans!=null){
			DecimalFormat df = new DecimalFormat("#.00");
			if(id==0||id==-1){
				noCampaignCountRate(reportBeans);
			}else{
				Campaign campaign = campaignMapper.findById(id);
				countRate(type, reportBeans, d,cpdDay,campaign.getMonitor_type());
//				if(campaign.getMonitor_type()==1){
//					reportBeans.setCpd(Double.parseDouble(df.format(reportBeans.getCost()/cpdDay)));
//				}
			}
			reportBeans.setDay("合计");
			}
		return reportBeans;
	}

	private void noCampaignCountRate(ReportBean reportBeans) {
		DecimalFormat df = new DecimalFormat("#.00");
		reportBeans.setClickRate("0.0%");
		reportBeans.setReachRate("0.0%");
				if (reportBeans.getReach() != 0) {
					double convertionRate = (double) reportBeans
							.getConvertion() / reportBeans.getReach();
					reportBeans.setConvertionRate(Double.parseDouble(df
							.format(convertionRate * 100)) + "%");
					}else{
						reportBeans.setConvertionRate("0.0%");
					}
	}

	private int cpdDayCount(Date startDate, Date date, Long id) {
		long start;
		long end = 0;
		long diff;
		Campaign campaign = campaignMapper.findById(id);
		if(campaign.getBegin_date().getTime()-startDate.getTime()>0){
			start=campaign.getBegin_date().getTime();
		}else{
			start=startDate.getTime();
		}
		if(campaign.getEnd_date().getTime()-date.getTime()<0){
			start=campaign.getEnd_date().getTime();
		}else{
			end=date.getTime();
		}
		if(startDate.getTime()>campaign.getEnd_date().getTime()){
			return 0;
		}
		diff = end - start;
		 int days = (int) (diff / (1000 * 60 * 60 * 24));
		 if(days==0){
			 return 1;
		 }
		return days+1;
	}

	// 趋势grid
	@Override
	public GridBean<ReportBean> findReportGrid(Long accountId,Long id, String channelIds,
			String creativeIds, Date startDate, Date endDate, int page,
			int rows, String sidx, String sord, String type, double d) {
		SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd");
		String starts = null;
		String end = null;
		if (startDate != null) {
			starts = times.format(startDate);
		}
		if (endDate != null) {
			end = times.format(endDate);
		}
//		int cpdDay= cpdDayCount(startDate,endDate,id);
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "";
		if (channelIds != null && !"".equals(channelIds)) {
			sql = sql + " and channel_id in(" + channelIds + ")";
		}
		if (creativeIds != null && !"".equals(creativeIds)) {
			sql = sql + " and creative_id in(" + creativeIds + ")";
		}
		if (startDate != null && !"".equals(startDate)) {
			sql = sql + " and day >='" + starts + "'";
		}
		if (endDate != null && !"".equals(endDate)) {
			sql = sql + " and DATEDIFF('" + end + "',day) >= 0";
		}
		map.put("id", "and campaign_id="+id.toString());
		map.put("accountId", accountId.toString());
		map.put("sql", sql);
		
		ReportBean report = getSumData(channelIds,creativeIds,id,map);
		
		GridBean<ReportBean> gridBean = new GridBean<ReportBean>();
		List<ReportBean>newreportList=findExportExcel(accountId,id, channelIds, creativeIds, type, d, startDate, endDate);
		double sum =0;
		for(ReportBean bean:newreportList){
			sum= sum+bean.getCost();
		}if(report!=null){
			report.setCost(Double.parseDouble(new DecimalFormat("#.00").format(sum)));
		}
		SortListUtil.sortList(newreportList, sidx, sord);
		int records = newreportList.size();
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		if (rowpage > newreportList.size()) {
			rowpage = newreportList.size();
		}
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(newreportList.subList(start, rowpage));
		gridBean.setUserdata(report);
		return gridBean;
	}

	private ReportBean getSumData(String channelIds, String creativeIds, Long id, HashMap<String, String> map) {
		ReportBean report = new ReportBean();
		if(id==-1){
			map.put("table", "rpt_ac");
			map.remove("id");
			report = reportMapper.findReportSum(map);
		}else
		if(channelIds != null && !"".equals(channelIds)&&creativeIds != null && !"".equals(creativeIds)){
			map.put("table", "rpt_base");
			report = reportMapper.findReportSum(map);
		}else if(!"".equals(channelIds)&&"".equals(creativeIds)){
			map.put("table", "rpt_ac_ca_ch");
			report = reportMapper.findReportSum(map);
		}else if("".equals(channelIds)&&!"".equals(creativeIds)){
			map.put("table", "rpt_ac_ca_cr");
			report = reportMapper.findReportSum(map);
		}else{
			map.put("table", "rpt_ac_ca");
			report = reportMapper.findReportSum(map);
		}if(report!=null){
			report.setDay("合计");
		}
		return report;
	}

	// 维度排名
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ReportBean> findDimensionRank(Long accountId,String dimension, Long id,
			String type, double d, String level, Date startDate, Date endDate,
			HttpServletRequest request) {
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd");
		String starts = null;
		String end = null;
		if (startDate != null) {
			starts = times.format(startDate);
		}
		if (endDate != null) {
			end = times.format(endDate);
		}
//		int cpdDay= cpdDayCount(startDate,endDate,id);
		HashMap<String, String> map = new HashMap<String, String>();
		List<ReportBean> reportBeanList = new ArrayList<ReportBean>();
		map.put("campaignId", id.toString());
		String sql = "";
		int cpdDay = 0;
			if (startDate != null && !"".equals(startDate)) {
				sql = sql + "and  day >='" + starts + "'";
			}
			if (endDate != null && !"".equals(endDate)) {
				sql = sql + "and DATEDIFF('" + end + "',day) >= 0";
			}
			 cpdDay= reportMapper.getCpdDay(map);
		
		map.put("accountId", accountId.toString());
		map.put("sql", sql);
		DecimalFormat df = new DecimalFormat("#.00");
		if ("渠道".equals(dimension)) {
			map.put("selectId", "channel_id,");
			map.put("rank", " and channel_id!=0 group by channel_id ");
			map.put("sidx","");
			map.put("","");
			reportBeanList = reportMapper
					.findDimensionRank(map);
			Campaign campaign = campaignMapper.findById(id);
			for(ReportBean reportBean:reportBeanList){
				Channel channel=channelMapper.findByIds(reportBean.getChannel_id().toString());
				if(channel!=null){
					reportBean.setDimension(channel.getName());
					countRate(type, reportBean, d,cpdDay,campaign.getMonitor_type());
					if(campaign.getMonitor_type()==1){
						reportBean.setCpd(Double.parseDouble(df.format(reportBean.getCost()/cpdDay)));
					}
				}
			}
		}
		if ("创意".equals(dimension)) {
			map.put("selectId", "creative_id,");
			map.put("rank", "and creative_id!=0 group by creative_id ");
			reportBeanList = reportMapper
					.findDimensionCreativeRank(map);
			Campaign campaign = campaignMapper.findById(id);
			for(ReportBean reportBean:reportBeanList){
				Creative creative = campaignMapper.findCreativeByIds(reportBean.getCreative_id().toString());
				if(creative!=null){
					reportBean.setDimension(creative.getName());
					countRate(type, reportBean, d,cpdDay,campaign.getMonitor_type());
					if(campaign.getMonitor_type()==1){
						reportBean.setCpd(Double.parseDouble(df.format(reportBean.getCost()/cpdDay)));
					}
				}
				
			}
		}
		if ("时段".equals(dimension)) {
				reportBeanList = reportMapper
						.findDimensionHourRank(map);
			
			if(id==0){
				for(ReportBean reportBean:reportBeanList){
					reportBean.setDimension(reportBean.getHour()+"");
					noCampaignCountRate(reportBean);
				}
				
			}else{
				Campaign campaign = campaignMapper.findById(id);
				for(ReportBean reportBean:reportBeanList){
					reportBean.setDimension(reportBean.getHour()+"");
					countRate(type, reportBean, d,cpdDay,campaign.getMonitor_type());
					if(campaign.getMonitor_type()==1){
						reportBean.setCpd(Double.parseDouble(df.format(reportBean.getCost()/cpdDay)));
					}
				}
			}
			
		}
		if ("geo".equals(dimension)) {
			if("city".equals(level)){
				map.put("selectId", "geo_id,");
				map.put("rank", "geo_id");
					reportBeanList = reportMapper
							.findDimensionGeoRank(map);
				
				if(id==0){
					for(ReportBean reportBean:reportBeanList){
						List<String> geoNanme=geoMap.get(reportBean.getGeo_id()+"");
						if(geoNanme==null||geoNanme.size()==0){
//							reportBean.setDimension(geoNanme.get(0));
//							reportBean.setCity(geoNanme.get(2));
//							reportBean.setProvince(geoNanme.get(0));
							reportBean.setDimension("");
							reportBean.setCity("");
							reportBean.setProvince("");
						}else{
							reportBean.setDimension(geoNanme.get(0));
							reportBean.setCity(geoNanme.get(2));
							reportBean.setProvince(geoNanme.get(0));
							}
						noCampaignCountRate(reportBean);
					}
				}else{
					Campaign campaign = campaignMapper.findById(id);
					for(ReportBean reportBean:reportBeanList){
						List<String> geoNanme=geoMap.get(reportBean.getGeo_id()+"");
						if(geoNanme==null||geoNanme.size()==0){
//							reportBean.setDimension(geoNanme.get(0));
//							reportBean.setCity(geoNanme.get(2));
//							reportBean.setProvince(geoNanme.get(0));
							reportBean.setDimension("");
							reportBean.setCity("");
							reportBean.setProvince("");
						}else{
							reportBean.setDimension(geoNanme.get(0));
							reportBean.setCity(geoNanme.get(2));
							reportBean.setProvince(geoNanme.get(0));
							}
						countRate(type, reportBean, d,cpdDay,campaign.getMonitor_type());
						if(campaign.getMonitor_type()==1){
							reportBean.setCpd(Double.parseDouble(df.format(reportBean.getCost()/cpdDay)));
						}
					}
				}
				
				SortListUtil.sortList(reportBeanList, "visit", "desc");
			}
			else{
				map.put("selectId", "LEFT(geo_id, 6)AS pro ,geo_id,");
				map.put("rank", "pro");
					reportBeanList = reportMapper
							.findDimensionGeoRank(map);
				
				if(id==0){
					for(ReportBean reportBean:reportBeanList){
						List<String> geoNanme=geoMap.get(reportBean.getGeo_id()+"");
						if(geoNanme.size()>0){
							String pro = geoNanme.get(0).substring(0,
									geoNanme.get(0).length() - 1);
							reportBean.setProvince(pro);
							reportBean.setDimension(geoNanme.get(0)+"-"+geoNanme.get(2));
							noCampaignCountRate(reportBean);
					}}
				}else{
					Campaign campaign = campaignMapper.findById(id);
					for(ReportBean reportBean:reportBeanList){
						List<String> geoNanme=geoMap.get(reportBean.getGeo_id()+"");
						if(geoNanme.size()>0){
							String pro = geoNanme.get(0).substring(0,
									geoNanme.get(0).length() - 1);
							reportBean.setProvince(pro);
							reportBean.setDimension(geoNanme.get(0)+"-"+geoNanme.get(2));
							countRate(type, reportBean, d,cpdDay,campaign.getMonitor_type());
							if(campaign.getMonitor_type()==1){
								reportBean.setCpd(Double.parseDouble(df.format(reportBean.getCost()/cpdDay)));
							}
						}
						
						}
				}
				
			}
		}
		return reportBeanList;
		}

	// 下载趋势
	@Override
	public List<ReportBean> findExportExcel(Long accountId,Long id, String channelIds,
			String creativeIds, String type, double d, Date startDate,
			Date endDate) {
		SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd");
		String starts = null;
		String end = null;
		if (startDate != null) {
			starts = times.format(startDate);
		}
		if (endDate != null) {
			end = times.format(endDate);
		}
//		int cpdDay= cpdDayCount(startDate,endDate,id);
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "";
		if (channelIds != null && !"".equals(channelIds)) {
			sql = sql + " and channel_id in(" + channelIds + ")";
		}
		if (creativeIds != null && !"".equals(creativeIds)) {
			sql = sql + " and creative_id in(" + creativeIds + ")";
		}
		if (startDate != null && !"".equals(startDate)) {
			sql = sql + " and day >='" + starts + "'";
		}
		if (endDate != null && !"".equals(endDate)) {
			sql = sql + " and DATEDIFF('" + end + "',day) >= 0";
		}
		map.put("id", id.toString());
		map.put("accountId", accountId.toString());
		map.put("sql", sql);
		DecimalFormat df = new DecimalFormat("#.00");
		List<ReportBean> reportList;
		if(id==-1){
			reportList = reportMapper.findReportAllGrid(map);
		}else
		if(channelIds != null && !"".equals(channelIds)&&creativeIds != null && !"".equals(creativeIds)){
			 reportList = reportMapper.findReportGrid(map);
		}else if(!"".equals(channelIds)&&"".equals(creativeIds)){
			 reportList = reportMapper.findReportChannelGrid(map);
		}else if("".equals(channelIds)&&!"".equals(creativeIds)){
			 reportList = reportMapper.findReportCreativeGrid(map);
		}else{
			 reportList = reportMapper.findReportCampaignGrid(map);
		}
			if(id==0||id==-1){
				for (ReportBean reportBean : reportList) {
					noCampaignCountRate(reportBean);
				}
				SortListUtil.sortList(reportList, "imp", "desc");
			}else{
				int cpdDay= reportList.size();
				Campaign campaign = campaignMapper.findById(id);
				if (reportList.size() > 0) {
					double countCost = 0;;
					for (ReportBean reportBean : reportList) {
						countRate(type, reportBean, d,cpdDay,campaign.getMonitor_type());
						countCost=countCost+reportBean.getCost();
			}
					if(campaign.getMonitor_type()==1){
				for (ReportBean reportBean : reportList) {
					reportBean.setCpd(Double.parseDouble(df.format(countCost/cpdDay)));
		}}
				}
			SortListUtil.sortList(reportList, "cost", "desc");
			}
		return reportList;
	}

	// 导出趋势数据
	@Override
	public void exportExcel(Integer type,String campaignName,Date startDate, Date endDate, String who, List<ReportBean> reportList,
			HttpServletResponse response) {
		try {
			SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd");
			String starts = null;
			String end = null;
			if (startDate != null) {
				starts = times.format(startDate);
			}
			if (endDate != null) {
				end = times.format(endDate);
			}
			String name ="";
			if("日期".equals(who)){
				name =""+campaignName+"_"+starts+"-"+end+".xls";
			}
			if("渠道".equals(who)){
				name =""+campaignName+"_渠道_"+starts+"-"+end+".xls";
			}
			if("创意".equals(who)){
				name =""+campaignName+"_创意_"+starts+"-"+end+".xls";
			}
			if("geo".equals(who)){
				name =""+campaignName+"_地域_"+starts+"-"+end+".xls";
			}
			if("时段".equals(who)){
				name =""+campaignName+"_时段_"+starts+"-"+end+".xls";
			}
			if(who==""||who==null){name="样例.xls";}
			NumberFormat doubleFormat3;
			WritableCellFormat doublewcf3;
			 doubleFormat3=new NumberFormat("0.00%");
             doublewcf3=new WritableCellFormat(doubleFormat3);
            
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8")); 
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("sheet", 0);
			if ("geo".equals(who)) {
				if(type==null){
//					时段、浏览数、浏览人数、浏览IP数、转化数、转化人数、转化IP数
					sheet.addCell(new Label(0, 0, "省份"));
					sheet.addCell(new Label(1, 0, "城市"));
					sheet.addCell(new Label(2, 0, "浏览数"));
					sheet.addCell(new Label(3, 0, "浏览人数"));
					sheet.addCell(new Label(4, 0, "浏览IP数"));
					sheet.addCell(new Label(5, 0, "转化数"));
					sheet.addCell(new Label(6, 0, "转化人数"));
					sheet.addCell(new Label(7, 0, "转化IP数"));
					int i = 1;
					for (ReportBean reportBean : reportList) {
						sheet.addCell(new Label(0, i, reportBean.getDimension()));
						sheet.addCell(new Label(1, i, reportBean.getCity()));
						sheet.addCell(new jxl.write.Number(2, i, reportBean.getVisit()));
						sheet.addCell(new jxl.write.Number(3, i, reportBean.getVisitUser()));
						sheet.addCell(new jxl.write.Number(4, i, reportBean.getVisitIp()));
						sheet.addCell(new jxl.write.Number(5, i, reportBean.getConvertion()));
						sheet.addCell(new jxl.write.Number(6, i, reportBean.getConversionUser()));
						sheet.addCell(new jxl.write.Number(7, i, reportBean.getConversionIp()));
						i++;
					}
				}else if(type==0){
//					时段、花费、到达数、到达人数、到达IP数、浏览数、浏览人数、浏览IP数、转化数、转化人数、转化IP数、转化率、CPL、CPA

					sheet.addCell(new Label(0, 0, "省份"));
					sheet.addCell(new Label(1, 0, "城市"));
					sheet.addCell(new Label(2, 0, "花费"));
					sheet.addCell(new Label(3, 0, "到达数"));
					sheet.addCell(new Label(4, 0, "到达人数"));
					sheet.addCell(new Label(5, 0, "到达IP数")); 
					sheet.addCell(new Label(6, 0, "浏览数"));
					sheet.addCell(new Label(7, 0, "浏览人数"));
					sheet.addCell(new Label(8, 0, "浏览IP数"));
					sheet.addCell(new Label(9, 0, "转化数"));
					sheet.addCell(new Label(10, 0, "转化人数"));
					sheet.addCell(new Label(11, 0, "转化IP数"));
					sheet.addCell(new Label(12, 0, "转化率"));
					sheet.addCell(new Label(13, 0, "CPL"));
					sheet.addCell(new Label(14, 0, "CPA"));
					int i = 1;
					for (ReportBean reportBean : reportList) {
						sheet.addCell(new Label(0, i, reportBean.getDimension()));
						sheet.addCell(new Label(1, i, reportBean.getCity()));
						sheet.addCell(new jxl.write.Number(2, i, reportBean.getCost()));
						sheet.addCell(new jxl.write.Number(3, i, reportBean.getReach()));
						sheet.addCell(new jxl.write.Number(4, i, reportBean.getReachUser()));
						sheet.addCell(new jxl.write.Number(5, i, reportBean.getReachIp()));
						sheet.addCell(new jxl.write.Number(6, i, reportBean.getVisit()));
						sheet.addCell(new jxl.write.Number(7, i, reportBean.getVisitUser()));
						sheet.addCell(new jxl.write.Number(8, i,reportBean.getVisitIp()));
						sheet.addCell(new jxl.write.Number(9, i,reportBean.getConvertion()));
						sheet.addCell(new jxl.write.Number(10, i,reportBean.getConversionUser()));
						sheet.addCell(new jxl.write.Number(11, i,reportBean.getConversionIp()));
						sheet.addCell(new jxl.write.Number(12, i, Double.parseDouble(reportBean.getConvertionRate().substring(0, reportBean.getConvertionRate().length()-1))/100,doublewcf3));
						sheet.addCell(new jxl.write.Number(13, i, reportBean.getCpl()));
						sheet.addCell(new jxl.write.Number(14, i, reportBean.getCpa()));
						i++;
					}
				}else {
//					时段、花费、展示数、展示人数、点击数、点击人数、到达数、到达人数、浏览数、浏览人数、转化数、转化人数、点击率、到达率、转化率、CPL、CPA
				sheet.addCell(new Label(0, 0, "省份"));
				sheet.addCell(new Label(1, 0, "城市"));
				sheet.addCell(new Label(2, 0, "花费"));
				sheet.addCell(new Label(3, 0, "展示数"));
				sheet.addCell(new Label(4, 0, "展示人数"));
				sheet.addCell(new Label(5, 0, "点击数")); 
				sheet.addCell(new Label(6, 0, "点击人数"));
				sheet.addCell(new Label(7, 0, "到达数"));
				sheet.addCell(new Label(8, 0, "到达人数"));
				sheet.addCell(new Label(9, 0, "浏览数"));
				sheet.addCell(new Label(10, 0, "浏览人数"));
				sheet.addCell(new Label(11, 0, "转化数"));
				sheet.addCell(new Label(12, 0, "转化人数"));
				sheet.addCell(new Label(13, 0, "点击率"));
				sheet.addCell(new Label(14, 0, "到达率"));
				sheet.addCell(new Label(15, 0, "转化率"));
				sheet.addCell(new Label(16, 0, "CPM"));
				sheet.addCell(new Label(17, 0, "CPC"));
				sheet.addCell(new Label(18, 0, "CPL"));
				sheet.addCell(new Label(19, 0, "CPA"));
				int i = 1;
				for (ReportBean reportBean : reportList) {
					sheet.addCell(new Label(0, i, reportBean.getDimension()));
					sheet.addCell(new Label(1, i, reportBean.getCity()));
					sheet.addCell(new jxl.write.Number(2, i, reportBean.getCost()));
					sheet.addCell(new jxl.write.Number(3, i, reportBean.getImp()));
					sheet.addCell(new jxl.write.Number(4, i, reportBean.getImpUser()));
					sheet.addCell(new jxl.write.Number(5, i, reportBean.getClick()));
					sheet.addCell(new jxl.write.Number(6, i, reportBean.getClickUser()));
					sheet.addCell(new jxl.write.Number(7, i, reportBean.getReach()));
					sheet.addCell(new jxl.write.Number(8, i, reportBean.getReachUser()));
					sheet.addCell(new jxl.write.Number(9, i, reportBean.getVisit()));
					sheet.addCell(new jxl.write.Number(10, i, reportBean.getVisitUser()));
					sheet.addCell(new jxl.write.Number(11, i, reportBean.getConvertion()));
					sheet.addCell(new jxl.write.Number(12, i, reportBean.getConversionUser()));
					sheet.addCell(new jxl.write.Number(13, i,Double.parseDouble(reportBean.getClickRate().substring(0, reportBean.getClickRate().length()-1))/100 ,doublewcf3));
					sheet.addCell(new jxl.write.Number(14, i, Double.parseDouble(reportBean.getReachRate().substring(0, reportBean.getReachRate().length()-1))/100,doublewcf3));
					sheet.addCell(new jxl.write.Number(15, i, Double.parseDouble(reportBean.getConvertionRate().substring(0, reportBean.getConvertionRate().length()-1))/100,doublewcf3));
					sheet.addCell(new jxl.write.Number(16, i, reportBean.getCpm()));
					sheet.addCell(new jxl.write.Number(17, i, reportBean.getCpc()));
					sheet.addCell(new jxl.write.Number(18, i, reportBean.getCpl()));
					sheet.addCell(new jxl.write.Number(19, i, reportBean.getCpa()));
					i++;
				}
			}} else {

				if(type==null){
//					时段、浏览数、浏览人数、浏览IP数、转化数、转化人数、转化IP数
					sheet.addCell(new Label(0, 0, who));
					sheet.addCell(new Label(1, 0, "浏览数"));
					sheet.addCell(new Label(2, 0, "浏览人数"));
					sheet.addCell(new Label(3, 0, "浏览IP数"));
					sheet.addCell(new Label(4, 0, "转化数"));
					sheet.addCell(new Label(5, 0, "转化人数"));
					sheet.addCell(new Label(6, 0, "转化IP数"));
					int i = 1;
					for (ReportBean reportBean : reportList) {
						if ("日期".equals(who)) {
							sheet.addCell(new Label(0, i, reportBean.getDay()));
						} else
						if("时段".equals(who)) {
							sheet.addCell(new jxl.write.Number(0, i,Integer.parseInt(reportBean.getDimension())));
						}else{
							sheet.addCell(new Label(0, i, reportBean.getDimension()));
						}
						sheet.addCell(new jxl.write.Number(1, i, reportBean.getVisit()));
						sheet.addCell(new jxl.write.Number(2, i, reportBean.getVisitUser()));
						sheet.addCell(new jxl.write.Number(3, i, reportBean.getVisitIp()));
						sheet.addCell(new jxl.write.Number(4, i, reportBean.getConvertion()));
						sheet.addCell(new jxl.write.Number(5, i, reportBean.getConversionUser()));
						sheet.addCell(new jxl.write.Number(6, i, reportBean.getConversionIp()));
						i++;
					}
				}else if(type==0){
//					时段、花费、到达数、到达人数、到达IP数、浏览数、浏览人数、浏览IP数、转化数、转化人数、转化IP数、转化率、CPL、CPA

					sheet.addCell(new Label(0, 0, who));
					sheet.addCell(new Label(1, 0, "花费"));
					sheet.addCell(new Label(2, 0, "到达数"));
					sheet.addCell(new Label(3, 0, "到达人数"));
					sheet.addCell(new Label(4, 0, "到达IP数")); 
					sheet.addCell(new Label(5, 0, "浏览数"));
					sheet.addCell(new Label(6, 0, "浏览人数"));
					sheet.addCell(new Label(7, 0, "浏览IP数"));
					sheet.addCell(new Label(8, 0, "转化数"));
					sheet.addCell(new Label(9, 0, "转化人数"));
					sheet.addCell(new Label(10, 0, "转化IP数"));
					sheet.addCell(new Label(11, 0, "转化率"));
					sheet.addCell(new Label(12, 0, "CPL"));
					sheet.addCell(new Label(13, 0, "CPA"));
					int i = 1;
					for (ReportBean reportBean : reportList) {
						if ("日期".equals(who)) {
							sheet.addCell(new Label(0, i, reportBean.getDay()));
						} else
						if("时段".equals(who)) {
							sheet.addCell(new jxl.write.Number(0, i,Integer.parseInt(reportBean.getDimension())));
						}else{
							sheet.addCell(new Label(0, i, reportBean.getDimension()));
						}
						sheet.addCell(new jxl.write.Number(1, i, reportBean.getCost()));
						sheet.addCell(new jxl.write.Number(2, i, reportBean.getReach()));
						sheet.addCell(new jxl.write.Number(3, i, reportBean.getReachUser()));
						sheet.addCell(new jxl.write.Number(4, i, reportBean.getReachIp()));
						sheet.addCell(new jxl.write.Number(5, i, reportBean.getVisit()));
						sheet.addCell(new jxl.write.Number(6, i, reportBean.getVisitUser()));
						sheet.addCell(new jxl.write.Number(7, i,reportBean.getVisitIp()));
						sheet.addCell(new jxl.write.Number(8, i,reportBean.getConvertion()));
						sheet.addCell(new jxl.write.Number(9, i,reportBean.getConversionUser()));
						sheet.addCell(new jxl.write.Number(10, i,reportBean.getConversionIp()));
						sheet.addCell(new jxl.write.Number(11, i, Double.parseDouble(reportBean.getConvertionRate().substring(0, reportBean.getConvertionRate().length()-1))/100,doublewcf3));
						sheet.addCell(new jxl.write.Number(12, i, reportBean.getCpl()));
						sheet.addCell(new jxl.write.Number(13, i, reportBean.getCpa()));
						i++;
					}
				}else {
//					时段、花费、展示数、展示人数、点击数、点击人数、到达数、到达人数、浏览数、浏览人数、转化数、转化人数、点击率、到达率、转化率、CPL、CPA
					sheet.addCell(new Label(0, 0, who));
				sheet.addCell(new Label(1, 0, "花费"));
				sheet.addCell(new Label(2, 0, "展示数"));
				sheet.addCell(new Label(3, 0, "展示人数"));
				sheet.addCell(new Label(4, 0, "点击数")); 
				sheet.addCell(new Label(5, 0, "点击人数"));
				sheet.addCell(new Label(6, 0, "到达数"));
				sheet.addCell(new Label(7, 0, "到达人数"));
				sheet.addCell(new Label(8, 0, "浏览数"));
				sheet.addCell(new Label(9, 0, "浏览人数"));
				sheet.addCell(new Label(10, 0, "转化数"));
				sheet.addCell(new Label(11, 0, "转化人数"));
				sheet.addCell(new Label(12, 0, "点击率"));
				sheet.addCell(new Label(13, 0, "到达率"));
				sheet.addCell(new Label(14, 0, "转化率"));
				sheet.addCell(new Label(15, 0, "CPM"));
				sheet.addCell(new Label(16, 0, "CPC"));
				sheet.addCell(new Label(17, 0, "CPL"));
				sheet.addCell(new Label(18, 0, "CPA"));
				int i = 1;
				for (ReportBean reportBean : reportList) {
					if ("日期".equals(who)) {
						sheet.addCell(new Label(0, i, reportBean.getDay()));
					} else
					if("时段".equals(who)) {
						sheet.addCell(new jxl.write.Number(0, i,Integer.parseInt(reportBean.getDimension())));
					}else{
						sheet.addCell(new Label(0, i, reportBean.getDimension()));
					}
					sheet.addCell(new jxl.write.Number(1, i, reportBean.getCost()));
					sheet.addCell(new jxl.write.Number(2, i, reportBean.getImp()));
					sheet.addCell(new jxl.write.Number(3, i, reportBean.getImpUser()));
					sheet.addCell(new jxl.write.Number(4, i, reportBean.getClick()));
					sheet.addCell(new jxl.write.Number(5, i, reportBean.getClickUser()));
					sheet.addCell(new jxl.write.Number(6, i, reportBean.getReach()));
					sheet.addCell(new jxl.write.Number(7, i, reportBean.getReachUser()));
					sheet.addCell(new jxl.write.Number(8, i, reportBean.getVisit()));
					sheet.addCell(new jxl.write.Number(9, i, reportBean.getVisitUser()));
					sheet.addCell(new jxl.write.Number(10, i, reportBean.getConvertion()));
					sheet.addCell(new jxl.write.Number(11, i, reportBean.getConversionUser()));
					sheet.addCell(new jxl.write.Number(12, i, Double.parseDouble(reportBean.getClickRate().substring(0, reportBean.getClickRate().length()-1))/100 ,doublewcf3));
					sheet.addCell(new jxl.write.Number(13, i, Double.parseDouble(reportBean.getReachRate().substring(0, reportBean.getReachRate().length()-1))/100,doublewcf3));
					sheet.addCell(new jxl.write.Number(14, i, Double.parseDouble(reportBean.getConvertionRate().substring(0, reportBean.getConvertionRate().length()-1))/100,doublewcf3));
					sheet.addCell(new jxl.write.Number(15, i, reportBean.getCpm()));
					sheet.addCell(new jxl.write.Number(16, i, reportBean.getCpc()));
					sheet.addCell(new jxl.write.Number(17, i, reportBean.getCpl()));
					sheet.addCell(new jxl.write.Number(18, i, reportBean.getCpa()));
					i++;
				}
			}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
		}

	}

	@Override
	public GridBean<ReportBean> jointGrid(int page, int rows, String sidx,
			String sord, int records, List<ReportBean> reportList) {
		GridBean<ReportBean> gridBean = new GridBean<ReportBean>();
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(reportList);
		return gridBean;
	}

//计算花费与点击到达转化率
	private  ReportBean countRate(String type,ReportBean reportBeans,double d, int cpdDay, Integer campaignType){
		DecimalFormat df = new DecimalFormat("#.00");
		if ("CPM".equals(type)) {
			reportBeans.setCost(reportBeans.getImp() * d / 1000D);
		}
		if ("CPC".equals(type)) {
			reportBeans.setCost(reportBeans.getClick() * d);
		}
		if ("CPL".equals(type)) {
			reportBeans.setCost(reportBeans.getReach() * d);
		}
		if ("CPA".equals(type)) {
			reportBeans.setCost(reportBeans.getConvertion() * d);
		}
		if("CPD".equals(type)){
			reportBeans.setCost(cpdDay * d);
		}
		if(campaignType==0){
			reportBeans.setClickRate("0.0%");
			reportBeans.setReachRate("0.0%");
			if (reportBeans.getReach() != 0) {
				double convertionRate = (double) reportBeans
						.getConvertion() / reportBeans.getReach();
				reportBeans.setCpl(Double.parseDouble(df
						.format((double) reportBeans.getCost()
								/ reportBeans.getReach())));
				
				if(reportBeans.getConvertion()!=0){
					reportBeans.setConvertionRate(Double.parseDouble(df
							.format(convertionRate * 100)) + "%");
					reportBeans.setCpa(Double.parseDouble(df
						.format((double) reportBeans.getCost()
								/ reportBeans.getConvertion())));
				}else{
					reportBeans.setCpa(0D);
					reportBeans.setConvertionRate("0.0%");
					}
				
			} else {
				reportBeans.setConvertionRate("0.0%");
			}
			}else{
		if (reportBeans.getImp() != 0) {
			double clickRate = (double) reportBeans.getClick()
					/ reportBeans.getImp();
			reportBeans.setClickRate(Double.parseDouble(df
					.format(clickRate * 100)) + "%");
			reportBeans.setCpm(Double.parseDouble(df
					.format((double) reportBeans.getCost()
							/ reportBeans.getImp() * 1000)));
			}else{
				reportBeans.setClickRate("0.0%");
				reportBeans.setCpm(0D);
			}
		
			if (reportBeans.getClick() != 0) {
				double reachRate = (double) reportBeans.getReach()
						/ reportBeans.getClick();
				reportBeans.setReachRate(Double.parseDouble(df
						.format(reachRate * 100)) + "%");
				reportBeans.setCpc(Double.parseDouble(df
						.format((double) reportBeans.getCost()
								/ reportBeans.getClick())));
				
				}else{
					reportBeans.setReachRate("0.0%");
					reportBeans.setCpc(0D);
				}
				
				if (reportBeans.getReach() != 0) {
					double convertionRate = (double) reportBeans
							.getConvertion() / reportBeans.getReach();
					reportBeans.setConvertionRate(Double.parseDouble(df
							.format(convertionRate * 100)) + "%");
					reportBeans.setCpl(Double.parseDouble(df
							.format((double) reportBeans.getCost()
									/ reportBeans.getReach())));
					}else{
						reportBeans.setCpl(0D);
						reportBeans.setConvertionRate("0.0%");
					}
				
					if(reportBeans.getConvertion()!=0){
						reportBeans.setCpa(Double.parseDouble(df
							.format((double) reportBeans.getCost()
									/ reportBeans.getConvertion())));
					}else{
						reportBeans.setCpa(0D);
						}
					}
		if ("CPM".equals(type)) {
			reportBeans.setCost(Double.parseDouble(df.format(reportBeans.getImp() * d / 1000D)));
			reportBeans.setCpm(d);
		}
		if ("CPC".equals(type)) {
			reportBeans.setCost(Double.parseDouble(df.format(reportBeans.getClick() * d)));
			reportBeans.setCpc(d);
		}
		if ("CPL".equals(type)) {
			reportBeans.setCost(Double.parseDouble(df.format(reportBeans.getReach() * d)));
			reportBeans.setCpl(d);
		}
		if ("CPA".equals(type)) {
			reportBeans.setCost(Double.parseDouble(df.format(reportBeans.getConvertion() * d)));
			reportBeans.setCpa(d);
		}
		if ("CPD".equals(type)) {
			reportBeans.setCost(Double.parseDouble(df.format(cpdDay* d)));
			reportBeans.setCpd(d);
		}
		return reportBeans;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GridBean<ReportAnalyzeBean> getClick_Log(Long who,Integer clickNum ,Long id, Long channelId,
			Date date, int startHour, int endHour, Integer page, Integer rows,
			String sidx, String sord,HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<ReportAnalyzeBean> gridBean = new GridBean<ReportAnalyzeBean>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		map.put("id", id);
		map.put("channelId", channelId);
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
		map.put("sidx", "req_time");
		map.put("sord", sord);
		map.put("start", start);
		map.put("rowpage", rowpage);
		map.put("clickNum", clickNum);
		int records = 0;
		if(who==null){
			records= reportMapper.getClick_LogRecords(id,channelId,date,startHour,endHour);
		}else if(who==0 && clickNum!=null){
			records=  reportMapper.getAllClickByClickNumUserIdRecords(map);
		}else if(who==1 && clickNum!=null){
			records=  reportMapper.getAllClickByClickNumIpRecords(map);
		}
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		List<ReportAnalyzeBean> reportAnalyzeList = null;
		if(who==null){
			reportAnalyzeList=  reportMapper.getClick_Log(map);
		}else if(who==0 && clickNum!=null){
		 reportAnalyzeList=  reportMapper.getAllClickByClickNumUserId(map);
		}else if(who==1 && clickNum!=null){
			 reportAnalyzeList=  reportMapper.getAllClickByClickNumIp(map);
		}
			
		
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			if(analyzeBean.getUrl()!=null){
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
			}
		}
		SortListUtil.sortList(reportAnalyzeList, sidx, sord);
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(reportAnalyzeList);
		return gridBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GridBean<ReportAnalyzeBean> getConversion_Log(Long accountId,Integer gap,Long conversionId,Long campaignId,
			Long channelId, Date date, int startHour, int endHour,
			Integer page, Integer rows, String sidx, String sord,
			HttpServletRequest request) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<ReportAnalyzeBean> gridBean = new GridBean<ReportAnalyzeBean>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		map.put("accountId", accountId);
		if(conversionId!=null){map.put("conversionId", "and ctl.cvt_id="+conversionId);}
		if(campaignId!=null){map.put("campaignId", "and ctl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and ctl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
		map.put("sidx", "click_time");
		map.put("sord", sord);
		map.put("start", start);
		map.put("rowpage", rowpage);
		if(gap!=null){
			switch (gap) {
			case 1:
				map.put("gap", "1 AND 60");
				break;
			case 2:
				map.put("gap", "60 AND 5*60");
				break;
			case 3:
				map.put("gap", "5*60 AND 60*60");
				break;
			case 4:
				map.put("gap", "60*60 AND 24*60*60");
				break;
			case 5:
				map.put("gap", "24*60*60 AND 24*60*60*3");
				break;
			case 6:
				map.put("gap", "24*60*60*3 AND 24*60*60*7");
				break;
			case 7:
				map.put("gap", "24*60*60*7 AND 24*60*60*15");
				break;
			default:
				break;
			}
		}
		int records;
		if(gap!=null){
			 records = reportMapper.getAllCvtBycvtgapRecords(map);
		}else{
			 records = reportMapper.getConversion_LogRecords(map);
		}
		
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		List<ReportAnalyzeBean> reportAnalyzeList = null;
		if(gap!=null){
			reportAnalyzeList=  reportMapper.getAllCvtBycvtgap(map);
		}else{
			reportAnalyzeList=  reportMapper.getConversion_Log(map);
		}
		 
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			analyzeBean.setUserId("<a href='#' style='text-decoration:underline;' class='oncolor' onclick=getvisit('"+analyzeBean.getUserId()+"')>"+analyzeBean.getUserId()+"</a>");
			
			long diff;
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
				
			}
			if(analyzeBean.getConversionTime()!=null&&analyzeBean.getClickTime()!=null){
				 diff = analyzeBean.getConversionTime().getTime() - analyzeBean.getClickTime().getTime();
				 	Long days = diff / (1000 * 60 * 60 * 24);
				    Long hour=(diff/(60*60*1000)-days*24);
				    Long min=((diff/(60*1000))-days*24*60-hour*60);
				    analyzeBean.setGap(days+"天"+hour+"时"+min+"分");
			}else{ analyzeBean.setGap("");}
			if(analyzeBean.getUrl()!=null){
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
			}
			if(analyzeBean.getOther()!=null){
				analyzeBean.setOther(ServletUtils.decode(analyzeBean.getOther()));
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
			}
			String[] others = {};
			if(analyzeBean.getOther()!=null){
				 others = analyzeBean.getOther().split(",");
				}
			for(String other:others){
				String a = other.replace("<", "＜");
				String b = a.replace(">", "＞");
				String[] o = b.split("::");
				if(o.length>1){
				if(o[0].equals("p1")){analyzeBean.setPhonep(o[1]);}
				if(o[0].equals("p2")){analyzeBean.setPeopleNamep(o[1]);}
				if(o[0].equals("p3")){analyzeBean.setEmailp(o[1]);}
				if(o[0].equals("p4")){analyzeBean.setNotep(o[1]);}
				if(o[0].equals("p5")){analyzeBean.setSexp(o[1]);}
				if(o[0].equals("p6")){analyzeBean.setAgep(o[1]);}
				if(o[0].equals("p7")){analyzeBean.setCityp(o[1]);}
				if(o[0].equals("p8")){analyzeBean.setDizhip(o[1]);}
				if(o[0].equals("p9")){analyzeBean.setMoneyp(o[1]);}
				if(o[0].equals("p10")){analyzeBean.setCompanyNamep(o[1]);}
				if(o[0].equals("p11")){analyzeBean.setWebSitep(o[1]);}
				}
			}
		}
		SortListUtil.sortList(reportAnalyzeList, sidx, sord);
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(reportAnalyzeList);
		return gridBean;
	}

	private void excelupload(List<ReportAnalyzeBean> reportAnalyzeList, HttpServletResponse response) {
		SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = times.format(new Date());
		try {
			String name="点击分析"+time+".xls";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("sheet",0);
			CellView cellView = new CellView();  
			cellView.setAutosize(true); //设置自动大小  
			sheet.addCell(new Label(0, 0, "时间"));
			sheet.addCell(new Label(1, 0, "城市"));
			sheet.addCell(new Label(2, 0, "IP"));
			sheet.addCell(new Label(3, 0, "用户ID"));
			sheet.addCell(new Label(4, 0, "当前URL"));
			sheet.addCell(new Label(5, 0, "创意名称"));
			int i=0;
			for(ReportAnalyzeBean bean:reportAnalyzeList){
				i++;
				sheet.addCell(new Label(0, i, sdf.format(bean.getClickTime())));
				sheet.addCell(new Label(1, i, bean.getCity()));
				sheet.addCell(new Label(2, i, bean.getIp()));
				sheet.addCell(new Label(3, i, bean.getUserId()));
				sheet.addCell(new Label(4, i, bean.getUrl()));
				sheet.addCell(new Label(5, i, bean.getCreativeName()));
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
		}
		
	}

	@Override
	public void clickExport(Long id, Long channelId, Date date, int startHour,
			int endHour, HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("channelId", channelId);
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
		List<ReportAnalyzeBean> reportAnalyzeList=  reportMapper.getClick_LogExport(map);
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			if(analyzeBean.getUrl()!=null){
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
			}
		}
		excelupload(reportAnalyzeList,response);
	}

	@Override
	public void conversionExport(Long accountId,Long conversionId, Long campaignId,
			Long channelId, Date date, int startHour, int endHour,
			HttpServletRequest request, HttpServletResponse response) {

		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<ReportAnalyzeBean> gridBean = new GridBean<ReportAnalyzeBean>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", accountId);
		if(conversionId!=null){map.put("conversionId","and ctl.cvt_id =" + conversionId);}
		if(campaignId!=null){map.put("campaignId", "and ctl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and ctl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
//		map.put("sidx", "ctl.req_time");
		List<ReportAnalyzeBean> reportAnalyzeList=  reportMapper.getConversion_LogExport(map);
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			long diff;
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			if(analyzeBean.getConversionTime()!=null&&analyzeBean.getClickTime()!=null){
				 diff = analyzeBean.getConversionTime().getTime() - analyzeBean.getClickTime().getTime();
				 Long days = diff / (1000 * 60 * 60 * 24);
				    Long hour=(diff/(60*60*1000)-days*24);
				    Long min=((diff/(60*1000))-days*24*60-hour*60);
				    analyzeBean.setGap(days+"天"+hour+"时"+min+"分");
			}else{ analyzeBean.setGap("");}
			if(analyzeBean.getUrl()!=null){
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
			}
			if(analyzeBean.getOther()!=null){
				analyzeBean.setOther(ServletUtils.decode(analyzeBean.getOther()));
			}
			String[] others = {};
			if(analyzeBean.getOther()!=null){
				 others = analyzeBean.getOther().split(",");
				}
			for(String other:others){
				String a = other.replace("<", "＜");
				String b = a.replace(">", "＞");
				String[] o = b.split("::");
				if(o.length>1){
				if(o[0].equals("p1")){analyzeBean.setPhonep(o[1]);}
				if(o[0].equals("p2")){analyzeBean.setPeopleNamep(o[1]);}
				if(o[0].equals("p3")){analyzeBean.setEmailp(o[1]);}
				if(o[0].equals("p4")){analyzeBean.setNotep(o[1]);}
				if(o[0].equals("p5")){analyzeBean.setSexp(o[1]);}
				if(o[0].equals("p6")){analyzeBean.setAgep(o[1]);}
				if(o[0].equals("p7")){analyzeBean.setCityp(o[1]);}
				if(o[0].equals("p8")){analyzeBean.setDizhip(o[1]);}
				if(o[0].equals("p9")){analyzeBean.setMoneyp(o[1]);}
				if(o[0].equals("p10")){analyzeBean.setCompanyNamep(o[1]);}
				if(o[0].equals("p11")){analyzeBean.setWebSitep(o[1]);}}
			}
		}
		exceluploadConversion(reportAnalyzeList,response);
	}
	private void exceluploadConversion(List<ReportAnalyzeBean> reportAnalyzeList, HttpServletResponse response) {
		SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = times.format(new Date());
		try {
			String name="转化分析"+time+".xls";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("sheet",0);
			sheet.addCell(new Label(0, 0, "时间"));
			sheet.addCell(new Label(1, 0, "城市"));
			sheet.addCell(new Label(2, 0, "IP"));
			sheet.addCell(new Label(3, 0, "用户ID"));
			sheet.addCell(new Label(4, 0, "到达时间"));
			sheet.addCell(new Label(5, 0, "到转间隔"));
			sheet.addCell(new Label(6, 0, "渠道"));
			sheet.addCell(new Label(7, 0, "来源URL"));
			sheet.addCell(new Label(8, 0, "活动名称"));
			sheet.addCell(new Label(9, 0, "创意名称"));
			sheet.addCell(new Label(10, 0, "转化名称"));
			int i=0;
			for(ReportAnalyzeBean bean:reportAnalyzeList){
				i++;
				sheet.addCell(new Label(0, i, sdf.format(bean.getConversionTime())));
				sheet.addCell(new Label(1, i, bean.getCity()));
				sheet.addCell(new Label(2, i, bean.getIp()));
				sheet.addCell(new Label(3, i, bean.getUserId()));
				if(bean.getClickTime()!=null){sheet.addCell(new Label(4, i,  sdf.format(bean.getClickTime())));}
				sheet.addCell(new Label(5, i, bean.getGap()));
				sheet.addCell(new Label(6, i, bean.getChannelName()));
				sheet.addCell(new Label(7, i, bean.getUrl()));
				sheet.addCell(new Label(8, i, bean.getCampaignName()));
				sheet.addCell(new Label(9, i, bean.getCreativeName()));
				sheet.addCell(new Label(10, i, bean.getConversionName()));
			}
			int j =0;
			int k =10;
			ReportAnalyzeBean analyzeBean = new ReportAnalyzeBean();
			int a = 1,b = 1,c = 1,d=1,e=1,f=1,g=1,h=1,m=1,n=1,p=1;
			for (ReportAnalyzeBean bean : reportAnalyzeList) {
				j++;
				if (bean.getPhonep() != null) {
					if(analyzeBean.getPhonep()==null){
						sheet.addCell(new Label(k, 0, "参数一"));
						analyzeBean.setPhonep(bean.getPhonep());
						k++;
						a = k;
						}
					sheet.addCell(new Label(a-1, j, bean.getPhonep().toString()));
				}
				if (bean.getPeopleNamep() != null) {
					if(analyzeBean.getPeopleNamep()==null){
						sheet.addCell(new Label(k, 0, "参数二"));
						analyzeBean.setPeopleNamep(bean.getPeopleNamep());
						k++;
						b=k;
						}
					sheet.addCell(new Label(b-1, j, bean.getPeopleNamep()));
				}
				if (bean.getEmailp() != null) {
					if(analyzeBean.getEmailp()==null){
						sheet.addCell(new Label(k, 0, "参数三"));
						analyzeBean.setEmailp(bean.getEmailp());
						k++;
						c=k;}
					sheet.addCell(new Label(c-1, j, bean.getEmailp()));
				}
				if (bean.getNotep() != null) {
					if(analyzeBean.getNotep()==null){
						sheet.addCell(new Label(k, 0, "参数四"));
						analyzeBean.setNotep(bean.getNotep());
						k++;
						d=k;}
					sheet.addCell(new Label(d-1, j, bean.getNotep()));
				}
				if (bean.getSexp() != null) {
					if(analyzeBean.getSexp()==null){
						sheet.addCell(new Label(k, 0, "参数五"));
						analyzeBean.setSexp(bean.getSexp());
						k++;
						e=k;}
					sheet.addCell(new Label(e-1, j, bean.getSexp()));
				}
				if (bean.getAgep() != null) {
					if(analyzeBean.getAgep()==null){
						sheet.addCell(new Label(k, 0, "参数六"));
						analyzeBean.setAgep(bean.getAgep());
						k++;
						f=k;}
					sheet.addCell(new Label(f-1, j, bean.getAgep()));
				}
				if (bean.getCityp() != null) {
					if(analyzeBean.getCityp()==null){
						sheet.addCell(new Label(k, 0, "参数七"));
						analyzeBean.setCityp(bean.getCityp());
						k++;
						g=k;}
					sheet.addCell(new Label(g-1, j, bean.getCityp()));
				}
				if (bean.getDizhip() != null) {
					if(analyzeBean.getDizhip()==null){
						sheet.addCell(new Label(k, 0, "参数八"));
						analyzeBean.setDizhip(bean.getDizhip());
						k++;
						h=k;}
					sheet.addCell(new Label(h-1, j, bean.getDizhip()));
				}
				if (bean.getMoneyp() != null) {
					if(analyzeBean.getMoneyp()==null){
						sheet.addCell(new Label(k, 0, "参数九"));
						analyzeBean.setMoneyp(bean.getMoneyp());
						k++;
						m=k;}
					sheet.addCell(new Label(m-1, j, bean.getMoneyp()));
				}
				if (bean.getCompanyNamep() != null) {
					if(analyzeBean.getCompanyNamep()==null){
						sheet.addCell(new Label(k, 0, "参数十"));
						analyzeBean.setCompanyNamep(bean.getCompanyNamep());
						k++;
						n=k;}
					sheet.addCell(new Label(n-1, j, bean.getCompanyNamep()));
				}
				if (bean.getWebSitep() != null) {
					if(analyzeBean.getWebSitep()==null){
						sheet.addCell(new Label(k, 0, "参数十一"));
						analyzeBean.setWebSitep(bean.getWebSitep());
						k++;
						p=k;
						}
					sheet.addCell(new Label(p-1, j, bean.getWebSitep()));
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
	}

	@Override
	public boolean isClickSoLong(Long id, Long channelId, Date date,
			int startHour, int endHour, HttpServletRequest request,
			HttpServletResponse response) {
		int records = reportMapper.getClick_LogRecords(id,channelId,date,startHour,endHour);
		if(records>20000){
			return false;
		}
		return true;
	}

	@Override
	public boolean isConversionSoLong(Long accountId,Long conversionId, Long campaignId,
			Long channelId, Date date, int startHour, int endHour,
			HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		map.put("accountId", accountId);
		if(conversionId!=null){map.put("conversionId", "and ctl.cvt_id = "+conversionId);}
		if(campaignId!=null){map.put("campaignId", "and ctl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and ctl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
		int records = reportMapper.getConversion_LogRecords(map);
		if(records>20000){
			return false;
		}
		return true;
	}

	@Override
	public List<ReportAnalyzeBean> cvtHasDataLists(Long accountId,Long conversionId, Long campaignId, Long channelId, Date date,
			int startHour, int endHour, HttpServletRequest request, HttpServletResponse response) {


		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", accountId);
		map.put("conversionId", conversionId);
		if(campaignId!=null){map.put("campaignId", "and ctl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and ctl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
//		map.put("sidx", "ctl.req_time");
		List<ReportAnalyzeBean> reportAnalyzeList=  reportMapper.getConversion_LogExport(map);
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			long diff;
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			if(analyzeBean.getConversionTime()!=null&&analyzeBean.getClickTime()!=null){
				 diff = analyzeBean.getConversionTime().getTime() - analyzeBean.getClickTime().getTime();
				 Long days = diff / (1000 * 60 * 60 * 24);
				    Long hour=(diff/(60*60*1000)-days*24);
				    Long min=((diff/(60*1000))-days*24*60-hour*60);
				    analyzeBean.setGap(days+"天"+hour+"时"+min+"分");
			}else{ analyzeBean.setGap("");}
			if(analyzeBean.getUrl()!=null){
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
			}
			if(analyzeBean.getOther()!=null){
				analyzeBean.setOther(ServletUtils.decode(analyzeBean.getOther()));
			}
			String[] others = {};
			if(analyzeBean.getOther()!=null){
				 others = analyzeBean.getOther().split(",");
				}
			for(String other:others){
				String[] o = other.split("::");
				if(o.length>1){
				if(o[0].equals("p1")){analyzeBean.setPhonep(o[1]);}
				if(o[0].equals("p2")){analyzeBean.setPeopleNamep(o[1]);}
				if(o[0].equals("p3")){analyzeBean.setEmailp(o[1]);}
				if(o[0].equals("p4")){analyzeBean.setNotep(o[1]);}
				if(o[0].equals("p5")){analyzeBean.setSexp(o[1]);}
				if(o[0].equals("p6")){analyzeBean.setAgep(o[1]);}
				if(o[0].equals("p7")){analyzeBean.setCityp(o[1]);}
				if(o[0].equals("p8")){analyzeBean.setDizhip(o[1]);}
				if(o[0].equals("p9")){analyzeBean.setMoneyp(o[1]);}
				if(o[0].equals("p10")){analyzeBean.setCompanyNamep(o[1]);}
				if(o[0].equals("p11")){analyzeBean.setWebSitep(o[1]);}}
			}
		}
	
		return reportAnalyzeList;
	}

	@Override
	public ReportAnalyzeBean cvtHasDataList(List<ReportAnalyzeBean> reportAnalyzeList) {
		ReportAnalyzeBean analyzeBean = new ReportAnalyzeBean();
		for (ReportAnalyzeBean bean : reportAnalyzeList) {
			if (bean.getPhonep() != null) {
				analyzeBean.setPhonep(bean.getPhonep());
			}
			if (bean.getPeopleNamep() != null) {
				analyzeBean.setPeopleNamep(bean.getPeopleNamep());
			}
			if (bean.getEmailp() != null) {
				analyzeBean.setEmailp(bean.getEmailp());
			}
			if (bean.getNotep() != null) {
				analyzeBean.setNotep(bean.getNotep());
			}
			if (bean.getSexp() != null) {
				analyzeBean.setSexp(bean.getSexp());
			}
			if (bean.getAgep() != null) {
				analyzeBean.setAgep(bean.getAgep());
			}
			if (bean.getCityp() != null) {
				analyzeBean.setCityp(bean.getCityp());
			}
			if (bean.getDizhip() != null) {
				analyzeBean.setDizhip(bean.getDizhip());
			}
			if (bean.getMoneyp() != null) {
				analyzeBean.setMoneyp(bean.getMoneyp());
			}
			if (bean.getCompanyNamep() != null) {
				analyzeBean.setCompanyNamep(bean.getCompanyNamep());
			}
			if (bean.getWebSitep() != null) {
				analyzeBean.setWebSitep(bean.getWebSitep());
			}
		}
		return analyzeBean;
	}

	@Override
	public List<ReportAnalyzeBean> clickHZuser(Long who,Long id, Long channelId, Date date, int startHour, int endHour,
			HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("channelId", channelId);
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
		List<ReportAnalyzeBean> reportAnalyzeList = null;
		if (who == 0) {
				reportAnalyzeList = reportMapper.clickHZuser(map);
		} else {
				reportAnalyzeList = reportMapper.clickHZip(map);
			
		}
		
		return reportAnalyzeList;
	}
	@Override
	public List<ReportAnalyzeBean> cvtHZ(Long accountId,Long conversionId, Long campaignId, Long channelId, Date date, int startHour,
			int endHour, HttpServletRequest request, HttpServletResponse response) {

		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", accountId);
		if(conversionId!=null){map.put("conversionId", "and ctl.cvt_id="+conversionId);}
		if(campaignId!=null){map.put("campaignId", "and ctl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and ctl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
//		map.put("sidx", "ctl.req_time");
		List<ReportAnalyzeBean> reportAnalyzeList=  reportMapper.cvtHZ(map);
		return reportAnalyzeList;
	}

	@Override
	public GridBean<ReportAnalyzeBean> visitLog(Long account,Long campaignId, Long channelId, Date date, int startHour, int endHour,
			Integer page, Integer rows, String sidx, String sord, HttpServletRequest request) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<ReportAnalyzeBean> gridBean = new GridBean<ReportAnalyzeBean>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		map.put("account", account);
		if(campaignId!=null){map.put("campaignId", "and rl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and rl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
		if(!sidx.equals("city")){
			map.put("sidx", sidx);
		}else{
			map.put("sidx", "visitTime");
		}
		if(sidx.equals("userId")){
			map.put("sidx", "user_id");
		}
		
		map.put("sord", sord);
		map.put("start", start);
		map.put("rowpage", rowpage);
		int records = 0;
			records= reportMapper.getVisit_LogRecords(map);
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		List<ReportAnalyzeBean> reportAnalyzeList = null;
			reportAnalyzeList=  reportMapper.getVisit_Log(map);
			
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			if (analyzeBean.getUrl() != null) {
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
				String a = analyzeBean.getUrl().replace("<", "＜");
				String b = a.replace(">", "＞");
				analyzeBean.setUrl(b);
				if (Tool.getCharEncode(analyzeBean.getUrl()).equals("UTF-8")) {
					analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
				} else {
					try {
						analyzeBean.setUrl(Tool.decode2(analyzeBean.getUrl()));
					} catch (Exception e) {
						System.out.println("url参数错误"+analyzeBean.getUrl());
					}
					
				}
				
			}
			if (analyzeBean.getNowUrl() != null) {
				analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				String a = analyzeBean.getNowUrl().replace("<", "＜");
				String b = a.replace(">", "＞");
				analyzeBean.setNowUrl(b);
				if (Tool.getCharEncode(analyzeBean.getNowUrl()).equals("UTF-8")) {
					analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				} else {
					try {
						analyzeBean.setNowUrl(Tool.decode2(analyzeBean.getNowUrl()));
					} catch (Exception e) {
						System.out.println("url参数错误"+analyzeBean.getNowUrl());
					}
					
				}
				
			}
		}
		if(!sidx.equals("city")){
			SortListUtil.sortList(reportAnalyzeList, sidx, sord);
		}
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(reportAnalyzeList);
		return gridBean;
	
	}

	@Override
	public void visitExport(Long account,Long campaignId, Long channelId, Date date, int startHour, int endHour,
			HttpServletRequest request, HttpServletResponse response) {

		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<ReportAnalyzeBean> gridBean = new GridBean<ReportAnalyzeBean>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("account", account);
		if(campaignId!=null){map.put("campaignId", "and rl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and rl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("startHour", startHour);
		map.put("endHour", endHour);
		List<ReportAnalyzeBean> reportAnalyzeList = null;
//			reportAnalyzeList=  reportMapper.getVisit_Log(map);
			reportAnalyzeList=  reportMapper.getVisit_LogExport(map);
			
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			try {
				if(analyzeBean.getUrl()!=null){
					analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
					String a = analyzeBean.getUrl().replace("<", "＜");
					String b = a.replace(">", "＞");
					analyzeBean.setUrl(b);
					analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
					
				}
			} catch (Exception e) {
				System.out.println(analyzeBean.getUrl());
			}
			if (analyzeBean.getNowUrl() != null) {
				analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				String a = analyzeBean.getNowUrl().replace("<", "＜");
				String b = a.replace(">", "＞");
				analyzeBean.setNowUrl(b);
				if (Tool.getCharEncode(analyzeBean.getNowUrl()).equals("UTF-8")) {
					analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				} else {
					try {
						analyzeBean.setNowUrl(Tool.decode2(analyzeBean.getNowUrl()));
					} catch (Exception e) {
						System.out.println("url参数错误"+analyzeBean.getNowUrl());
					}
					
				}
				
			}
		}
		exceluploadVisit(reportAnalyzeList,response);
	}

	private void exceluploadVisit(List<ReportAnalyzeBean> reportAnalyzeList, HttpServletResponse response) {
		SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = times.format(new Date());
		try {
			String name="到访分析"+time+".xls";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("sheet",0);
			CellView cellView = new CellView();  
			cellView.setAutosize(true); //设置自动大小  
			sheet.addCell(new Label(0, 0, "最后访问时间"));
			sheet.addCell(new Label(1, 0, "城市"));
			sheet.addCell(new Label(2, 0, "IP"));
			sheet.addCell(new Label(3, 0, "用户ID"));
			sheet.addCell(new Label(4, 0, "停留时长(当天)"));
			sheet.addCell(new Label(5, 0, "访问次数(7天)"));
			sheet.addCell(new Label(6, 0, "当前"));
			sheet.addCell(new Label(7, 0, "来源URL"));
			
			int i=0;
			for(ReportAnalyzeBean bean:reportAnalyzeList){
				i++;
				sheet.addCell(new Label(0, i, sdf.format(bean.getVisitTime())));
				sheet.addCell(new Label(1, i, bean.getCity()));
				sheet.addCell(new Label(2, i, bean.getIp()));
				sheet.addCell(new Label(3, i, bean.getUserId()));
				sheet.addCell(new Label(4, i, bean.getStopTime()));
				sheet.addCell(new jxl.write.Number(5, i, bean.getVisitNum()));
				sheet.addCell(new Label(6, i, bean.getNowUrl()));
				sheet.addCell(new Label(7, i, bean.getUrl()));
				
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
		}
		
	}

	@Override
	public GridBean<RptImpClick> rptImpClick(Long campaignId, Long channelId, Date startDate, Date endDate,Integer page, Integer rows, String sidx, String sord, HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		String starts = null;
		String end = null;
		if (startDate != null) {
			starts = format.format(startDate);
		}
		if (endDate != null) {
			end = format.format(endDate);
		}
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<RptImpClick> gridBean = new GridBean<RptImpClick>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		String sql = "";
		if (startDate != null && !"".equals(startDate)) {
			sql = sql + "and  day >='" + starts + "'";
		}
		if (endDate != null && !"".equals(endDate)) {
			sql = sql + "and DATEDIFF('" + end + "',day) >= 0";
		}
		map.put("sql", sql);
		map.put("campaignId", campaignId);
		map.put("channelId", channelId);
		map.put("sidx", "day");
		map.put("sord", sord);
		map.put("start", start);
		map.put("rowpage", rowpage);
		int records = 0;
			records= reportMapper.rptImpClickRecords(map);
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		List<RptImpClick> reportAnalyzeList = null;
		List<RptImpClick> reportAnalyzeList2 = null;
			reportAnalyzeList=  reportMapper.rptImpClick(map);
			reportAnalyzeList2 = reportMapper.rptImpClickNum(map);
			
		for(RptImpClick impClick:reportAnalyzeList){
			for(RptImpClick click :reportAnalyzeList2){
				if(impClick.getDay().equals(click.getDay())){
					impClick.setImp(click.getImp());
					impClick.setClick(click.getClick());
				}
			}
			if(impClick.getImpIpNum()!=0)
			impClick.setImpAveIp(impClick.getImp()/impClick.getImpIpNum());
			if(impClick.getImpUserNum()!=0)
			impClick.setImpAveUser(impClick.getImp()/impClick.getImpUserNum());
			if(impClick.getClickIpNum()!=0)
			impClick.setClickAveIp(impClick.getClick()/impClick.getClickIpNum());
			if(impClick.getClickUserNum()!=0)
			impClick.setClickAveUser(impClick.getClick()/impClick.getClickUserNum());
		}
		
		SortListUtil.sortList(reportAnalyzeList, sidx, sord);
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(reportAnalyzeList);
		return gridBean;
	}

	@Override
	public RptImpClick rptImpHZ(Long id,Long who) {
		RptImpClick clicks = null;
		if(who==1){
			 clicks = reportMapper.rptImpHZip(id);
		}else if (who==0){
			 clicks = reportMapper.rptImpHZuser(id);
		}
		
		return clicks;
	}

	@Override
	public void rptImpHZExport(Long id, Long channelId, Date startDate, Date endDate, HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		String starts = null;
		String end = null;
		if (startDate != null) {
			starts = format.format(startDate);
		}
		if (endDate != null) {
			end = format.format(endDate);
		}
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<RptImpClick> gridBean = new GridBean<RptImpClick>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String sql = "";
		if (startDate != null && !"".equals(startDate)) {
			sql = sql + "and  day >='" + starts + "'";
		}
		if (endDate != null && !"".equals(endDate)) {
			sql = sql + "and DATEDIFF('" + end + "',day) >= 0";
		}
		map.put("sql", sql);
		map.put("campaignId", id);
		map.put("channelId", channelId);
		List<RptImpClick> reportAnalyzeList = null;
		List<RptImpClick> reportAnalyzeList2 = null;
			reportAnalyzeList=  reportMapper.rptImpClickExport(map);
			reportAnalyzeList2 = reportMapper.rptImpClickNumExport(map);
			
		for(RptImpClick impClick:reportAnalyzeList){
			for(RptImpClick click :reportAnalyzeList2){
				if(impClick.getDay().getTime()==click.getDay().getTime()){
					impClick.setImp(click.getImp());
					impClick.setClick(click.getClick());
				}
			}
			if(impClick.getImpIpNum()!=0)
			impClick.setImpAveIp(impClick.getImp()/impClick.getImpIpNum());
			if(impClick.getImpUserNum()!=0)
			impClick.setImpAveUser(impClick.getImp()/impClick.getImpUserNum());
			if(impClick.getClickIpNum()!=0)
			impClick.setClickAveIp(impClick.getClick()/impClick.getClickIpNum());
			if(impClick.getClickUserNum()!=0)
			impClick.setClickAveUser(impClick.getClick()/impClick.getClickUserNum());
		}
		rptImpHZExportdon(reportAnalyzeList,response);
	}

	private void rptImpHZExportdon(List<RptImpClick> reportAnalyzeList, HttpServletResponse response) {

		SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = times.format(new Date());
		try {
			String name="频次分析"+time+".xls";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("sheet",0);
			CellView cellView = new CellView();  
			cellView.setAutosize(true); //设置自动大小  
			sheet.addCell(new Label(0, 0, "日期"));
			sheet.addCell(new Label(1, 0, "展示数"));
			sheet.addCell(new Label(2, 0, "展示人数"));
			sheet.addCell(new Label(3, 0, "展示ip数"));
			sheet.addCell(new Label(4, 0, "展示数/人数"));
			sheet.addCell(new Label(5, 0, "展示数/ip数"));
			sheet.addCell(new Label(6, 0, "点击数"));
			sheet.addCell(new Label(7, 0, "点击人数"));
			sheet.addCell(new Label(8, 0, "点击ip数"));
			sheet.addCell(new Label(9, 0, "点击数/人数"));
			sheet.addCell(new Label(10, 0, "点击数/ip数"));
			sheet.addCell(new Label(11, 0, "人群重合度"));
			sheet.addCell(new Label(12, 0, "ip重合度"));
			
			int i=0;
			for(RptImpClick bean:reportAnalyzeList){
				i++;
				sheet.addCell(new Label(0, i, sdf.format(bean.getDay())));
				sheet.addCell(new jxl.write.Number(1, i, bean.getImp()));
				sheet.addCell(new jxl.write.Number(2, i, bean.getImpUserNum()));
				sheet.addCell(new jxl.write.Number(3, i, bean.getImpIpNum()));
				sheet.addCell(new jxl.write.Number(4, i, bean.getImpAveUser()));
				sheet.addCell(new jxl.write.Number(5, i, bean.getImpAveIp()));
				sheet.addCell(new jxl.write.Number(6, i, bean.getClick()));
				sheet.addCell(new jxl.write.Number(7, i, bean.getClickUserNum()));
				sheet.addCell(new jxl.write.Number(8, i, bean.getClickIpNum()));
				sheet.addCell(new jxl.write.Number(9, i, bean.getClickAveUser()));
				sheet.addCell(new jxl.write.Number(10, i, bean.getClickAveIp()));
				sheet.addCell(new jxl.write.Number(11, i, bean.getUserContactRatio()));
				sheet.addCell(new jxl.write.Number(12, i, bean.getIpContactRatio()));
				
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
		}
		
	}

	@Override
	public ReportBean findDimension(Long accountId, String dimension, Long id, String cost_type, double unit_price,
			String level, Date startDate, Date endDate, HttpServletRequest request) {
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd");
		String starts = null;
		String end = null;
		if (startDate != null) {
			starts = times.format(startDate);
		}
		if (endDate != null) {
			end = times.format(endDate);
		}
//		int cpdDay= cpdDayCount(startDate,endDate,id);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "and campaign_id="+id.toString());
		String sql = "";
			if (startDate != null && !"".equals(startDate)) {
				sql = sql + "and  day >='" + starts + "'";
			}
			if (endDate != null && !"".equals(endDate)) {
				sql = sql + "and DATEDIFF('" + end + "',day) >= 0";
			}
		
		map.put("accountId", accountId.toString());
		map.put("sql", sql);
		ReportBean reportBean = new ReportBean();
		if ("渠道".equals(dimension)) {
			map.put("table", "rpt_ac_ca_ch");
			map.put("channel", "and channel_id !=0");
			 reportBean = reportMapper.findReportSum(map);
		}
		if ("创意".equals(dimension)) {
			map.put("table", "rpt_ac_ca_cr");
			map.put("creative", "and creative_id !=0");
			 reportBean = reportMapper.findReportSum(map);
		}
		if ("时段".equals(dimension)) {
			map.put("table", "rpt_hour");
			 reportBean = reportMapper.findReportSum(map);
		}
		if ("geo".equals(dimension)) {
				map.put("table", "rpt_geo");
				 reportBean = reportMapper.findReportSum(map);
		}
		
		return reportBean;
	}

	@Override
	public GridBean<ReportAnalyzeBean> visitLogInfo(String userId, Long id, Long campaignId, Long channelId, Date date,
			Integer page, Integer rows, String sidx, String sord, HttpServletRequest request) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		GridBean<ReportAnalyzeBean> gridBean = new GridBean<ReportAnalyzeBean>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		map.put("account", id);
		map.put("userId", "'"+userId+"'");
		if(campaignId!=null){map.put("campaignId", "and rl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and rl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		map.put("sidx", "req_time");
		map.put("sord", sord);
		map.put("start", start);
		map.put("rowpage", rowpage);
		int records = 0;
			records= reportMapper.getVisitLogInfoRecords(map);
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		List<ReportAnalyzeBean> reportAnalyzeList = null;
			reportAnalyzeList=  reportMapper.getVisitLogInfo(map);
			
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			if (analyzeBean.getUrl() != null) {
				analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
				String a = analyzeBean.getUrl().replace("<", "＜");
				String b = a.replace(">", "＞");
				analyzeBean.setUrl(b);
				if (Tool.getCharEncode(analyzeBean.getUrl()).equals("UTF-8")) {
					analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
				} else {
					try {
						analyzeBean.setUrl(Tool.decode2(analyzeBean.getUrl()));
					} catch (Exception e) {
						System.out.println("url参数错误"+analyzeBean.getUrl());
					}
					
				}
				
			}
			if (analyzeBean.getNowUrl() != null) {
				analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				String a = analyzeBean.getNowUrl().replace("<", "＜");
				String b = a.replace(">", "＞");
				analyzeBean.setNowUrl(b);
				if (Tool.getCharEncode(analyzeBean.getNowUrl()).equals("UTF-8")) {
					analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				} else {
					try {
						analyzeBean.setNowUrl(Tool.decode2(analyzeBean.getNowUrl()));
					} catch (Exception e) {
						System.out.println("url参数错误"+analyzeBean.getNowUrl());
					}
					
				}
				
			}
		}
		SortListUtil.sortList(reportAnalyzeList, sidx, sord);
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(reportAnalyzeList);
		return gridBean;
	
	}

	@Override
	public List<ReportAnalyzeBean> visitDateInfo(String userId, Long id, Long campaignId, Long channelId, Date date,
			Integer page, Integer rows, String sidx, String sord, HttpServletRequest request) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("account", id);
		map.put("userId", "'"+userId+"'");
		if(campaignId!=null){map.put("campaignId", "and rl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and rl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		List<ReportAnalyzeBean> reportAnalyzeList = null;
		reportAnalyzeList=  reportMapper.visitDateInfo(map);
		return reportAnalyzeList;
	}

	@Override
	public void visitInfoExport(String userId, Long id, Long campaignId, Long channelId, Date date, Integer page,
			Integer rows, String sidx, String sord, HttpServletRequest request,HttpServletResponse response) {

		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		 HashMap<String, List> geoMap = null;
		 geoMap = (HashMap<String, List>) request.getSession().getServletContext()
				.getAttribute("geoList");
		if (geoMap == null) {
			ReadCSV test = null;
			try {
				String cpt = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/resources/geo.csv");
				test = new ReadCSV(cpt);
				geoMap = test.readCSVFile();
				request.getSession().getServletContext()
						.setAttribute("geoMap", geoMap);
			} catch (Exception e) {
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("account", id);
		map.put("userId", "'"+userId+"'");
		if(campaignId!=null){map.put("campaignId", "and rl.campaign_id="+campaignId);}
		if(channelId!=null){map.put("channelId", "and rl.channel_id="+channelId);}
		map.put("date","'"+format.format(date)+"'");
		List<ReportAnalyzeBean> reportAnalyzeList = null;
		reportAnalyzeList=  reportMapper.getVisitLogInfoExport(map);
		for(ReportAnalyzeBean analyzeBean:reportAnalyzeList){
			if(analyzeBean.getGeo()!=null){
				try {
					analyzeBean.setCity(geoMap.get(analyzeBean.getGeo().toString()).get(2).toString());
				} catch (Exception e) {
					analyzeBean.setCity("");
				}
			}
			try {
				if(analyzeBean.getUrl()!=null){
					analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
					String a = analyzeBean.getUrl().replace("<", "＜");
					String b = a.replace(">", "＞");
					analyzeBean.setUrl(b);
					analyzeBean.setUrl(ServletUtils.decode(analyzeBean.getUrl()));
					
				}
			} catch (Exception e) {
				System.out.println(analyzeBean.getUrl());
			}
			if (analyzeBean.getNowUrl() != null) {
				analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				String a = analyzeBean.getNowUrl().replace("<", "＜");
				String b = a.replace(">", "＞");
				analyzeBean.setNowUrl(b);
				if (Tool.getCharEncode(analyzeBean.getNowUrl()).equals("UTF-8")) {
					analyzeBean.setNowUrl(ServletUtils.decode(analyzeBean.getNowUrl()));
				} else {
					try {
						analyzeBean.setNowUrl(Tool.decode2(analyzeBean.getNowUrl()));
					} catch (Exception e) {
						System.out.println("url参数错误"+analyzeBean.getNowUrl());
					}
					
				}
				
			}
		}
		visitInfoExport(reportAnalyzeList,userId,response);
	}

	private void visitInfoExport(List<ReportAnalyzeBean> reportAnalyzeList,String userId, HttpServletResponse response) {

		SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = times.format(new Date());
		try {
			String name=userId+"用户行为"+time+".xls";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("sheet",0);
			CellView cellView = new CellView();  
			cellView.setAutosize(true); //设置自动大小  
			sheet.addCell(new Label(0, 0, "时间"));
			sheet.addCell(new Label(1, 0, "城市"));
			sheet.addCell(new Label(2, 0, "IP"));
			sheet.addCell(new Label(3, 0, "渠道"));
			sheet.addCell(new Label(4, 0, "活动"));
			sheet.addCell(new Label(5, 0, "创意"));
			sheet.addCell(new Label(6, 0, "当前URL"));
			sheet.addCell(new Label(7, 0, "来源URL"));
			
			int i=0;
			for(ReportAnalyzeBean bean:reportAnalyzeList){
				i++;
				sheet.addCell(new Label(0, i, sdf.format(bean.getVisitTime())));
				sheet.addCell(new Label(1, i, bean.getCity()));
				sheet.addCell(new Label(2, i, bean.getIp()));
				sheet.addCell(new Label(3, i, bean.getChannelName()));
				sheet.addCell(new Label(4, i, bean.getCampaignName()));
				sheet.addCell(new Label(5, i, bean.getCreativeName()));
				sheet.addCell(new Label(6, i, bean.getNowUrl()));
				sheet.addCell(new Label(7, i, bean.getUrl()));
				
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
		}
		
	}

	@Override
	public List<RptImpClick> impInfo(Long campaignId, Long channelId, Long creativeId, Date date,long who) {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("campaignId", "campaign_id="+campaignId);
		if(channelId!=null&&creativeId==null){
			map.put("channelId", "and channel_id="+channelId);
			map.put("table", "rpt_ac_ca_ch");
		}
		if(creativeId!=null&&channelId==null){
			map.put("creativeId", "and creative_id="+creativeId);
			map.put("table", "rpt_ac_ca_cr");
		}
		if(creativeId!=null&&channelId!=null){
			map.put("channelId", "and channel_id="+channelId);
			map.put("creativeId", "and creative_id="+creativeId);
			map.put("table", "rpt_base");
		}
		if(creativeId==null&&channelId==null){
			map.put("table", "rpt_ac_ca");
		}
		map.put("date","'"+format.format(date)+"'");
		List<RptImpClick> list = new ArrayList<>();
		RptImpClick imp;
		if(who==0){
			map.put("select", "ip_num as ipUserNum");
			imp =reportMapper.impInfo(map);
		}else{
			map.put("select", "user_num as ipUserNum");
			imp =reportMapper.impInfo(map);
		}
		if(imp!=null&&!"".equals(imp.getIpUserNum())){
			String[] str = imp.getIpUserNum().split(",");
			for(String s: str){
				RptImpClick impClick = new RptImpClick();
				String[] st = s.split(":");
				impClick.setImpNum(Long.valueOf(st[0]));
				impClick.setNum(Long.valueOf(st[1]));
				list.add(impClick);
			}
		}
		
		return list;
	}
	
}
	