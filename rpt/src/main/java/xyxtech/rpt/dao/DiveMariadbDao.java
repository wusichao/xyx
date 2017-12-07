package xyxtech.rpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import xyxtech.rpt.data.LogField;
import xyxtech.rpt.utils.DateUtils;

public class DiveMariadbDao extends JdbcDaoSupport implements DiveDao {

	private String SQL_QUERY_DIVE_CLICK = "select action_id,req_time,campaign_id ,channel_id,creative_id,media_id "
			+ "from click_log where req_time between ? and ? and user_id=? and campaign_id in"
			+"(select rel.campaign_id from campaign_convertion_rel rel "
			+ "where rel.convertion_id=? ) order by req_time desc limit 1";
	
	private int convertDays = 15;

	@Override
	public void findClickLog(List<LogField> logs) {
		if (logs != null && logs.size() > 0) {
			for (LogField log : logs) {
				try{
					this.getJdbcTemplate().query(SQL_QUERY_DIVE_CLICK,
							new Object[] { new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.afterDate(log.getRequestTime(),-convertDays)),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getRequestTime()), log.getUserId(),log.getCvtId() }, new RowMapper<LogField>() {
	
								@Override
								public LogField mapRow(ResultSet rs, int rowNum) throws SQLException {
									log.setActionPreId(rs.getString("action_id"));
									log.setCampaignId(rs.getLong("campaign_id"));
									log.setChannelId(rs.getLong("channel_id"));
									log.setCreativeId(rs.getLong("creative_id"));
									log.setMediaId(rs.getLong("media_id"));
									log.setClickTime(new java.util.Date(rs.getTimestamp("req_time").getTime()));
									
									return log;
								}
	
					});
				}catch(Exception e){
					logger.error("从数据库查询点击记录时失败,异常为："+e.getClass().getName()+"|"+e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * @param convertDays the convertDays to set
	 */
	public void setConvertDays(int convertDays) {
		this.convertDays = convertDays;
	}

}
