package xyxtech.rpt.manager;

import java.util.TreeSet;

import redis.clients.jedis.JedisCluster;
import xyxtech.rpt.data.LogField;

public interface Manager {

	public void collect(LogField log);
	
	public void flush();
	
	public boolean needFlush();

	public void collectHZ(TreeSet<String> set, JedisCluster jedis);

	public void flushHZ(String ache, int i);

	public void clear();
	
	
}
