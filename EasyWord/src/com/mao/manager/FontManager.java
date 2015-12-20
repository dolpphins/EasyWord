package com.mao.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.mao.bean.Font;
import com.mao.utils.IoUtils;
import com.mao.utils.TextUtils;

import android.graphics.Color;

/**
 * 字体管理器
 * 
 * @author mao
 *
 */
public class FontManager {

	/** 预定义字体大小列表 */
	private final static List<String> sFontSizeList = new ArrayList<String>();
	
	/** 预定义字体颜色列表 */
	private final static List<Integer> sFontColorList = new ArrayList<Integer>();
	
	/**
	 * 字体样式枚举
	 * */
	public static enum TextStyle {
		BOLD, ITALIC, UNDERLINE, DELETELINE
	}
	
	/** 默认配置字体对象 */
	private final static Font sFont = new Font();
	
	static {
		sFontSizeList.add("5");
		sFontSizeList.add("5.5");
		sFontSizeList.add("6.5");
		sFontSizeList.add("7.5");
		sFontSizeList.add("8");
		sFontSizeList.add("9");
		sFontSizeList.add("10");
		sFontSizeList.add("10.5");
		sFontSizeList.add("11");
		sFontSizeList.add("12");//默认
		sFontSizeList.add("14");
		sFontSizeList.add("16");
		sFontSizeList.add("18");
		sFontSizeList.add("20");
		sFontSizeList.add("22");
		sFontSizeList.add("24");
		sFontSizeList.add("26");
		sFontSizeList.add("28");
		sFontSizeList.add("36");
		sFontSizeList.add("48");
		sFontSizeList.add("72");
		
		sFontColorList.add(Color.BLACK);//默认
		sFontColorList.add(Color.WHITE);
		sFontColorList.add(Color.RED);
		sFontColorList.add(Color.GREEN);
		sFontColorList.add(Color.BLUE);
		sFontColorList.add(Color.YELLOW);
		sFontColorList.add(Color.GRAY);
		
		sFont.setSize(12.0f);
		sFont.setColor(Color.BLACK);
		sFont.setTextStyleSet(new HashSet<FontManager.TextStyle>());
	}
	
	/**
	 * 获取字体大小列表
	 * 
	 * @return 返回字体大小列表
	 */
	public static List<String> getFontSizeList() {
		return sFontSizeList;
	}
	
	/**
	 * 获取字体列表中占用宽度最长的长度
	 * 
	 * @param size 指定的字体大小
	 * @return 返回在指定字体下字体列表中占用宽度最长的长度
	 */
	public static float getMaxTextWidth(float size) {
		return TextUtils.getTextLength("10.55", size);
	}
	
	/**
	 * 获取默认字体大小
	 * 
	 * @return 返回默认字体大小
	 */
	public static String getDefaultFontSize() {
		return sFont.getSize() + "";
	}
	
	/**
	 * 获取字体颜色列表
	 * 
	 * @return 返回字体颜色列表
	 */
	public static List<Integer> getFontColorList() {
		return sFontColorList;
	}
	
	/**
	 * 获取默认的字体颜色
	 * 
	 * @return 返回字体颜色
	 */
	public static int getDefaultFontColor() {
		return sFont.getColor();
	}
	
	/**
	 * 获取默认字体配置
	 * 
	 * @return 返回默认字体配置对象
	 */
	public static Font getDefaultFont() {
		return IoUtils.copyObject(sFont);
	}
}
