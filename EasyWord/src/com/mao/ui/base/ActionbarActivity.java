package com.mao.ui.base;

import android.os.Bundle;
import android.view.Window;

/**
 * 带有自定义ActionBar的Activity
 * 
 * @author mao
 *
 */
public class ActionbarActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
	}
}
