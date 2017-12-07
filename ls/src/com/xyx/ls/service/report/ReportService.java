package com.xyx.ls.service.report;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyx.ls.bean.GridBean;
import com.xyx.ls.bean.ReportAnalyzeBean;
import com.xyx.ls.bean.ReportBean;
import com.xyx.ls.bean.RptImpClick;
import com.xyx.ls.model.campaign.OperateLog;

public interface ReportService {

	ReportBean findConvertionData(Long id, Long long1, String channelIds,
			String creativeIds, Date startDate, Date endDate, String string, double d);

	GridBean<ReportBean> findReportGrid(Long accountId,Long id, String channelIds, String creativeIds, Date startDate, Date endDate, int page, int rows, String sidx, String sord, String string, double d);

	List<ReportBean> findDimensionRank(Long long1, String dimension, Long id,
			String string, double d, String level, Date startDate, Date endDate, HttpServletRequest request);

	List<ReportBean> findExportExcel(Long id, Long long1, String channelIds,
			String creativeIds, String cost_type, double unit_price,
			Date startDate, Date endDate);

	void exportExcel(Integer integer, String name,Date startDate, Date endDate, String who, List<ReportBean> reportList, HttpServletResponse response);

	GridBean<ReportBean> jointGrid(int page, int rows, String sidx,
			String sord, int i, List<ReportBean> reportList);

	GridBean<ReportAnalyzeBean> getClick_Log(Long who,Integer clickNum ,Long id, Long channelId, Date date,
			int startHour, int endHour, Integer page, Integer rows,
			String sidx, String sord,HttpServletRequest request, HttpServletResponse response);

	GridBean<ReportAnalyzeBean> getConversion_Log(Long long1, Integer gap,Long conversionId,Long id, Long channelId,
			Date date, int startHour, int endHour, Integer page, Integer rows,
			String sidx, String sord, HttpServletRequest request);

	void clickExport(Long id, Long channelId, Date date, int startHour,
			int endHour, HttpServletRequest request,
			HttpServletResponse response);

	void conversionExport(Long conversionId, Long campaignId, Long channelId,
			Long channelId2, Date date, int startHour, int endHour, HttpServletRequest request, HttpServletResponse response);

	boolean isClickSoLong(Long id, Long channelId, Date date, int startHour,
			int endHour, HttpServletRequest request,
			HttpServletResponse response);

	boolean isConversionSoLong(Long conversionId, Long campaignId,
			Long channelId, Long channelId2, Date date, int startHour, int endHour,
			HttpServletRequest request, HttpServletResponse response);

	List<ReportAnalyzeBean> cvtHasDataLists(Long conversionId, Long campaignId, Long channelId, Long channelId2, Date date, int startHour,
			int endHour, HttpServletRequest request, HttpServletResponse response);

	ReportAnalyzeBean cvtHasDataList(List<ReportAnalyzeBean> reportAnalyzeList);

	List<ReportAnalyzeBean> clickHZuser(Long who,Long id, Long channelId, Date date, int startHour, int endHour,
			HttpServletRequest request, HttpServletResponse response);

	List<ReportAnalyzeBean> cvtHZ(Long conversionId, Long campaignId, Long channelId, Long channelId2, Date date, int startHour,
			int endHour, HttpServletRequest request, HttpServletResponse response);

	GridBean<ReportAnalyzeBean> visitLog(Long campaignId, Long channelId, Long channelId2, Date date, int startHour, int endHour,
			Integer page, Integer rows, String sidx, String sord, HttpServletRequest request);

	void visitExport(Long campaignId, Long channelId, Long channelId2, Date date, int startHour, int endHour, HttpServletRequest request,
			HttpServletResponse response);

	GridBean<RptImpClick> rptImpClick(Long campaignId, Long channelId, Date date, Date endDate, Integer page, Integer rows, String sidx, String sord, HttpServletRequest request,
			HttpServletResponse response);

	RptImpClick rptImpHZ(Long id,Long who);

	void rptImpHZExport(Long id, Long channelId, Date startDate, Date endDate, HttpServletRequest request,
			HttpServletResponse response);

	ReportBean findDimension(Long id, String dimension, Long id2, String cost_type, double unit_price, String level,
			Date startDate, Date endDate, HttpServletRequest request);

	GridBean<ReportAnalyzeBean> visitLogInfo(String userId, Long id, Long campaignId, Long channelId, Date date,
			Integer page, Integer rows, String sidx, String sord, HttpServletRequest request);

	List<ReportAnalyzeBean> visitDateInfo(String userId, Long id, Long campaignId, Long channelId, Date date,
			Integer page, Integer rows, String sidx, String sord, HttpServletRequest request);

	void visitInfoExport(String userId, Long id, Long campaignId, Long channelId, Date date, Integer page, Integer rows,
			String sidx, String sord, HttpServletRequest request, HttpServletResponse response);

	List<RptImpClick> impInfo(Long campaignId, Long channelId, Long creativeId, Date date,long who);


}
