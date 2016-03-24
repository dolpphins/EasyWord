package com.mao.ui;

import com.mao.bean.User;
import com.mao.conf.SpConfig;
import com.mao.easyword.R;
import com.mao.easyword.UserManager;
import com.mao.loader.Loginer;
import com.mao.loader.Loginer.LoginListener;
import com.mao.ui.base.BaseActivity;
import com.mao.utils.SharePreferencesManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * 启动Activity
 * 
 * @author mao
 *
 */
public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_splash_activity);
		
		//判断是否可以自动登录
		judgeAutoLogin();
	}
	
	private void judgeAutoLogin() {
		SharePreferencesManager spm = SharePreferencesManager.getInstance();
		String username = spm.getLoginMessageString(getApplicationContext(), SpConfig.USERNAME_KEY, null);
		String password = spm.getLoginMessageString(getApplicationContext(), SpConfig.PASSWORD_KEY, null);
		User user = new User();
		user.setUsername(username);
		//登录
		Loginer.getInstance().login(getApplicationContext(), user, password, new LoginListener() {
			
			@Override
			public void onSuccess(User user) {
				UserManager.getInstance().setCurrentUser(user);
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFail(String message) {
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
