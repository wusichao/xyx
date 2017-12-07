package xyxtech.rpt.reader;

import java.io.File;

import xyxtech.rpt.manager.Manager;
import xyxtech.rpt.parser.LineParser;

public interface LogFileReader {

	public void readFile(File file);
	
	public void setLineParser(LineParser lineParser);
	
	public void setManager(Manager manager);
}
