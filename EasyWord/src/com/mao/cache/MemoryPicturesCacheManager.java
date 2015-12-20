package com.mao.cache;

import com.mao.utils.ImageUtils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

/**
 * 图片内存缓存管理器
 * 
 * @author mao
 *
 */
public class MemoryPicturesCacheManager {

	private final static MemoryPicturesCacheManager sInstance = new MemoryPicturesCacheManager();
	
	private int maxMemory = (int) Runtime.getRuntime().maxMemory();//heap区最大大小
	private int maxSize = maxMemory / 6;
	
	private final LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(maxSize) {
		
		@Override
		protected int sizeOf(String key, Bitmap value) {
			return ImageUtils.sizeOfBitmap(value);
		}
	};
	
	private MemoryPicturesCacheManager() {
		init();
	}
	
	private void init() {
	}
	
	public static MemoryPicturesCacheManager getInstance() {
		return sInstance;
	}
	
	/**
	 * 添加缓存
	 * 
	 * @param key 不能为空
	 * @param value 不能空
	 * @return 添加成功返回之前该key对应的value,可能为null.
	 */
	public Bitmap put(String key, Bitmap value) {
		if(TextUtils.isEmpty(key) || value == null) {
			return null;
		}
		return mLruCache.put(key, value);
	}
	
	/**
	 * 获取缓存
	 * 
	 * @param key 不能为空
	 * @return 存在返回相应的Bitmap,不存在返回null.
	 */
	public Bitmap get(String key) {
		if(TextUtils.isEmpty(key)) {
			return null;
		}
		return mLruCache.get(key);
	}
	
	/**
	 * 重新调整缓存大小
	 * 
	 * @param maxSize 以字节为单位,必须大于0.
	 */
	public void resize(int maxSize) {
		if(maxSize > 0) {
			mLruCache.resize(maxSize);
			this.maxSize = maxSize;
		}
	}
	
	/**
	 * 获取缓存最大大小,以字节为单位
	 * 
	 * @return 返回缓存最大大小
	 */
	public int getMaxSize() {
		return maxSize;
	}
}
