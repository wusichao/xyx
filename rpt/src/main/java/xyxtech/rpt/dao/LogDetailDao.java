package xyxtech.rpt.dao;

import java.util.List;

import xyxtech.rpt.data.LogField;

public interface LogDetailDao {

	public void saveClickLogDetail(List<LogField> list);
	
	public void saveCvtLogDetail(List<LogField> list);
	
	public void saveReachLogDetail(List<LogField> list);
	
}
