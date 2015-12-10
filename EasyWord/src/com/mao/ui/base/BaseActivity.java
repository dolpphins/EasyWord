package com.mao.ui.base;

import com.mao.easyword.R;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 基本的Activity
 * 
 * @author 麦灿标
 *
 */
public class BaseActivity extends Activity {
	
	private View mActionBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(0);
		mActionBarView = LayoutInflater.from(this).inflate(R.layout.app_common_actionbar, null);
		ActionBar.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(mActionBarView, params);
	}
	
	/**
	 * 设置ActionBar左边文字
	 * 
	 * @param text 要设置的文字
	 */
	public void setActionBarLeftText(String text) {
		if(mActionBarView != null) {
			TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_left_tv);
			if(tv != null) {
				tv.setText(text);
			}
		}
	}
	
	/**
	 * 设置ActionBar中间文字
	 * 
	 * @param text 要设置的文字
	 */
	public void setActionBarCenterText(String text) {
		if(mActionBarView != null) {
			TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_center_tv);
			if(tv != null) {
				tv.setText(text);
			}
		}
	}
	
	/**
	 * 设置ActionBar右边文字
	 * 
	 * @param text 要设置的文字
	 */
	public void setActionBarRightText(String text) {
		if(mActionBarView != null) {
			TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_right_tv);
			if(tv != null) {
				tv.setText(text);
			}
		}
	}
	
	/**
	 * 设置ActionBar背景颜色
	 * 
	 * @param color 要设置的颜色
	 */
	public void setActionBarBackgroundColor(int color) {
		if(mActionBarView != null) {
			mActionBarView.setBackgroundColor(color);
		}
	}
}
