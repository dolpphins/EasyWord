package com.mao.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * 方法兼容类
 * 
 * @author mao
 *
 */
public class MethodCompat {

	/**
	 * {@link Drawable#getDrawable()}兼容方法
	 * 
	 * @param context
	 * @param id
	 * @return 
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static Drawable getDrawable(Context context, int id) {
		if(context == null) {
			return null;
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return context.getResources().getDrawable(id, context.getTheme());
		} else {
			return context.getResources().getDrawable(id);
		}
	}
}
