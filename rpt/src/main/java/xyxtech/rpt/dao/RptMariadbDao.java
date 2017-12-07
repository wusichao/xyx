package xyxtech.rpt.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import xyxtech.rpt.data.RptBase;
import xyxtech.rpt.data.RptChannel;
import xyxtech.rpt.data.RptChannelCreative;
import xyxtech.rpt.data.RptCreative;
import xyxtech.rpt.data.RptGeo;
import xyxtech.rpt.data.RptHour;
import xyxtech.rpt.data.RptMedia;

public class RptMariadbDao extends JdbcDaoSupport implements RptDao {

	// TODO 四个方法待重构
	private final String SQL_RPT_CHANNEL_CREATIVE = "insert into rpt_base("
			+ " campaign_id,channel_id,creative_id,day,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values"
			+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" + " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";

	private final String SQL_INSERT_RPT_HOUR = "insert into rpt_hour("
			+ " campaign_id,day,hour,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values" + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			+ " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";

	private final String SQL_INSERT_PRT_MEDIA = "insert into rpt_media("
			+ " campaign_id,day,media_id,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values" + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			+ " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";

	private final String SQL_INSERT_PRT_GEO = "insert into rpt_geo("
			+ " campaign_id,day,geo_id,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values" + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
			+ " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";
	
	private final String SQL_RPT = "insert into rpt_ac_ca("
			+ " campaign_id,day,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values"
			+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" + " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),"
			+ " imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";
	
	private final String SQL_RPT_CHANNEL= "insert into rpt_ac_ca_ch("
			+ " campaign_id,channel_id,day,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values"
			+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" + " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),"
			+ " imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";
	private final String SQL_RPT_CREATIVE= "insert into rpt_ac_ca_cr("
			+ " campaign_id,creative_id,day,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values"
			+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" + " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),"
			+ " imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";
	private final String SQL_RPT_ACCOUNT= "insert into rpt_ac("
			+ " day,last_modified,imp,click,reach,conversion,visit,account_id,imp_ip,click_ip,reach_ip,visit_ip,conversion_ip,imp_user,click_user,reach_user,visit_user,conversion_user) values"
			+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" + " on DUPLICATE key update imp=imp+values(imp),click=click+values(click),"
			+ " reach=reach+values(reach),conversion=conversion+values(conversion),"
			+ " imp_ip=imp_ip+values(imp_ip),click_ip=click_ip+values(click_ip),reach_ip=reach_ip+values(reach_ip),visit_ip=visit_ip+values(visit_ip),conversion_ip=conversion_ip+values(conversion_ip),imp_user=imp_user+values(imp_user),click_user=click_user+values(click_user),reach_user=reach_user+values(reach_user),visit_user=visit_user+values(visit_user),conversion_user=conversion_user+values(conversion_user),last_modified=values(last_modified),visit=visit+values(visit)";
	@Override
	public void saveRptCcs(List<RptChannelCreative> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_RPT_CHANNEL_CREATIVE, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptChannelCreative rpt = list.get(i);
					int index = 1;
					ps.setLong(index++, rpt.getCampaignId());
					ps.setLong(index++, rpt.getChannelId());
					ps.setLong(index++, rpt.getCreativeId());
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getVisit());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
					logger.info("写入一条rpt_base："+rpt.getAccountId()+"|"+rpt.getCampaignId()+"|"+rpt.getChannelId()+"|"+rpt.getCreativeId()+"|"+new java.sql.Date(rpt.getDay().getTime())+"|"+rpt.getImp()+"|"+rpt.getClick()+"|"+rpt.getReach()+"|"+rpt.getVisit()+"|"+rpt.getConversion());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_base时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

	@Override
	public void saveRptHour(List<RptHour> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_INSERT_RPT_HOUR, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptHour rpt = list.get(i);
					int index = 1;
					ps.setLong(index++, rpt.getCampaignId());
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setInt(index++, rpt.getHour());
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getVisit());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
					//logger.info("写入一条rpt_hour："+rpt.getCampaignId()+"|"+rpt.getHour()+"|"+new java.sql.Date(rpt.getDay().getTime())+"|"+rpt.getImp()+"|"+rpt.getClick()+"|"+rpt.getReach()+"|"+rpt.getConversion()+"|"+rpt.getVisit());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_hour时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

	@Override
	public void saveRptMedia(List<RptMedia> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_INSERT_PRT_MEDIA, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptMedia rpt = list.get(i);
					int index = 1;
					ps.setLong(index++, rpt.getCampaignId());
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setLong(index++, rpt.getMediaId());
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_media时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

	@Override
	public void saveRptGeo(List<RptGeo> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_INSERT_PRT_GEO, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptGeo rpt = list.get(i);
					int index = 1;
					ps.setLong(index++, rpt.getCampaignId());
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setLong(index++, rpt.getGeoId());
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getVisit());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
					//logger.info("写入一条rpt_geo："+rpt.getCampaignId()+"|"+rpt.getGeoId()+"|"+new java.sql.Date(rpt.getDay().getTime())+"|"+rpt.getImp()+"|"+rpt.getClick()+"|"+rpt.getReach()+"|"+rpt.getConversion()+"|"+rpt.getVisit());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_geo时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

	@Override
	public void saveRptAc(List<RptBase> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_RPT, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptBase rpt = list.get(i);
					int index = 1;
					ps.setLong(index++, rpt.getCampaignId());
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getVisit());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
//					logger.info("写入一条rpt_ac_ca："+rpt.getAccountId()+"|"+rpt.getCampaignId()+"|"+new java.sql.Date(rpt.getDay().getTime())+"|"+rpt.getImp()+"|"+rpt.getClick()+"|"+rpt.getReach()+"|"+rpt.getConversion()+"|"+rpt.getVisit()+"|"+rpt.getImpUser());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_ac_ca时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

	@Override
	public void saveRptAcc(List<RptChannel> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_RPT_CHANNEL, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptChannel rpt = list.get(i);
					int index = 1;
					ps.setLong(index++, rpt.getCampaignId());
					ps.setLong(index++, rpt.getChannelId());
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getVisit());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
//					logger.info("写入一条rpt_ac_ca_ch："+rpt.getAccountId()+"|"+rpt.getCampaignId()+"|"+rpt.getChannelId()+"|"+new java.sql.Date(rpt.getDay().getTime())+"|"+rpt.getImp()+"|"+rpt.getClick()+"|"+rpt.getReach()+"|"+rpt.getConversion()+"|"+rpt.getVisit()+"|"+rpt.getImpUser());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_ac_ca_ch时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

	@Override
	public void saveRptAce(List<RptCreative> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_RPT_CREATIVE, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptCreative rpt = list.get(i);
					int index = 1;
					ps.setLong(index++, rpt.getCampaignId());
					ps.setLong(index++, rpt.getCreativeId());
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getVisit());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
//					logger.info("写入一条rpt_ac_ca_ce："+rpt.getAccountId()+"|"+rpt.getCampaignId()+"|"+rpt.getCreativeId()+"|"+new java.sql.Date(rpt.getDay().getTime())+"|"+rpt.getImp()+"|"+rpt.getClick()+"|"+rpt.getReach()+"|"+rpt.getConversion()+"|"+rpt.getVisit()+"|"+rpt.getImpUser());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_ac_ca_ce时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

	@Override
	public void saveRptA(List<RptBase> list) {

		if(list == null || list.isEmpty()) return;
		
		try{
			getJdbcTemplate().batchUpdate(SQL_RPT_ACCOUNT, new BatchPreparedStatementSetter() {
	
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					RptBase rpt = list.get(i);
					int index = 1;
					ps.setDate(index++, new java.sql.Date(rpt.getDay().getTime()));
					ps.setTimestamp(index++, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setLong(index++, rpt.getImp());
					ps.setLong(index++, rpt.getClick());
					ps.setLong(index++, rpt.getReach());
					ps.setLong(index++, rpt.getConversion());
					ps.setLong(index++, rpt.getVisit());
					ps.setLong(index++, rpt.getAccountId());
					ps.setLong(index++, rpt.getImpIp());
					ps.setLong(index++, rpt.getClickIp());
					ps.setLong(index++, rpt.getReachIp());
					ps.setLong(index++, rpt.getVisitIp());
					ps.setLong(index++, rpt.getConversionIp());
					ps.setLong(index++, rpt.getImpUser());
					ps.setLong(index++, rpt.getClickUser());
					ps.setLong(index++, rpt.getReachUser());
					ps.setLong(index++, rpt.getVisitUser());
					ps.setLong(index++, rpt.getConversionUser());
//					logger.info("写入一条rpt_ac："+rpt.getAccountId()+"|"+rpt.getCampaignId()+"|"+rpt.getCreativeId()+"|"+new java.sql.Date(rpt.getDay().getTime())+"|"+rpt.getImp()+"|"+rpt.getClick()+"|"+rpt.getReach()+"|"+rpt.getConversion()+"|"+rpt.getVisit()+"|"+rpt.getImpUser());
				}
	
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		}catch(Exception e){
			logger.error("写入数据表rpt_ac时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
		}
	}

}
