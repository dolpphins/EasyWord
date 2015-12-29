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
	 * @see ImageSpanGenerator#generate(Context, Drawable, String, OnImageMeasureListener)
	 */
	public ImageSpan generate(Context context, Drawable drawable, String imageSpanSource) {
		return generate(context, drawable, imageSpanSource, null);
	}
	
	/**
	 * 生成一个缩放后的ImageSpan
	 * 
	 * @param context 上下文
	 * @param drawable ImageSpan中的drawable
	 * @param imageSpanSource ImageSpan的mSource
	 * @param listener 缩放因子测量监听器,为null表示不缩放
	 * 
	 * @return 成功返回一个ImageSpan对象,失败返回null.
	 */
	public ImageSpan generate(Context context, Drawable drawable, String imageSpanSource, OnImageMeasureListener listener) {
		if(context != null) {
			Bitmap bm = null;
			if(drawable == null) {
				drawable = MethodCompat.getDrawable(context, R.drawable.empty_picture);
			} 
			if(drawable == null) {
				return null;
			}
			float factor = 1.0f;
			if(listener != null) {
				factor = listener.onFactorMeasure(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			}
			if(factor < 0.0f) {
				factor = 1.0f;
			}
			//需要缩放
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
	
	/**
	 * Image测量接口
	 * 
	 * @author mao
	 *
	 */
	public interface OnImageMeasureListener {
		
		/**
		 * 通过宽高计算缩放因子,1.0f表示不进行缩放
		 * 
		 * @param width 宽
		 * @param height 高
		 * @return 返回缩放因子,注意所有小于0的都按1.0f对待
		 */
		float onFactorMeasure(int width, int height);
	}
	
	/**
	 * 获取默认的OnImageMeasureListener实现类对象,该实现类
	 * 的onFactorMeasure方法将会按图片最终显示宽度为1/2*totalWidth
	 * 计算缩放因子.
	 * 
	 * @param totalWidth 总宽度,必须大于0
	 * 
	 * @return 返回一个OnImageMeasureListener的默认实现类对象
	 */
	public static OnImageMeasureListener getDefaultImageMeasureListener(int totalWidth) {
		return new OnDefaultImageMeasureListener(totalWidth);
	}
	
	/**
	 * 默认的OnImageMeasureListener实现类
	 * 
	 * @author mao
	 *
	 */
	private static class OnDefaultImageMeasureListener implements ImageSpanGenerator.OnImageMeasureListener {
		
		private int totalWidth;
		
		public OnDefaultImageMeasureListener(int width) {
			totalWidth = width;
		}
		
		@Override
		public float onFactorMeasure(int width, int height) {
			if(totalWidth >= 0 && width > 0) {
				return totalWidth / (2.0f * width);
			}
			return 1.0f;
		}
	}
}
