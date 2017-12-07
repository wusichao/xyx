package xyxtech.rpt.data.collector.reporter;

import java.util.List;

import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptBase;

public interface Reporter<T extends RptBase> {

	public void process(LogField log);
	
	public List<T> report();
	
	public void clear();
	
	public boolean needFlush();
	
}
