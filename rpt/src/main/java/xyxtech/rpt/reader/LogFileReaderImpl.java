package xyxtech.rpt.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyxtech.rpt.manager.Manager;
import xyxtech.rpt.parser.LineParser;

public class LogFileReaderImpl implements LogFileReader {
	
	private static final Logger logger = LoggerFactory.getLogger(LogFileReaderImpl.class);
	
	private LineParser lineParser;
	
	private Manager manager;
	
	@Override
	public void readFile(File file) {
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			String line;
			while((line = br.readLine()) != null){
				manager.collect(lineParser.parseLine(line));
			}
			br.close();
		} catch ( IOException e) {
			logger.error("read file [{}] with exception.", new StringBuilder(file.getAbsolutePath()).append(File.separatorChar).append(file.getName()));
		}
	}

	@Override
	public void setLineParser(LineParser lineParser) {
		this.lineParser = lineParser;
	}

	@Override
	public void setManager(Manager manager) {
		this.manager = manager;
	}

}
