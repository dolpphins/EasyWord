package com.mao.utils;

import java.text.DecimalFormat;

import android.graphics.Paint;

/**
 * Text相关工具类
 * 
 * @author mao
 *
 */
public class TextUtils {

	/**
	 * 获取指定文本在指定字体大小下的宽度
	 * 
	 * @param text 指定的文本
	 * @param size 指定的字体大小
	 * @return 返回指定文本在指定字体大小下的宽度
	 */
	public static float getTextLength(String text, float size) {
		Paint paint = new Paint();
		paint.setTextSize(size);
		return paint.measureText(text);
	}
	
	/**
	 * 将以字节为单位转换为合适的单位表示,保留两位小数
	 * 
	 * @param size 要转换的大小,以字节为单位
	 * @return 转换成功返回相应的字符串表示,失败返回null.
	 */
	public static String convertByteCount(long size) {
		if(size < 0) {
			return null;
		}
		long KB = 1024;
		long MB = 1024 * KB;
		long GB = 1024 * MB;
		long TB = 1024 * GB;
		DecimalFormat format = new DecimalFormat("#0.00");
		StringBuilder sb = new StringBuilder(8);
		if(size < KB) {
			sb.append(format.format(size));
			sb.append("B");
		} else if(size < MB) {
			sb.append(format.format(size / KB));
			sb.append("KB");
		} else if(size < GB) {
			sb.append(format.format(size / MB));
			sb.append("MB");
		} else if(size < TB) {
			sb.append(format.format(size / GB));
			sb.append("GB");
		} else {
			sb.append(format.format(size / TB));
			sb.append("TB");		
		}
		return sb.toString();
	}
}
