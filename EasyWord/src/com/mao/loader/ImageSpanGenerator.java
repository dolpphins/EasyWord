package com.mao.loader;

import com.mao.easyword.R;
import com.mao.utils.ImageUtils;
import com.mao.utils.MethodCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * ImageSpan生成器
 * 
 * @author mao
 *
 */
public class ImageSpanGenerator {

	private final static ImageSpanGenerator sInstance = new ImageSpanGenerator();
	
	private ImageSpanGenerator() {}
	
	public static ImageSpanGenerator getInstance() {
		return sInstance;
	}
	
	/**
	 * 生成一个带有默认图片的ImageSpan
	 * 
	 * @param context 上下文
	 * 
	 * @return 成功返回一个ImageSpan对象,失败返回null.
	 * 
	 * @see ImageSpanGenerator#generate(Context, Drawable, String)
	 */
	public ImageSpan generate(Context context) {
		return generate(context, null, null);
	}
	
	/**
	 * 生成一个ImageSpan
	 * 
	 * @param context 上下文
	 * @param drawable ImageSpan中的drawable
	 * @param imageSpanSource ImageSpan的mSource
	 * 
	 * @return 成功返回一个ImageSpan对象,失败返回null.
	 * 
	 * @see ImageSpanGenerator#generate(Context, Drawable, String, float)
	 */
	public ImageSpan generate(Context context, Drawable drawable, String imageSpanSource) {
		return generate(context, drawable, imageSpanSource, 1.0f);
	}
	
	/**
	 * 生成一个缩放后的ImageSpan
	 * 
	 * @param context 上下文
	 * @param drawable ImageSpan中的drawable
	 * @param imageSpanSource ImageSpan的mSource
	 * @param factor 缩放因子
	 * 
	 * @return 成功返回一个ImageSpan对象,失败返回null.
	 */
	public ImageSpan generate(Context context, Drawable drawable, String imageSpanSource, float factor) {
		if(context != null) {
			Bitmap bm = null;
			if(drawable == null) {
				drawable = MethodCompat.getDrawable(context, R.drawable.empty_picture);
			} 
			if(drawable == null) {
				return null;
			}
			//需要进行缩放
			if(Math.abs(factor - 1.0f) > 1e-6) {
				bm = ImageUtils.drawable2Bitmap(drawable);
				Bitmap scaledBitmap = ImageUtils.createScaledBitmap(bm, factor);
				bm.recycle();
				drawable = new BitmapDrawable(context.getResources(), scaledBitmap);
			}
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			ImageSpan span = new ImageSpan(drawable, imageSpanSource, ImageSpan.ALIGN_BOTTOM);
			return span;
		} else {
			return null;
		}
	}
}
