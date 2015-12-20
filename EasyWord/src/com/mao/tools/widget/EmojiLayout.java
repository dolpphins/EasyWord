package com.mao.tools.widget;

import com.mao.adapter.EmotionViewPagerAdapter;
import com.mao.easyword.R;
import com.mao.interf.Togglable;
import com.mao.widget.WrapContentViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 表情布局
 * 
 * @author mao
 *
 */
public class EmojiLayout extends LinearLayout implements Togglable {

	private ViewPager mViewPager;
	private EmotionViewPagerAdapter mPagerAdapter;
	/** 相关联的EditText */
	private EditText mAttachEditText;
	
	private ToggleWrapView mWrapView;
	
	public EmojiLayout(Context context) {
		this(context, null);
	}
	
	public EmojiLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mViewPager = new WrapContentViewPager(getContext());
		mPagerAdapter = new EmotionViewPagerAdapter(getContext(), mAttachEditText);
		mViewPager.setAdapter(mPagerAdapter);
		addView(mViewPager);
		mWrapView = new ToggleWrapView(this, true);
	}
	
	/**
	 * 设置接收表情输入的EditText
	 * 
	 * @param et 要接收表情输入EditText
	 */
	public void attachEditText(EditText et) {
		mAttachEditText = et;
		if(mPagerAdapter != null) {
			mPagerAdapter.attachEditext(mAttachEditText);
		}
	}
	
	@Override
	public void toggle() {
		mWrapView.toggle();
	}
	
	@Override
	public void show() {
		mWrapView.show();
	}
	
	@Override
	public void hide() {
		mWrapView.hide();
	}
	
	@Override
	public boolean isShowing() {
		return mWrapView.isShowing();
	}
}
