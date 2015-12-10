package com.mao.ui;

import com.mao.easyword.R;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		
		setEventForView();
	}
	
	private void initView() {

	}
	
	private void setEventForView() {
	}
}
