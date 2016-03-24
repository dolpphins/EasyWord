package com.mao.loader;

import java.util.List;

import com.mao.bean.User;

import android.content.Context;
import android.text.TextUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 登录器
 * 
 * @author mao
 *
 */
public class Loginer {

	private final static Loginer sLoginer = new Loginer();
	
	/** 表示是否正在进行登录 */
	private volatile boolean mIsLogging;
	
	private Loginer() {
		mIsLogging = false;
	}
	
	public static Loginer getInstance() {
		return sLoginer;
	}
	
	/**
	 * 判断是否正在登录
	 * 
	 * @param username 用户名,不能为空
	 * @param password 密码,不能为空,注意是MD5码
	 * 
	 * @return 返回true表示正在进行登录操作,否则返回false.
	 */
	public boolean isLogging() {
		return mIsLogging;
	}
	
	/**
	 * 
	 * @param context
	 * @param user
	 * @param password
	 * @param listener
	 * 
	 */
	public void login(Context context, User user, String password, final LoginListener listener) {
		if(!mIsLogging) {
			if(context != null && user != null && !TextUtils.isEmpty(password)) {
				BmobQuery<User> query = new BmobQuery<User>();
				if(!TextUtils.isEmpty(user.getUsername())) {
					query.addWhereEqualTo("username", user.getUsername());	
				} else if(!TextUtils.isEmpty(user.getPhone())) {
					query.addWhereEqualTo("phone", user.getPhone());
				} else {
					if(listener != null) {
						listener.onFail("illegal arguments,username and phone can't be null on the same time");
					}
				}
				query.addWhereEqualTo("password", password);
				mIsLogging = true;
				query.findObjects(context, new FindListener<User>() {
					
					@Override
					public void onSuccess(List<User> userList) {
						mIsLogging = false;
						if(userList != null && userList.size() > 0) {
							if(listener != null) {
								listener.onSuccess(userList.get(0));
							}
						}
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						mIsLogging = false;
						if(listener != null) {
							listener.onFail(arg1);
						}
					}
				});
				return;
			}
		}
		if(listener != null) {
			listener.onFail("illegal arguments");
		}
	}
	
	/**
	 * 登录监听器
	 * 
	 * @author mao
	 *
	 */
	public interface LoginListener {
		
		/**
		 * 登录成功回调函数
		 * 
		 * @param user 登录的用户信息
		 */
		void onSuccess(User user);
		
		/**
		 * 登录失败回调函数
		 * 
		 * @param message 失败原因
		 */
		void onFail(String message);
	}
}
