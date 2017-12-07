package xyxtech.rpt.runner;

import java.util.concurrent.atomic.AtomicBoolean;

public class SignalConst {

	private static AtomicBoolean shutdown = new AtomicBoolean(false);
	
	public static void shutdown(){
		shutdown.set(true);
	}
	
	public static boolean isShutdown(){
		return shutdown.get();
	}
}
