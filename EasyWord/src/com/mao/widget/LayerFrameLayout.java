package com.mao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 可以设置层次的FrameLayout
 * 
 * @author mao
 *
 */
public class LayerFrameLayout extends FrameLayout {

	public LayerFrameLayout(Context context) {
		this(context, null);
	}
	
	public LayerFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 将指定的View移到最顶层
	 * 
	 * @param v 要移到的View
	 */
	public void moveToTop(View v) {
		if(v != null) {
			int index = indexOfChild(v);
			if(index >= 0 && index < getChildCount() - 1) {
				removeView(v);
				addView(v);
			}
		}
	}
	
	/**
	 * 将指定的View移到最顶层
	 * 
	 * @param v 要移到的View
	 */
	public void moveToBottom(View v) {
		if(v != null) {
			int index = indexOfChild(v);
			if(index > 0) {
				removeView(v);
				addView(v, 0);
			}
		}
	}
	
	/**
	 * 将指定的View向上移动一层
	 * 
	 * @param v 要移到的View
	 */
	public void moveUp(View v) {
		if(v != null) {
			int index = indexOfChild(v);
			if(index >= 0 && index < getChildCount() - 1) {
				removeViewAt(index);
				addView(v, index + 1);
			}
		}
	}
	
	/**
	 * 将指定的View向下移动一层
	 * 
	 * @param v 要移到的View
	 */
	public void moveDown(View v) {
		if(v != null) {
			int index = indexOfChild(v);
			if(index > 0) {
				removeViewAt(index);
				addView(v, index - 1);
			}
		}
	}

}
