package com.xyx.dp.dao.mariadb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xyx.x.model.Account;
import com.xyx.x.model.Campaign;
import com.xyx.x.model.enu.CostType;
import com.xyx.dp.dao.TrackingDao;
import com.xyx.dp.model.TrackingIdsWithTimes;

public class TrackingMariadbDao extends JdbcDaoSupport implements TrackingDao {
	
	private static final String QUERY_CAMPAIGN_CREATIVE_SQL = ""
			+ " select ca.id,unix_timestamp(last_modified) last_modified,null channel_set,group_concat(cerel.creative_id) creative_set,null convertion_set,"
			+ " ca.account_id,ca.name,ca.begin_date,ca.end_date,ca.cost_type,ca.target_url,ca.removed"
			+ " from campaign ca left join campaign_creative_rel cerel on ca.id = cerel.campaign_id"
			+ " where ca.id ";
	private static final String QUERY_CAMPAIGN_CHANNEL_SQL= ""
			+ " group by ca.id"
			+ " union"
			+ " select ca.id,unix_timestamp(last_modified) last_modified,group_concat(chrel.channel_id) channel_set,null creative_set,null convertion_set,"
			+ " ca.account_id,ca.name,ca.begin_date,ca.end_date,ca.cost_type,ca.target_url,ca.removed"
			+ " from campaign ca left join campaign_channel_rel chrel on ca.id = chrel.campaign_id"
			+ " where ca.id ";
	
	private static final String QUERY_CAMPAIGN_CONVERTION_SQL= ""
			+ " group by ca.id"
			+ " union"
			+ " select ca.id,unix_timestamp(last_modified) last_modified,null channel_set,null creative_set,group_concat(corel.convertion_id) convertion_set,"
			+ " ca.account_id,ca.name,ca.begin_date,ca.end_date,ca.cost_type,ca.target_url,ca.removed"
			+ " from campaign ca left join campaign_convertion_rel corel on ca.id = corel.campaign_id"
			+ " where ca.id ";
	
	private static final String QUERY_CAMPAIGN_SQL_TAIL = ""
			+ " group by ca.id";
	
	
	private static final String QUERY_ACCOUNT_CHANNEL_SQL= ""
			+ " select ac.id,ac.domain_type,unix_timestamp(last_modified) last_modified,group_concat(acrel.channel_id) channel_set,null media_set"
			+ " from account ac left join account_channel_rel acrel  on acrel.account_id = ac.id"
			+ " where ac.id ";
	private static final String QUERY_ACCOUNT_MEDIA_SQL= ""
			+ " group by ac.id"
			+ " union"
			+ " select ac.id,ac.domain_type,unix_timestamp(last_modified) last_modified,null channel_set,group_concat(amrel.media_id) media_set"
			+ " from account ac left join account_media_rel amrel on amrel.account_id = ac.id"
			+ " where ac.id ";
	
	private static final String QUERY_ACCOUNT_SQL_TAIL = ""
			+ " group by ac.id";
	
	private static final String QUERY_IDS_SQL = ""
			+ " select ca.id campaign_id,unix_timestamp(ca.last_modified) campaign_last_modified,"
			+ " ac.id account_id,unix_timestamp(ac.last_modified) account_last_modified"
			+ " from campaign ca,account ac"
			+ " where ca.account_id = ac.id"
			+ " and curdate() >= ca.begin_date" //提前加载一天的数据 + " and curdate() >= date_add(ca.begin_date,INTERVAL -1 DAY)"
			+ " and curdate() <= ca.end_date"
			+ " and ca.last_modified > 20151107";
	
	public List<Campaign> getCampaign(Long... ids) {
		if (ids == null || ids.length == 0) {
			return new ArrayList<Campaign>();
		}
		
		StringBuilder sql = new StringBuilder(getInSql(QUERY_CAMPAIGN_CREATIVE_SQL, ids, true));
		sql.append(QUERY_CAMPAIGN_CHANNEL_SQL);
		sql = new StringBuilder(getInSql(sql.toString(), ids ,true ));
		sql.append(QUERY_CAMPAIGN_CONVERTION_SQL);
		sql = new StringBuilder(getInSql(sql.toString(), ids ,true ));
		sql.append(QUERY_CAMPAIGN_SQL_TAIL);
		
		List<Campaign> list = this.getJdbcTemplate().query(sql.toString(),new RowMapper<Campaign>(){
			@Override
			public Campaign mapRow(ResultSet rs, int rowNum) throws SQLException {
				Campaign ret = new Campaign();
				ret.setId(rs.getLong("id"));
				ret.setLastModified(rs.getLong("last_modified"));
				ret.setAccountId(rs.getLong("account_id"));
				ret.setName(rs.getString("name"));
				ret.setBeginDate(rs.getString("begin_date"));
				ret.setEndDate(rs.getString("end_date"));
				ret.setCostType(CostType.valueOf(rs.getString("cost_type")));
				ret.setTargetUrl(rs.getString("target_url"));
				ret.setChannelIdSet(getLongSet(rs.getString("channel_set"),','));
				ret.setCreativeIdSet(getLongSet(rs.getString("creative_set"), ','));
				ret.setConvertionIdSet(getLongSet(rs.getString("convertion_set"), ','));
				ret.setRemoved(rs.getBoolean("removed"));
				return ret;
			}
			
		});
		
		//合并相同的记录行
		Map<Long, Campaign> campaignMap = new HashMap<>();
		
		List<Campaign> ret = new ArrayList<>();
		
		list.forEach( i -> {
			Campaign ca = campaignMap.get(i.getId());
			if(ca == null){
				if(i.getChannelIdSet() == null)
					i.setChannelIdSet(new HashSet<>());
				if(i.getCreativeIdSet() == null)
					i.setCreativeIdSet(new HashSet<>());
				if(i.getConvertionIdSet() == null)
					i.setConvertionIdSet(new HashSet<>());
				campaignMap.put(i.getId(), i);
				ret.add(i);
			}else{
				if(i.getChannelIdSet() != null){
					ca.getChannelIdSet().addAll(i.getChannelIdSet());
				}
				if(i.getCreativeIdSet() != null){
					ca.getCreativeIdSet().addAll(i.getCreativeIdSet());
				}
				if(i.getConvertionIdSet() != null){
					ca.getConvertionIdSet().addAll(i.getConvertionIdSet());
				}
			}
		});
		
		return ret;
	}

	public List<Account> getAccount(Long... ids) {
		if (ids == null || ids.length == 0) {
			return new ArrayList<Account>();
		}
		StringBuilder sql = new StringBuilder(getInSql(QUERY_ACCOUNT_CHANNEL_SQL, ids, true));
		sql.append(QUERY_ACCOUNT_MEDIA_SQL);
		sql = new StringBuilder(getInSql(sql.toString(), ids ,true ));
		sql.append(QUERY_ACCOUNT_SQL_TAIL);
		
		List<Account> list = this.getJdbcTemplate().query(sql.toString(), new RowMapper<Account>(){
			@Override
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account ret = new Account();
				ret.setId(rs.getLong("id"));
				ret.setDomainType(rs.getInt("domain_type"));
				ret.setLastModified(rs.getLong("last_modified"));
				ret.setChannelIdSet(getLongSet(rs.getString("channel_set"),','));
				ret.setMediaIdSet(getLongSet(rs.getString("media_set"),','));
				return ret;
			}
			
		});
		
		//合并相同的记录行
		Map<Long, Account> accountMap = new HashMap<>();
		
		List<Account> ret = new ArrayList<>();
		
		list.forEach( i -> {
			Account ac = accountMap.get(i.getId());
			if(ac == null){
				if(i.getChannelIdSet() == null)
					i.setChannelIdSet(new HashSet<>());
				if(i.getMediaIdSet() == null)
					i.setMediaIdSet(new HashSet<>());
				accountMap.put(i.getId(), i);
				ret.add(i);
			}else{
				if(i.getChannelIdSet() != null){
					ac.getChannelIdSet().addAll(i.getChannelIdSet());
				}
				if(i.getMediaIdSet() != null){
					ac.getMediaIdSet().addAll(i.getMediaIdSet());
				}
			}
		});
		
		return ret;
		
		
	}
	
	private String getInSql(String sql, Long[] ids, boolean onlyIn) {
		StringBuilder buf = new StringBuilder(sql);
		if (onlyIn) {
			buf.append(" in(");
		} else {
			buf.append(" where id in(");
		}
		for (int i = 0; i < ids.length - 1; i++) {
			buf.append(ids[i]).append(",");
		}
		buf.append(ids[ids.length - 1]);
		buf.append(")");
		return buf.toString();
	}
	
	private Set<Long> getLongSet(String str, char sepa) {
		if (str == null) {
			return null;
		}
		String[] sets = StringUtils.split(str, sepa);
		Set<Long> set = new HashSet<>();
		for (String re : sets) {
			re = re.trim();
			if (re.length() != 0) {
				set.add(com.xyx.x.utils.StringUtils.toLong(re));
			}
		}
		return set;
	}

	@Override
	public List<TrackingIdsWithTimes> getTrackingIds() {
		return this.getJdbcTemplate().query(QUERY_IDS_SQL, BeanPropertyRowMapper.newInstance(TrackingIdsWithTimes.class));
	}

}
