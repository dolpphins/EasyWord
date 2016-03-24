package com.mao.loader;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mao.conf.EmotionConfiguration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.text.style.ImageSpan;

/**
 * 默认的ImageSpanGetter实现类
 * 
 * @author mao
 *
 */
public class DefaultImageSpanHandler implements SpannedLoader.ImageSpanHandler{

	private Context mContext;
	private String mBasePath;
	
//	//缩放因子,默认为1/3
//	private float mFactor = (float) 1.0f / 3;
	
	//监听器
	private ImageSpanGenerator.OnImageMeasureListener mImageMeasureListener;
	
	//保存所有图片
	private final Map<String, String> mPicturesMap = new LinkedHashMap<String, String>();
	
	/**
	 * 构造函数
	 * 
	 * @param context 上下文
	 * @param basePath ImageSpan中source的基本路径(即路径前缀)
	 */
	public DefaultImageSpanHandler(Context context, String basePath) {
		mContext = context;
		mBasePath = basePath;
	}
	
	@Override
	public ImageSpan getImageSpan(String source) {
		
		//可能是表情
		ImageSpan span = generateEmojiImageSpan(source);
		//可能是图片
		if(span == null) {
			String path = mBasePath + source;
			mPicturesMap.put(source, path);
			span = generatePictureImageSpan(path, source);
		}
		return span;
	}

	//产生表情ImageSpan
	private ImageSpan generateEmojiImageSpan(String source) {
		Drawable drawable = EmotionConfiguration.getEmojiDrawableFromString(mContext, source);
		if(drawable == null) {
			return null;
		} else {
			return ImageSpanGenerator.getInstance().generate(mContext, drawable, source);
		}
	}
	
	/**
	 * 生成图片ImageSpan
	 * 
	 * @param srcPath 源图片路径
	 * @param source 对应ImageSpan的mSource属性
	 * @return 成功返回ImageSpan对象,失败返回null.
	 */
	public ImageSpan generatePictureImageSpan(String srcPath, String source) {
		if(!TextUtils.isEmpty(srcPath)) {
			Bitmap bm = BitmapFactory.decodeFile(srcPath);
			Drawable drawable = null;
			if(bm != null) {
				drawable = new BitmapDrawable(mContext.getResources(), bm);
			}
			return ImageSpanGenerator.getInstance().generate(mContext, drawable, source, mImageMeasureListener);
		} else {
			return null;
		}
	}
	
//	/**
//	 * 设置图片缩放因子,注意该缩放因子只对图片有效,对其它(比如表情)无效
//	 * 
//	 * @param factor 要设置的缩放因子,注意必须大于等于0.
//	 * 
//	 * @throws IllegalArgumentException 如果factor小于0将抛出该异常
//	 */
//	public void setFactor(float factor) {
//		if(factor < 0) {
//			throw new IllegalArgumentException("factor can't be less than 0");
//		}
//		mFactor = factor;
//	}
//	
//	/**
//	 * 获取图片缩放因子
//	 * 
//	 * @return 返回缩放因子
//	 */
//	public float getFactor() {
//		return mFactor;
//	}
	
	/**
	 * 设置OnImageMeasureListener
	 * 
	 * @param listener 要设置的监听器
	 */
	public void setOnImageMeasureListener(ImageSpanGenerator.OnImageMeasureListener listener) {
		mImageMeasureListener = listener;
	}
	
	/**
	 * 获取OnImageMeasureListener
	 * 
	 * @return 返回OnImageMeasureListener
	 */
	public ImageSpanGenerator.OnImageMeasureListener getOnImageMeasureListener() {
		return mImageMeasureListener;
	}
	
	/**
	 * 获取当前已处理的图片信息(url)map,其中key:文件名 value:文件路径
	 * 
	 * @return 返回已处理的图片地址map
	 */
	public Map<String, String> getPicturesPathList() {
		return mPicturesMap;
	}
}
