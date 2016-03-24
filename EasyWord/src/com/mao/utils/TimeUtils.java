package com.mao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间相关工具类
 * 
 * @author mao
 *
 */
public class TimeUtils {

//	/**
//	 * 时间戳转人性化字符串表示
//	 * 
//	 * @param timestamp 要转换的时间戳
//	 * @return 转换后的字符串
//	 */
//	public static String timestamp2HString(long timestamp) {
//		long currentTime = System.currentTimeMillis();
//		long time = currentTime - timestamp;
//		if(time < 0) {
//			return null;
//		} else if(time < 60 * 1000) {
//			//一分钟内
//		} else if(time < 60 * 60 * 1000) {
//			//一小时内
//		} else if(time < 24 * 60 * 60 * 1000) {
//			//一天内
//		} else {
//			
//		}
//	}
	
	/**
	 * 时间戳转正常时间格式
	 * 
	 * @param timestamp 要转换的时间戳
	 * @return
	 */
	public static String timestamp2String(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		return sdf.format(new Date(timestamp));
	}
}
