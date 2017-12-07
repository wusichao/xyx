package xyxtech.rpt.parser;

import xyxtech.rpt.data.LogField;

public interface LineParser{

	public LogField parseLine(String columns);
}
