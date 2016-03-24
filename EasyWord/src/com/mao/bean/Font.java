package com.mao.bean;

import java.io.Serializable;
import java.util.Set;

import com.mao.manager.FontManager;

/**
 * 字体实体类
 * 
 * @author mao
 *
 */
public class Font implements Serializable{

	/** 字体大小 */
	private float size;
	
	/** 字体颜色 */
	private int color;
	
	/** 字体样式 */
	private Set<FontManager.TextStyle> textStyleSet;
	
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Set<FontManager.TextStyle> getTextStyleSet() {
		return textStyleSet;
	}

	public void setTextStyleSet(Set<FontManager.TextStyle> textStyleSet) {
		this.textStyleSet = textStyleSet;
	}	
}
