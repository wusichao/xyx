package ch.qos.logback.core.rolling;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class ForceRollingFileAppender extends RollingFileAppender<ILoggingEvent> {

	private int forceRollingSeconds = 60;
	
	private static ScheduledExecutorService timer = Executors.newScheduledThreadPool(5, new DaemonThreadFactory());
	
	static class DaemonThreadFactory implements ThreadFactory{

		private static final AtomicInteger threadNumber = new AtomicInteger(1);
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.setName("force rolling file appender-"+threadNumber.getAndIncrement());
			return t;
		}
	}
	
	@Override
	public void start() {
		super.start();
		timer.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				forceRollover();
			}
		}, forceRollingSeconds, forceRollingSeconds, TimeUnit.SECONDS);
	}
	
	private void forceRollover(){
		synchronized (lock) {
			if(triggeringPolicy.isTriggeringEvent(currentlyActiveFile, null) 
					&& currentlyActiveFile.length() > 0){
				rollover();
			}
		}
	}

	/**
	 * @param forceRollingSeconds the forceRollingSeconds to set
	 */
	public void setForceRollingSeconds(int forceRollingSeconds) {
		this.forceRollingSeconds = forceRollingSeconds;
	}

	
}
