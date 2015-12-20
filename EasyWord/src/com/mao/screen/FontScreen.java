package com.mao.screen;

import com.mao.bean.Size;

/**
 * 与字体设置相关的屏幕适配类
 * 
 * @author mao
 *
 */
public class FontScreen {

	public static Size getFontColorItemSize(int totalWidth, int numColumn, int interval) {
		Size size = new Size();
		if(totalWidth <= 0 || numColumn <= 0 || interval < 0) {
			return size;
		}
		int totalInterval = (numColumn - 1) * interval;
		if(totalInterval > totalWidth) {
			return size;
		}
		int surplus = totalWidth - totalInterval;
		size.setWidth(surplus / numColumn);
		size.setHeight(size.getWidth() * 2 / 3);
		
		return size;
	}
}
