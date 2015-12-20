package com.mao.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

/**
 * 与Android中View相关的工具类
 * 
 * @author mao
 *
 */
public class ViewUtils {

	/**
	 * 获取指定View的背景颜色
	 * 
	 * @param v 要获取背景颜色的View对象
	 * @return 获取成功返回相应的背景颜色值,获取失败返回-1.
	 */
	public static int getBackgroundColor(View v) {
		if(v == null) {
			return -1;
		}
		Drawable drawable = v.getBackground();
		if(drawable != null) {
			if(drawable instanceof ColorDrawable) {
				return ((ColorDrawable) drawable).getColor();
			}
		}
		return -1;
	}
}
