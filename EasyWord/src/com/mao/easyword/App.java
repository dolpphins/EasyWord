package com.mao.easyword;

import java.io.File;

import com.mao.bean.User;
import com.mao.conf.AppConfig;

import android.app.Application;
import cn.bmob.v3.Bmob;

public class App extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		
		Bmob.initialize(getApplicationContext(), AppAccessToken.APPLICATION_ID);
		
		createFolder();
	}
	
	private void createFolder() {
		File mainFolder = new File(AppConfig.MAIN_SDCARD_FOLDER_PATH);
		if(!mainFolder.exists()) {
			mainFolder.mkdirs();
		}
	}
}
