package xyxtech.rpt.data.collector.reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyxtech.rpt.data.LogField;
import xyxtech.rpt.data.RptBase;
import xyxtech.rpt.data.collector.reporter.merge.Merger;

public abstract class BaseReporter<T extends RptBase> implements Reporter<T> {

	protected Map<T, T> data  = new HashMap<>();

	public abstract T generateRpt(LogField log);
	public abstract T userAndIpRpt(T rpt, LogField log);
	
	@Override
	public void process(LogField log) {
		T rpt = generateRpt(log);
		
		 rpt = userAndIpRpt(rpt,log);
		
		Merger.mergeLog(rpt, log);
		T oldValue = data.putIfAbsent(rpt, rpt);
		//同一统计纬度的数据进行合并
		if(oldValue != null){
			Merger.mergRpt(rpt, oldValue);
		}
	}

	@Override
	public List<T> report() {
		return new ArrayList<>(data.keySet());
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean needFlush() {
		return data.keySet().size()>600;
	}

	
}
