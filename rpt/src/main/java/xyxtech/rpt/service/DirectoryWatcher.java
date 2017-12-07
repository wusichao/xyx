package xyxtech.rpt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryWatcher implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(DirectoryWatcher.class);

	private String directoryToWatch;

	private BlockingQueue<File> waitToProcessFilesQueue;
	
	private boolean watchFile = true;
	
	/**
	 * @param watchFile the watchFile to set
	 */
	public void setWatchFile(boolean watchFile) {
		this.watchFile = watchFile;
	}

	@Override
	public void run() {

		WatchService watchService;
		try {
			watchService = FileSystems.getDefault().newWatchService();
			Paths.get(directoryToWatch).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.OVERFLOW);
			while (true) {
				// 如果未变化，则阻塞进程
				WatchKey key = watchService.poll(6, TimeUnit.SECONDS);
				if(key!=null){
					for (WatchEvent<?> event : key.pollEvents()) {
						if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE
								|| event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
							File f = FileUtils.getFile(directoryToWatch, event.context().toString());
							if (f != null && f.isFile() && watchFile) {
								if(f.getName().endsWith(".log")){
									//logger.info("发现待分析文件:{},事件类型:{}", f.getAbsolutePath(),event.kind());
									waitToProcessFilesQueue.put(f);
								}else{
									logger.warn("发现异常文件："+f.getName());
								}
							}
							
						} else {
							logger.warn("目录:{},文件:{},事件:{},个数:{}", directoryToWatch, event.context().toString(), event.kind(),
									event.count());
						}
					}
					
					if (!key.reset()) {
						logger.error("wathkey失效，停止监控", directoryToWatch);
						break;
					}
	
				}else{
					if(!new File(directoryToWatch).exists()){
						logger.info("目录{}已经不存在，停止监控", directoryToWatch);
						break;
					}
				}
				
			}
		} catch (IOException | InterruptedException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

	}


	/**
	 * @param directoryToWatch the directoryToWatch to set
	 */
	public void setDirectoryToWatch(String directoryToWatch) {
		this.directoryToWatch = directoryToWatch;
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
