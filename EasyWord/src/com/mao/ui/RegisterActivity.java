package com.mao.ui;

import java.util.List;

import org.w3c.dom.Text;

import com.mao.bean.User;
import com.mao.conf.SpConfig;
import com.mao.easyword.R;
import com.mao.easyword.UserManager;
import com.mao.ui.base.BackActivity;
import com.mao.utils.EncryptHelper;
import com.mao.utils.SharePreferencesManager;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

/**
 * 注册Activity
 * 
 * @author mao
 *
 */
public class RegisterActivity extends BackActivity {

	private EditText app_register_username_et;
	private EditText app_register_phone_et;
	private EditText app_register_password_et;
	private EditText app_register_validatecode_et;
	private TextView app_register_validatecode_tv;
	private Button app_finish_btn;
	private TextView app_register_tip;
	
	//验证码
	private Integer smsId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.app_register_activity);
		
		initActionBar();
		
		initView();
		
		setEvent();
	}
	
	private void initActionBar() {
		setActionBarCenterText(R.string.register);
	}
	
	private void initView() {
		app_register_username_et = (EditText) findViewById(R.id.app_register_username_et);
		app_register_phone_et = (EditText) findViewById(R.id.app_register_phone_et);
		app_register_password_et = (EditText) findViewById(R.id.app_register_password_et);
		app_register_validatecode_et = (EditText) findViewById(R.id.app_register_validatecode_et);
		app_register_validatecode_tv = (TextView) findViewById(R.id.app_register_validatecode_tv);
		app_finish_btn = (Button) findViewById(R.id.app_finish_btn);
		app_register_tip = (TextView) findViewById(R.id.app_register_tip);
		
		enableValidateCodeTv(true);
	}
	
	private void setEvent() {
		//发送验证码
		app_register_validatecode_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = app_register_phone_et.getText().toString().trim();
				if(TextUtils.isEmpty(phone)) {
					setRegisterError(R.string.input_phone_tip);
				} else {
					sendValidateCode(phone);
				}
			}
		});
		//尝试注册
		app_finish_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//注册
				register();
			}
		});
	}
	
	private void register() {
		String username = app_register_username_et.getText().toString().trim();
		String phone = app_register_phone_et.getText().toString().trim();
		String password = app_register_password_et.getText().toString().trim();
		String validateCode = app_register_validatecode_et.getText().toString().trim();
		
		if(TextUtils.isEmpty(username)) {
			setRegisterError(R.string.input_username_tip);
		} else if(TextUtils.isEmpty(phone)) {
			setRegisterError(R.string.input_phone_tip);
		} else if(TextUtils.isEmpty(password)) {
			setRegisterError(R.string.input_password_tip);
		} else if(TextUtils.isEmpty(validateCode)) {
			setRegisterError(R.string.input_validatecode_tip);
		} else {
			startRegister(username, phone, password, validateCode);
		}
	}
	
	private void sendValidateCode(String phone) {
		BmobSMS.requestSMSCode(getApplicationContext(), phone, "注册验证码", new RequestSMSCodeListener() {
			
			@Override
			public void done(Integer smsId, BmobException e) {
				//成功
				if(e == null) {
					handleForValidateCodeSendSuccessFully(smsId);
				}
			}
		});
	}
	
	private void setRegisterError(int resid) {
		app_register_tip.setText(resid);
	}
	
	private void handleForValidateCodeSendSuccessFully(Integer smsId) {
		this.smsId = smsId;
		Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
		enableValidateCodeTv(false);
		new ValidateCodeUpdateThread(60).start();
	}
	
	//设置获取验证码按钮是否可用
	private void enableValidateCodeTv(boolean enable) {
		Resources res = getResources();
		if(enable) {
			app_register_validatecode_tv.setClickable(true);
			app_register_validatecode_tv.setBackgroundColor(res.getColor(R.color.app_main_color));
		} else {
			app_register_validatecode_tv.setClickable(false);
			app_register_validatecode_tv.setBackgroundColor(res.getColor(R.color.app_common_button_selBackgroundColor));
		}
	}
	
	private void updateValidateCodeTv(int remainTime) {
		String reGet = getResources().getString(R.string.reget);
		StringBuilder sb = new StringBuilder(reGet);
		if(remainTime <= 0) {
			enableValidateCodeTv(true);
		} else {
			sb.append("(");
			sb.append(remainTime);
			sb.append("s)");
		}
		app_register_validatecode_tv.setText(sb.toString());
	}
	
	//开始注册
	private void startRegister(final String username, final String phone, final String password, String validateCode) {
		
		//测试阶段不用验证码
		if(true) {
			registerNewUser(username, phone, password);
			return;
		}
		
		//先验证验证码是否正确
		BmobSMS.verifySmsCode(getApplicationContext(), phone, validateCode, new VerifySMSCodeListener() {
			
			@Override
			public void done(BmobException e) {
				if(e == null) {
					registerNewUser(username, phone, password);
				} else {
					setRegisterError(R.string.validate_code_error);
				}
			}
		});
	}
	
	//注册新用户
	private void registerNewUser(final String username, final String phone, final String password) {
		//判断该用户名是否已注册
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("username", username);
		query.count(getApplicationContext(), User.class, new CountListener() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				setRegisterError(R.string.register_fail);
			}
			
			@Override
			public void onSuccess(int count) {
				if(count <= 0) {
					BmobQuery<User> query = new BmobQuery<User>();
					query.addWhereEqualTo("phone", phone);
					query.count(getApplicationContext(), User.class, new CountListener() {
						
						@Override
						public void onFailure(int arg0, String arg1) {
							setRegisterError(R.string.register_fail);
						}
						
						@Override
						public void onSuccess(int count) {
							if(count <= 0) {
								realRegister(username, phone, password);
							} else {
								setRegisterError(R.string.phone_alread_register);
							}
						}
					});
				} else {
					setRegisterError(R.string.username_alread_register);
				}
			}
		});
	}
	
	//真正进行主注册
	private void realRegister(String username, String phone, String password) {
		final User user = new User();
		user.setPhone(phone);
		user.setPassword(EncryptHelper.md5(password));
		user.setUsername(username);//默认以手机号码为用户名
		user.save(getApplicationContext(), new SaveListener() {
			
			@Override
			public void onSuccess() {
				//注册成功
				handleRegisterSuccessfully(user);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				//注册失败
				Toast.makeText(getApplicationContext(), R.string.register_fail, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void handleRegisterSuccessfully(User user) {
		//先设置信息
		UserManager.getInstance().setCurrentUser(user);
		//写入SP文件
		SharePreferencesManager spm = SharePreferencesManager.getInstance();
		spm.setLoginMessageString(getApplicationContext(), SpConfig.USERNAME_KEY, user.getUsername());
		spm.setLoginMessageString(getApplicationContext(), SpConfig.PASSWORD_KEY, user.getPassword());
		//跳转
		finish();
	}
	
	//验证码TextView更新线程
	private class ValidateCodeUpdateThread extends Thread {
		
		private int mTotalSecond;
		private int mPastSecond;
		
		public ValidateCodeUpdateThread(int total) {
			mTotalSecond = total;
			mPastSecond = 0;
		}
		
		@Override
		public void run() {
			while(mPastSecond <= mTotalSecond) {
				app_register_validatecode_tv.post(new Runnable() {
					
					@Override
					public void run() {
						updateValidateCodeTv(mTotalSecond - mPastSecond);
					}
				});
				try {
					Thread.sleep(1000L);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mPastSecond++;
			}
		}
	}
}
