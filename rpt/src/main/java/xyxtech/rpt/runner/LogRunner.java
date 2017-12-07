package xyxtech.rpt.runner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyx.x.utils.TimeUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import xyxtech.rpt.manager.Manager;
import xyxtech.rpt.reader.LogFileReader;
import xyxtech.rpt.service.DirectoryWatcher;
import xyxtech.rpt.service.TimeWatcher;
import xyxtech.rpt.utils.DateUtils;

public class LogRunner implements Runner {

	private static final Logger logger = LoggerFactory.getLogger(LogRunner.class);
	
	private BlockingQueue<File> queue = new LinkedBlockingDeque<>(1000);
	
	private LogFileReader reader;
	
	private Manager manager;
	
	private List<String> parentPaths;
	
	private String bakDirName;
	
	private long writeDbIntervalMills;
	
	private long lastWriteDbTime=System.currentTimeMillis();
	
	private boolean moreServerLog;
	
	//日志分析程序异常终止后，再次重新启动时，需要指定开始时间，开始时间是上次最后成功分析的日志时间，格式为：yyyyMMddHHmm。该参数在启动rpt.sh时使用
	private String timeToWatch;
	
	private String stopTime;
	
	private int noFileWaitMills;
	
	private CountDownLatch latch;
	
	private JedisCluster jedis; 
	
	/*
	 * 启动时执行
	 * 检查当天和第二天的目录是否存在，如果不存在就创建目录，并启动监测
	 * 将前两天的目录移动到备份目录下
	 */
	public void initDateDir(){
		
		try {
			
			for(String path : parentPaths){
				
				String dateDirName=new StringBuilder(path).append(File.separatorChar).append(new SimpleDateFormat("yyyyMMdd").format(new Date())).toString();
				File dateDir=new File(dateDirName);
				if(!dateDir.exists()){
					dateDir.mkdirs();
				}
				newWatcher(queue,dateDir.getAbsolutePath(),false,"begin to watch date dir");
				
				Calendar d=Calendar.getInstance();
				d.setTime(new Date());
				if(d.get(Calendar.HOUR_OF_DAY)>=3){
					dateDirName=new StringBuilder(path).append(File.separatorChar).append(new SimpleDateFormat("yyyyMMdd").format(DateUtils.afterDate(new Date(), 1))).toString();
					dateDir=new File(dateDirName);
					if(!dateDir.exists()){
						dateDir.mkdirs();
					}
					newWatcher(queue,dateDir.getAbsolutePath(),false,"begin to watch date dir");
				}

				File bakDir=new File(new StringBuilder(path).append(File.separatorChar).append(bakDirName).toString());
				if(!bakDir.exists()){
					bakDir.mkdirs();
				}
				dateDirName=new StringBuilder(path).append(File.separatorChar).append(new SimpleDateFormat("yyyyMMdd").format(DateUtils.afterDate(new Date(), -2))).toString();
				dateDir=new File(dateDirName);
				if(dateDir.exists()){
					dateDir.renameTo(new File(new StringBuilder(path).append(File.separatorChar).append(bakDirName).append(File.separatorChar).append(dateDir.getName()).toString()));
				}
				
			}
			
	} catch (Exception e) {
		logger.error(e.getLocalizedMessage(),e);
	}
	}
	
	/*
	 * 每天凌晨定时执行
	 * 检查第二天的目录是否存在，如果不存在就创建目录，并启动监测
	 * 将前两天的目录移动到备份目录下
	 */
	public void dateDirMonitor(){
		try {
			if(timeToWatch!=null||stopTime!=null)
				return;
				
			for(String path : parentPaths){
				
				String dateDirName=new StringBuilder(path).append(File.separatorChar).append(new SimpleDateFormat("yyyyMMdd").format(DateUtils.afterDate(new Date(), 1))).toString();
				File dateDir=new File(dateDirName);
				if(!dateDir.exists()){
					dateDir.mkdirs();
				}
				newWatcher(queue,dateDir.getAbsolutePath(),false,"begin to watch date dir");
				
				File bakDir=new File(new StringBuilder(path).append(File.separatorChar).append(bakDirName).toString());
				if(!bakDir.exists()){
					bakDir.mkdirs();
				}
				dateDirName=new StringBuilder(path).append(File.separatorChar).append(new SimpleDateFormat("yyyyMMdd").format(DateUtils.afterDate(new Date(), -2))).toString();
				dateDir=new File(dateDirName);
				if(dateDir.exists()){
					dateDir.renameTo(new File(new StringBuilder(path).append(File.separatorChar).append(bakDirName).append(File.separatorChar).append(dateDir.getName()).toString()));
				}
				
			}
			/*
			Set<String> set = keys(new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.afterDate(new Date(), -2))+"*",jedis);
			logger.info("key数量:"+set.size());
			Iterator<String> it = set.iterator();  
			logger.info("清理开始时间:"+TimeUtils.sdfMills.format((new Date())));
	        while(it.hasNext()){  
	            String keyStr = it.next();  
	            jedis.del(keyStr);  
	        }
	        logger.info("清理结束时间:"+TimeUtils.sdfMills.format((new Date())));
	        */
	} catch (Exception e) {
		logger.error(e.getLocalizedMessage());
	}
	}
	//更根据前缀获取所有key
	public  TreeSet<String> keys(String pattern, JedisCluster jedis){  
		TreeSet<String> keys = new TreeSet<>();  
        Map<String, JedisPool> clusterNodes = jedis.getClusterNodes();  
        for(String k : clusterNodes.keySet()){  
            JedisPool jp = clusterNodes.get(k);  
            Jedis connection = jp.getResource();  
            try {  
                keys.addAll(connection.keys(pattern));  
            } catch(Exception e){  
            } finally{  
                connection.close();//用完一定要close这个链接！！！  
            }  
        }  
        return keys;  
    }
	//定时统计imp人数与IP
	public void impCount(){
		String prip = TimeUtils.sdf.format(DateUtils.afterDate(new Date(), -1))+":ip:"+"IMPRESSION:";
		String pruser =  TimeUtils.sdf.format(DateUtils.afterDate(new Date(), -1))+":user:"+"IMPRESSION:";
		String acip=prip+"ac*";
		String acuser=pruser+"ac*";
		String achip=prip+"ach*";
		String achuser=pruser+"ach*";
		String aceip=prip+"ace*";
		String aceuser=pruser+"ace*";
		String acheip=prip+"ache*";
		String acuheser=pruser+"ache*";
		String [] imp = {acip,acuser,achip,achuser,aceip,aceuser,acheip,acuheser};
		try {
			for(int i = 0 ;i<imp.length;i++){
				TreeSet<String> set = keys(imp[i], jedis);
				String ache = set.first();
				manager.collectHZ(set,jedis);
				manager.flushHZ(ache,i);
				manager.clear();
			}
		} catch (Exception e) {
			logger.info("定时统计imp人数与IP出错");
		}
	}
	
	
	@Override
	public void run() {
		
		logger.info("LogRunner启动了,Thread name="+Thread.currentThread().getName());
		
		processDelay();
		
		File log;
		
		while(!SignalConst.isShutdown()){
			
			try {
				logger.info("队列有"+queue.size()+"个文件");
				log = queue.poll(noFileWaitMills, TimeUnit.MILLISECONDS);
				if(log != null ){
					String minute=log.getName().substring(0, log.getName().indexOf("."));
					//把同一分钟生成的日志全部读进来
					do{
						long start=System.currentTimeMillis();
						reader.readFile(log);
						logger.info("end read "+log.getName()+", took "+(System.currentTimeMillis()-start)+" milliseconds");
						log = queue.poll(10000, TimeUnit.MILLISECONDS);
					}while(log!= null && minute.equals(log.getName().substring(0, log.getName().indexOf("."))));
					
				}else{
					logger.info("no read");
				}
				
				if(manager.needFlush() || System.currentTimeMillis() - lastWriteDbTime > writeDbIntervalMills){
					long start=System.currentTimeMillis();
					manager.flush();
					lastWriteDbTime = System.currentTimeMillis();
					logger.info("end flush, took "+(lastWriteDbTime-start)+" milliseconds");
				}
				
				if(log!=null){
					long start=System.currentTimeMillis();
					reader.readFile(log);
					logger.info("end read "+log.getName()+", took "+(System.currentTimeMillis()-start)+" milliseconds");
				}
				
			} catch (InterruptedException e) {
				logger.error("read file with exception [{}]",e.getLocalizedMessage(),e);
			}
			
		}
		
		
		long start=System.currentTimeMillis();
		manager.flush();
		logger.info("end flush before shutdown, took "+(System.currentTimeMillis()-start)+" milliseconds");
		
		
		logger.info("LogRunner退出了,Thread name="+Thread.currentThread().getName());
		
		//运行结束
		latch.countDown();
		
	}
	
	private void newWatcher(BlockingQueue<File> queue,String path,boolean watchDir,String msg){
		DirectoryWatcher watcher = new DirectoryWatcher();
		watcher.setWaitToProcessFilesQueue(queue);
		watcher.setDirectoryToWatch(path);
		if(watchDir){
			watcher.setWatchFile(false);
		}
		Thread thread = new Thread(watcher);
		thread.setDaemon(true);
		thread.start();
		logger.info("{}:{}",msg,path);
	}
	
	private void processDelay(){
		
		if(timeToWatch==null){
			initDateDir();
			return;
		}
		
		try{
			
			TimeWatcher watcher = new TimeWatcher();
			watcher.setLogRunner(this);
			watcher.setWaitToProcessFilesQueue(queue);
			watcher.setParentPaths(parentPaths);
			watcher.setTimeToWatch(timeToWatch);
			watcher.setStopTime(stopTime);
			watcher.setMoreServerLog(moreServerLog);
			Thread thread = new Thread(watcher);
			thread.setDaemon(true);
			thread.start();
			logger.info("开始处理延迟日志,日志起始时间是："+timeToWatch);
		}catch(Exception e){
			logger.error("启动TimeWatcher时遇到错误："+e.getLocalizedMessage());
		}
	}
	
	public void callback(){
		if(stopTime==null){
			initDateDir();
		}
		
		//dateDirMonitor可以按时执行
		timeToWatch=null;
	}
	

	/**
	 * @param queue the queue to set
	 */
	public void setQueue(BlockingQueue<File> queue) {
		this.queue = queue;
	}

	/**
	 * @param reader the reader to set
	 */
	public void setReader(LogFileReader reader) {
		this.reader = reader;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(Manager manager) {
		this.manager = manager;
	}

	/**
	 * @param parentPath the parentPath to set
	 */
	public void setParentPaths(String parentPaths) {
		this.parentPaths = Arrays.asList(parentPaths.split(";"));
	}

	public void setBakDirName(String bakDirName) {
		this.bakDirName = bakDirName;
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
	 * @param noFileWaitMills the noFileWaitMills to set
	 */
	public void setNoFileWaitMills(int noFileWaitMills) {
		this.noFileWaitMills = noFileWaitMills;
	}
	
	public void setWriteDbIntervalMills(int writeDbIntervalMills) {
		this.writeDbIntervalMills = writeDbIntervalMills;
	}

	@Override
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}
	public void setJedis(JedisCluster jedis) {
		this.jedis = jedis;
	}
}
