package com.mao.executor;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.mao.utils.IoUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

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
	
	//暂时不使用线程池
	public void displayImage(ImageView imageView, String url) {
		if(imageView == null || TextUtils.isEmpty(url)) {
			return;
		}
		new ImageLoaderTask(imageView, url).execute(new Void[]{});
	}
	
	private static class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
		
		private ImageView imageView;
		private String networkUrl;
		
		public ImageLoaderTask(ImageView imageView, String networkUrl) {
			this.imageView = imageView;
			this.networkUrl = networkUrl;
		}
		
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			HttpURLConnection con = null;
			InputStream is = null;
			try {
				URL url = new URL(networkUrl);
				con = (HttpURLConnection) url.openConnection();
				is = con.getInputStream();
				return BitmapFactory.decodeStream(is);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				IoUtils.closeInputStream(is);
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if(imageView != null && result != null) {
				imageView.setImageBitmap(result);
			}
		}
	}
}
