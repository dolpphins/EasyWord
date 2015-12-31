package com.mao.ui;

import com.mao.easyword.R;
import com.mao.ui.base.BackActivity;

import android.os.Bundle;

/**
 * 设置Activity
 * 
 * @author mao
 *
 */
public class SettingsActivity extends BackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActionBar();
	}
	
	private void initActionBar() {
		setActionBarCenterText(R.string.settings);
	}
}
