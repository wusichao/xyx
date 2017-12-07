package xyxtech.rpt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import xyxtech.rpt.runner.LogRunner;
import xyxtech.rpt.runner.SignalConst;
import xyxtech.rpt.utils.DateUtils; 

@SuppressWarnings("restriction")
public class App {
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		SignalHandlerImpl handler = new SignalHandlerImpl();
		Signal.handle(new Signal("INT"),handler);//捕获CTRL+c
		Signal.handle(new Signal("TERM"),handler);//捕获kill
		
		//启动主线程
		CountDownLatch latch = new CountDownLatch(1);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring.xml");
		
		LogRunner runner = (LogRunner)context.getBean("runner");
		runner.setLatch(latch);
		if(args!=null && args.length>0){
			if(args[0]!=null && args[0].equals("true")){
				runner.setMoreServerLog(true);
			}
			if(args[1]!=null && args[1].length()==12)
				runner.setTimeToWatch(args[1]);
			if(args[2]!=null && args[2].length()==12)
				runner.setStopTime(args[2]);
		}
		
		Thread thread = new Thread(runner);
		thread.setName("LogWatcher");
		thread.start();
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			context.close();
		}
		
	}

	public static class SignalHandlerImpl implements SignalHandler{
		
		@Override
		public void handle(Signal arg0) {
			logger.info("rpt is shuting down");
			SignalConst.shutdown();
		}
		
	}
	
}
