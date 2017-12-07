package xyxtech.rpt.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.utils.TimeUtils;
import com.xyx.x.utils.XidUtils;

import xyxtech.rpt.runner.LogRunner;

public class TimeWatcher implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(TimeWatcher.class);

	private List<String> parentPaths;
	
	private String timeToWatch;
	
	private String stopTime;
	
	private boolean moreServerLog;
	
	private LogRunner runner;
	
	private BlockingQueue<File> waitToProcessFilesQueue;
	
	private String[] serverNums={"2","3","4"};
	

	@Override
	public void run() {

		while (true) {
			
			try{
				
				while(waitToProcessFilesQueue.size()>=500){
					logger.info("待处理文件太多："+waitToProcessFilesQueue.size()+",等待10秒后继续");
					Thread.sleep(10000);
				}
				
				if((timeToWatch=TimeUtils.getIntervalMinute(timeToWatch,1))==null){
					logger.error("时间参数输入的不正确，无法继续执行");
					break ;
				}
				long endTime=TimeUtils.sdfMinute.parse(TimeUtils.sdfMinute.format(new Date())).getTime();
				if(stopTime!=null&&stopTime.length()==12){
					endTime=TimeUtils.sdfMinute.parse(stopTime).getTime();
				}
				if(TimeUtils.sdfMinute.parse(timeToWatch).getTime()>=endTime){
					break;
				}
					
				String dirName=timeToWatch.substring(0, 8);
				
				File dir;
				String fileName;
				File f;
				for(String path : parentPaths){
					if(!moreServerLog){
						if(path.endsWith("imp")){
							dir=FileUtils.getFile(path,dirName);
							if(dir!=null){
								fileName=new StringBuilder(timeToWatch).append(".").append(XidUtils.getServerNum()).append(".imp.log").toString();
								f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
								if(f!=null)
									waitToProcessFilesQueue.put(f);
							}
							
						}else if(path.endsWith("click")){
							dir=FileUtils.getFile(path,dirName);
							if(dir!=null){
								fileName=new StringBuilder(timeToWatch).append(".").append(XidUtils.getServerNum()).append(".click.log").toString();
								f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
								if(f!=null)
									waitToProcessFilesQueue.put(f);
							}
							
						}else if(path.endsWith("reach")){
							dir=FileUtils.getFile(path,dirName);
							if(dir!=null){
								fileName=new StringBuilder(timeToWatch).append(".").append(XidUtils.getServerNum()).append(".reach.log").toString();
								f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
								if(f!=null)
									waitToProcessFilesQueue.put(f);
							}
							
						}else if(path.endsWith("cvt")){
							dir=FileUtils.getFile(path,dirName);
							if(dir!=null){
								fileName=new StringBuilder(timeToWatch).append(".").append(XidUtils.getServerNum()).append(".cvt.log").toString();
								f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
								if(f!=null)
									waitToProcessFilesQueue.put(f);
							}
							
						}
					}else{
						for(String serverNum:serverNums){
							if(path.endsWith("imp")){
								dir=FileUtils.getFile(path,dirName);
								if(dir!=null){
									fileName=new StringBuilder(timeToWatch).append(".").append(serverNum).append(".imp.log").toString();
									f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
									if(f!=null)
										waitToProcessFilesQueue.put(f);
								}
								
							}else if(path.endsWith("click")){
								dir=FileUtils.getFile(path,dirName);
								if(dir!=null){
									fileName=new StringBuilder(timeToWatch).append(".").append(serverNum).append(".click.log").toString();
									f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
									if(f!=null)
										waitToProcessFilesQueue.put(f);
								}
								
							}else if(path.endsWith("reach")){
								dir=FileUtils.getFile(path,dirName);
								if(dir!=null){
									fileName=new StringBuilder(timeToWatch).append(".").append(serverNum).append(".reach.log").toString();
									f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
									if(f!=null)
										waitToProcessFilesQueue.put(f);
								}
								
							}else if(path.endsWith("cvt")){
								dir=FileUtils.getFile(path,dirName);
								if(dir!=null){
									fileName=new StringBuilder(timeToWatch).append(".").append(serverNum).append(".cvt.log").toString();
									f=FileUtils.getFile(dir.getAbsolutePath(), fileName);
									if(f!=null)
										waitToProcessFilesQueue.put(f);
								}
								
							}
						}
						
					}
				}
			} catch (Exception e) {
				logger.error("TimeWatcher执行时出现错误"+e.getLocalizedMessage());
			}	
		}
		runner.callback();
		logger.info("TimeWatcher执行结束");
	}
	
	public void setParentPaths(List<String> parentPaths) {
		this.parentPaths = parentPaths;
	}
	
	public void setLogRunner(LogRunner runner) {
		this.runner = runner;
	}
	
	public void setTimeToWatch(String timeToWatch) {
		this.timeToWatch = timeToWatch;
	}
	
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	
	public void setMoreServerLog(boolean moreServerLog) {
		this.moreServerLog = moreServerLog;
	}


	/**
	 * @return the waitToProcessFilesQueue
	 */
	public BlockingQueue<File> getWaitToProcessFilesQueue() {
		return waitToProcessFilesQueue;
	}

	/**
	 * @param waitToProcessFilesQueue the waitToProcessFilesQueue to set
	 */
	public void setWaitToProcessFilesQueue(BlockingQueue<File> waitToProcessFilesQueue) {
		this.waitToProcessFilesQueue = waitToProcessFilesQueue;
	}

	

}
