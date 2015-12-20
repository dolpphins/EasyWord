package com.mao.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片加载器
 * 
 * @author mao
 *
 */
public class ImageLoader {

	private final static ImageLoader sInstance = new ImageLoader();
	
	private final static int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
	private final static int maximumPoolSize = 2 * corePoolSize + 1;
	private final static long keepAliveTime = 60;
	private final static TimeUnit unit = TimeUnit.SECONDS;
	private final static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(128);
	private final static ThreadFactory threadFactory = new ThreadFactory() {
		
		private final AtomicInteger sTaskNumber = new AtomicInteger(0);
		
		@Override
		public Thread newThread(Runnable r) {
			return new Thread("task #" + sTaskNumber.incrementAndGet());
		}
	};
	
	private final static ExecutorService sThreadPool = 
			new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 
					keepAliveTime, unit, workQueue, threadFactory,
					new ThreadPoolExecutor.DiscardOldestPolicy());
	
	private ImageLoader() {}
	
	public static ImageLoader getInstance() {
		return sInstance;
	}
	
	public void submit(Runnable task) {
		sThreadPool.submit(task);
	}
}
