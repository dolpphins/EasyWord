package com.mao.ui.base;

import com.mao.easyword.R;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BaseActivity extends Activity {
	
	private View mActionBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		if(actionBar != null) {
			actionBar.setDisplayOptions(0);
			actionBar.setDisplayShowCustomEnabled(true);
			mActionBarView = LayoutInflater.from(this).inflate(R.layout.app_common_actionbar, null);
			ActionBar.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mActionBarView, params);
		}
		
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		//设置当前Window背景颜色为白色
		getWindow().setBackgroundDrawableResource(R.color.white);
	}
	
	public TextView setActionBarLeftText(int id) {
		Resources resources = getResources();
		return setActionBarLeftText(resources.getString(id));
	}
	
	public TextView setActionBarLeftText(String text) {
		if(mActionBarView != null) {
			TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_left_tv);
			if(tv != null) {
				tv.setText(text);
				return tv;
			}
		}
		return null;
	}
	
	public TextView setActionBarCenterText(int id) {
		Resources resources = getResources();
		return setActionBarCenterText(resources.getString(id));
	}
	
	public TextView setActionBarCenterText(String text) {
		if(mActionBarView != null) {
			TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_center_tv);
			if(tv != null) {
				tv.setText(text);
				return tv;
			}
		}
		return null;
	}
	
	public TextView setActionBarRightText(int id) {
		Resources resources = getResources();
		return setActionBarRightText(resources.getString(id));
	}
	
	public TextView setActionBarRightText(String text) {
		if(mActionBarView != null) {
			TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_right_tv);
			if(tv != null) {
				tv.setText(text);
			}
			return tv;
		}
		return null;
	}
	
	public void setActionBarBackgroundColor(int color) {
		if(mActionBarView != null) {
			mActionBarView.setBackgroundColor(color);
		}
	}
}
