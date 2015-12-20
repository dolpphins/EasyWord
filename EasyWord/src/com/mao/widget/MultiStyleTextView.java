package com.mao.widget;

import com.mao.easyword.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 多样式TextView,支持下划线、删除线等
 * 
 * @author mao
 *
 */
public class MultiStyleTextView extends TextView {

	private boolean mIsUnderline;
	private boolean mIsDeleteline;
	
	public MultiStyleTextView(Context context) {
		this(context, null);
	}
	
	public MultiStyleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.multiStyleTextView);
		int count = array.getIndexCount();
		for(int i = 0; i < count; i++) {
			int index = array.getIndex(i);
			switch (index) {
			case R.styleable.multiStyleTextView_underline:
				mIsUnderline = array.getBoolean(index, false);
				break;
			case R.styleable.multiStyleTextView_deleteline:
				mIsDeleteline = array.getBoolean(index, false);
				break;
			}
		}
		array.recycle();
		setText(getText());//重新调用
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		if(mIsUnderline) {
			getPaint().setAntiAlias(true);
			getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		}
		if(mIsDeleteline) {
			getPaint().setAntiAlias(true);
			getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		super.setText(text, type);
	}

	/**
	 * 设置是否是否添加下划线
	 * 
	 * @param isUnderline true表示添加,false表示不添加
	 */
	public void setUnderline(boolean isUnderline) {
		mIsUnderline = isUnderline;
	}
	
	/**
	 * 设置是否是否添加删除线
	 * 
	 * @param isDeleteline true表示添加,false表示不添加
	 */
	public void setDeleteline(boolean isDeleteline) {
		mIsDeleteline = isDeleteline;
	}
}
