package com.mao.ui.base;

import com.mao.easyword.R;
import com.mao.utils.MethodCompat;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * <p>带有返回箭头的ActionBar的Activity</p>
 * 
 * @author 麦灿标
 * */
public class BackActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置ActionBar返回箭头及点击事件
		initActionBarBack();
	}
	
	private void initActionBarBack() {
		TextView tv = setActionBarLeftText("");
		if(tv != null) {
			Drawable left = MethodCompat.getDrawable(getApplicationContext(), R.drawable.app_back_arrow);
			left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
			tv.setCompoundDrawables(left, null, null, null);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onActionBarBack();
				}
			});
		}	
	}
	
	/**
	 * <p>
	 * 当点击ActionBar返回箭头时调用该方法,默认实现为调用返回键方法{@link Activity#onBackPressed()},
	 * 你可以重写该方法以便在处理ActionBar返回箭头点击事件
	 * </p>
	 * */
	public void onActionBarBack() {
		onBackPressed();
	}
}

