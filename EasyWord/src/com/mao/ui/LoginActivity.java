package com.mao.ui;

import com.mao.bean.User;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.conf.SpConfig;
import com.mao.easyword.R;
import com.mao.easyword.UserManager;
import com.mao.loader.Loginer;
import com.mao.ui.base.BaseActivity;
import com.mao.utils.EncryptHelper;
import com.mao.utils.SharePreferencesManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登录Activity
 * 
 * @author mao
 *
 */
public class LoginActivity extends BaseActivity {

	private EditText app_login_username_phone_et;
	private EditText app_login_password_et;
	private Button app_login_btn;
	private TextView app_login_login_tip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.app_login_activity);
		
		initActionBar();
		
		initView();
		
		setViewEvent();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//判断是否已登录，如果已登录则finish掉当前Activity
		if(UserManager.getInstance().getCurrentUser() != null) {
			finish();
		}
	}
	
	private void initActionBar() {
		setActionBarCenterText(R.string.login);
		TextView registerTv = setActionBarRightText(R.string.register);
		if(registerTv != null) {
			registerTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
					startActivityForResult(intent, ActivityRequestResultCode.LOGIN_TO_REGISTER_ACTIVITY_REQUEST_CODE);
				}
			});
		}
	}
	
	private void initView() {
		app_login_username_phone_et = (EditText) findViewById(R.id.app_login_username_phone_et);
		app_login_password_et = (EditText) findViewById(R.id.app_login_password_et);
		app_login_btn = (Button) findViewById(R.id.app_login_btn);
		app_login_login_tip = (TextView) findViewById(R.id.app_login_login_tip);
	}
	
	private void setViewEvent() {
		app_login_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String usernamePhone = app_login_username_phone_et.getText().toString().trim();
				String password = app_login_password_et.getText().toString().trim();
				if(!TextUtils.isEmpty(usernamePhone) && !TextUtils.isEmpty(password)) {
					password = EncryptHelper.md5(password);
					if(!TextUtils.isEmpty(password)) {
						User user = new User();
						//先判断是用户名还是手机号
						if(usernamePhone.length() > 10) {
							user.setPhone(usernamePhone);
						} else {
							user.setUsername(usernamePhone);
						}
						Loginer.getInstance().login(getApplicationContext(), user, password, new OnLoginListener());
					}
				} else {
					if(TextUtils.isEmpty(usernamePhone)) {
						app_login_login_tip.setText(R.string.input_username_phone_tip);
					} else if(TextUtils.isEmpty(password)) {
						app_login_login_tip.setText(R.string.input_password_tip);
					}
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//已登录
		if(UserManager.getInstance().getCurrentUser() != null) {
			//启动MainActivity
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	//登录监听器
	private class OnLoginListener implements Loginer.LoginListener {

		@Override
		public void onSuccess(User user) {
			UserManager.getInstance().setCurrentUser(user);
			//写入SP文件
			writeToSP(user);
			
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		@Override
		public void onFail(String message) {
			app_login_login_tip.setText(R.string.login_fail);
		}
		
	}
	
	private void writeToSP(User user) {
		SharePreferencesManager spm = SharePreferencesManager.getInstance();
		spm.setLoginMessageString(getApplicationContext(), SpConfig.USERNAME_KEY, user.getUsername());
		spm.setLoginMessageString(getApplicationContext(), SpConfig.PASSWORD_KEY, user.getPassword());
	}
}
