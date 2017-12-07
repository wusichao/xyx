package xyxtech.rpt.dao;

import java.util.List;

import xyxtech.rpt.data.LogField;

public interface DiveDao {

	public void findClickLog(List<LogField> logs);
	
}
