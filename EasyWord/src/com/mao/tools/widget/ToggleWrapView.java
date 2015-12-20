package com.mao.tools.widget;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class ToggleWrapView {

	private final static String TAG = "ToggleWrapView";
	
	private View mView;
	
//	private Animation mShowAnimation;
//	
//	private Animation mHideAnimation;
	
	//private int mPreVisibility = View.VISIBLE;
//	
	private boolean isShowing;
	
	/**
	 * 构造函数
	 * 
	 * @param view 要实现显示和隐藏的View
	 * @param isShowing 初始状态,true表示显示,false表示隐藏
	 */
	public ToggleWrapView(View view, boolean isShowing) {
		mView = view;
		this.isShowing = isShowing;
		init(isShowing);
	}
	
	private void init(boolean isShowing) {
		if(mView != null) {
			if(isShowing) {
				show();
			} else {
				hide();
			}
		}
	}
	
//	public void setShowAnimation(Animation anim) {
//		mShowAnimation = anim;
//	}
//	
//	public void setHideAnimation(Animation anim) {
//		mHideAnimation = anim;
//	}
	
//	public void setShowAnimation(Context context, int id) {
//		if(context != null) {
//			mShowAnimation = AnimationUtils.loadAnimation(context, id);
//		}
//	}
//	
//	public void setHideAnimation(Context context, int id) {
//		if(context != null) {
//			mHideAnimation = AnimationUtils.loadAnimation(context, id);
//		}
//	}
	
	public void toggle() {
		if(mView != null && mView.getVisibility() == View.VISIBLE) {
			hide();
		} else {
			show();
		}
	}
	
	public void show() {
//		if(mView != null && mShowAnimation != null && !isShwoing) {
//			mPreVisibility = mView.getVisibility();
//			mView.setVisibility(View.VISIBLE);
//			mView.startAnimation(mShowAnimation);
//			isShwoing = true;
//		}
		if(mView != null) {
			mView.setVisibility(View.VISIBLE);
			isShowing = true;
		}
	}
	
	public void hide() {
//		if(mView != null && mHideAnimation != null && isShwoing) {
//		    mHideAnimation.setAnimationListener(new AnimationListener() {
//				
//		    	@Override
//				public void onAnimationStart(Animation animation) {}
//				
//		    	@Override
//				public void onAnimationRepeat(Animation animation) {}
//				
//		    	@Override
//				public void onAnimationEnd(Animation animation) {
//		    		mView.setVisibility(mPreVisibility);
//				}
//			});
//			mView.startAnimation(mHideAnimation);
//			isShwoing = false;
//		}
		if(mView != null) {
			mView.setVisibility(View.GONE);
			isShowing = false;
		}
	}
	
	public boolean isShowing() {
		return isShowing;
	}
}
