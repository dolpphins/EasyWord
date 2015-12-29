package com.mao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 自定义ActionBar
 * 
 * @author mao
 *
 */
public class SimilarActionBar extends ViewGroup {

	public SimilarActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.layout(l, t, r, b);
	}

}
