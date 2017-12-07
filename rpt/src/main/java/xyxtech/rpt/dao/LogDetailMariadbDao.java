package xyxtech.rpt.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import xyxtech.rpt.data.LogField;

public class LogDetailMariadbDao extends JdbcDaoSupport implements LogDetailDao {
	
	private static final Logger logger = LoggerFactory.getLogger(LogDetailMariadbDao.class);
	
	private String SQL_INSERT_CLICK = "INSERT INTO click_log "
			+ " (action_id, action_pre_id, action_type, req_time, user_id,"
			+ " account_id, campaign_id, channel_id, creative_id, media_id, geo_id ,ip ,url)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private String SQL_INSERT_CVT = "INSERT INTO convertion_log "
			+ " (action_id, action_pre_id, action_type, req_time, user_id, "
			+ " account_id, campaign_id, channel_id, creative_id, media_id, cvt_id, geo_id, ip,other)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
	
	private String SQL_INSERT_REACH = "INSERT INTO reach_log "
			+ " (action_id, action_pre_id, action_type, req_time, user_id,"
			+ " account_id, campaign_id, channel_id, creative_id, media_id, geo_id ,ip, referer,url)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	@Override
	public void saveClickLogDetail(List<LogField> list) {
		
		if(list!=null && list.size()>0){
			
			try{
				getJdbcTemplate().batchUpdate(SQL_INSERT_CLICK, new BatchPreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						LogField log = list.get(i);
						int index=1;
						ps.setString(index++, log.getActionId());
						ps.setString(index++, log.getActionPreId());
						ps.setString(index++, log.getActionType().toString());
						ps.setTimestamp(index++, new java.sql.Timestamp(log.getRequestTime().getTime()));
						ps.setString(index++, log.getUserId());
						ps.setLong(index++, log.getAccountId());
						ps.setLong(index++, log.getCampaignId());
						ps.setLong(index++, log.getChannelId());
						ps.setLong(index++, log.getCreativeId());
						ps.setLong(index++, log.getMediaId());;
						ps.setLong(index++, log.getGeoId());;
						ps.setString(index++, log.getIp());
						ps.setString(index++, log.getReferer());
						//logger.info("写入一条click数据:"+log.getActionId()+"|"+log.getUserId());
					}
					
					@Override
					public int getBatchSize() {
						return list.size();
					}
				});
			}catch(DataAccessException e){
				SQLException sqle = (SQLException) e.getCause(); 
				logger.error("点击记录写入数据库时失败,异常为："+sqle.getErrorCode()+"|"+sqle.getSQLState());
			}
		}
		
	}
	
	@Override
	public void saveCvtLogDetail(List<LogField> list) {
		if(list!=null && list.size()>0){
			try{
				getJdbcTemplate().batchUpdate(SQL_INSERT_CVT, new BatchPreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						LogField log = list.get(i);
						int index=1;
						ps.setString(index++, log.getActionId());
						ps.setString(index++, log.getActionPreId());
						ps.setString(index++, log.getActionType().toString());
						ps.setTimestamp(index++, new java.sql.Timestamp(log.getRequestTime().getTime()));
						ps.setString(index++, log.getUserId());
						ps.setLong(index++, log.getAccountId());
						ps.setLong(index++, log.getCampaignId());
						ps.setLong(index++, log.getChannelId());
						ps.setLong(index++, log.getCreativeId());
						ps.setLong(index++, log.getMediaId());;
						ps.setLong(index++, log.getCvtId());
						ps.setLong(index++, log.getGeoId());
						ps.setString(index++, log.getIp());
						ps.setString(index++, log.getOther());
						//logger.info("写入一条cvt数据:"+log.getActionId()+"|"+log.getUserId());
					}
					
					@Override
					public int getBatchSize() {
						return list.size();
					}
				});
			}catch(DataAccessException e){
				SQLException sqle = (SQLException) e.getCause(); 
				logger.error("点击记录写入数据库时失败,异常为："+sqle.getErrorCode()+"|"+sqle.getSQLState());
			}
		}
	}

	@Override
	public void saveReachLogDetail(List<LogField> list) {
		
		if(list!=null && list.size()>0){
			
			try{
				getJdbcTemplate().batchUpdate(SQL_INSERT_REACH, new BatchPreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						LogField log = list.get(i);
						int index=1;
						ps.setString(index++, log.getActionId());
						ps.setString(index++, log.getActionPreId());
						ps.setString(index++, log.getActionType().toString());
						ps.setTimestamp(index++, new java.sql.Timestamp(log.getRequestTime().getTime()));
						ps.setString(index++, log.getUserId());
						ps.setLong(index++, log.getAccountId());
						ps.setLong(index++, log.getCampaignId());
						ps.setLong(index++, log.getChannelId());
						ps.setLong(index++, log.getCreativeId());
						ps.setLong(index++, log.getMediaId());;
						ps.setLong(index++, log.getGeoId());;
						ps.setString(index++, log.getIp());
						ps.setString(index++, log.getReferer());
						ps.setString(index++, log.getUrl());
						//logger.info("写入一条reach数据:"+log.getActionId()+"|"+log.getUserId()+"|"+log.getActionType().toString()+"|"+log.getAccountId()+"|"+log.getCampaignId()+"|"+log.getChannelId()+"|"+log.getReferer()+"|"+log.getUrl());
					}
					
					@Override
					public int getBatchSize() {
						return list.size();
					}
				});
			}catch(DataAccessException e){
				SQLException sqle = (SQLException) e.getCause(); 
				logger.error("reach记录写入数据库时失败,异常为："+sqle.getErrorCode()+"|"+sqle.getSQLState());
			}
		}
		
	
	}

}
