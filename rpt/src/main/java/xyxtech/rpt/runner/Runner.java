package xyxtech.rpt.runner;

import java.util.concurrent.CountDownLatch;

public interface Runner extends Runnable {

	public void setLatch(CountDownLatch latch);
}
