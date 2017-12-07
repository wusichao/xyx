package com.xyx.ls.dao.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyx.ls.bean.ReportAnalyzeBean;
import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.bean.RptImpClick;

public interface ReportMapper {

	List<ReportBean> findConvertionData(@Param("id")Long id,@Param("channelIds") String channelIds,
			@Param("creativeIds")String creativeIds,@Param("startDate")Date startDate,@Param("date") Date date);

	ReportBean findConvertionData(HashMap<String, String> map);

	int getRecords(HashMap<String, String> map);

	List<ReportBean> findReportGrid(HashMap<String, String> map);

	List<ReportBean> findDimensionRank(HashMap<String, String> map);

	List<ReportBean> findDimensionGeoRank(HashMap<String, String> map);

	List<ReportBean> findDimensionHourRank(HashMap<String, String> map);

	int getClick_LogRecords(Long id, Long channelId, Date date, int startHour,
			int endHour);

	List<ReportAnalyzeBean> getClick_Log(HashMap<String, Object> map);

	int getConversion_LogRecords(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getConversion_Log(HashMap<String, Object> map);

	Date findClickTimeByConversionTime(String actionPreId);

	String findConversionUrl(String actionPreId);

	List<ReportAnalyzeBean> getClick_LogExport(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getConversion_LogExport(HashMap<String, Object> map);

	List<ReportAnalyzeBean> clickHZuser(HashMap<String, Object> map);

	List<ReportAnalyzeBean> clickHZip(HashMap<String, Object> map);

	List<ReportAnalyzeBean> cvtHZ(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getAllClickByClickNumUserId(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getAllClickByClickNumIp(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getAllCvtBycvtgap(HashMap<String, Object> map);

	int getAllCvtBycvtgapRecords(HashMap<String, Object> map);

	int getAllClickByClickNumUserIdRecords(HashMap<String, Object> map);

	int getAllClickByClickNumIpRecords(HashMap<String, Object> map);

	int getVisit_LogRecords(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getVisit_Log(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getVisit_LogExport(HashMap<String, Object> map);

	int getCpdDay(HashMap<String, String> map);

	int rptImpClickRecords(HashMap<String, Object> map);

	List<RptImpClick> rptImpClick(HashMap<String, Object> map);

	List<RptImpClick> rptImpClickNum(HashMap<String, Object> map);

	RptImpClick rptImpHZip(Long id);

	RptImpClick rptImpHZuser(Long id);

	List<RptImpClick> rptImpClickExport(HashMap<String, Object> map);

	List<RptImpClick> rptImpClickNumExport(HashMap<String, Object> map);

	ReportBean findReachConvertion(HashMap<String, String> map);

	List<ReportBean> findReachConversionByDay(HashMap<String, String> map);

	List<ReportBean> findDimensionHourRankNoCampaign(HashMap<String, String> map);

	List<ReportBean> findDimensionGeoRankNoCampaign(HashMap<String, String> map);

	List<ReportBean> findDimensionGeoRankPro(HashMap<String, String> map);

	List<ReportBean> findReportChannelGrid(HashMap<String, String> map);

	List<ReportBean> findReportCreativeGrid(HashMap<String, String> map);

	List<ReportBean> findReportCampaignGrid(HashMap<String, String> map);

	ReportBean findConvertionChannelData(HashMap<String, String> map);

	ReportBean findConvertionCreativeData(HashMap<String, String> map);

	ReportBean findConvertionCampaignData(HashMap<String, String> map);

	List<ReportBean> findDimensionCreativeRank(HashMap<String, String> map);

	List<ReportBean> findReportAllGrid(HashMap<String, String> map);

	ReportBean findConverAlltionData(HashMap<String, String> map);

	ReportBean findReportAll(HashMap<String, String> map);

	ReportBean findReport(HashMap<String, String> map);

	ReportBean findReportChannel(HashMap<String, String> map);

	ReportBean findReportCreative(HashMap<String, String> map);

	ReportBean findReportCampaign(HashMap<String, String> map);

	ReportBean findReportSum(HashMap<String, String> map);

	int getVisitLogInfoRecords(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getVisitLogInfo(HashMap<String, Object> map);

	List<ReportAnalyzeBean> visitDateInfo(HashMap<String, Object> map);

	List<ReportAnalyzeBean> getVisitLogInfoExport(HashMap<String, Object> map);

	RptImpClick impInfo(HashMap<String, Object> map);

}
