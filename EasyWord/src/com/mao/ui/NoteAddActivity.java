package com.mao.ui;

import java.util.List;
import java.util.Set;

import com.mao.bean.Font;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.easyword.R;
import com.mao.interf.Togglable;
import com.mao.manager.FontManager;
import com.mao.manager.FontManager.TextStyle;
import com.mao.tools.widget.EmojiLayout;
import com.mao.tools.widget.NavigationViewPagerLayout;
import com.mao.tools.widget.ToolsFontLayout;
import com.mao.ui.base.BaseActivity;
import com.mao.utils.SoftInputHelper;
import com.mao.widget.LayerFrameLayout;
import com.mao.widget.WordEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * 
 * 
 * @author mao
 *
 */
public class NoteAddActivity extends BaseActivity {

	private final static String TAG = "NoteAddActivity";
	
	private WordEditText note_add_et;
	private RelativeLayout note_add_bottom_photo;
	private RelativeLayout note_add_bottom_emoji;
	private RelativeLayout note_add_bottom_tools;
	private LayerFrameLayout note_add_bottom_content;
	
	/**
	 * 表情
	 */
	private EmojiLayout mEmojiLayout;
	
	/**
	 * 工具
	 */
	private NavigationViewPagerLayout mNavigationViewPager;
	private String[] mTitles;
	private View[] mToolsVPViews;
	
	/** 标记是否正在显示底部布局 */
	private boolean mIsBottomShowing = false;
	
	//默认使用字体配置
	private Font mFont = FontManager.getDefaultFont(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_add_activity);
		
		initView();
		setEventForView();
	}
	
	private void initView() {
		note_add_et = (WordEditText) findViewById(R.id.note_add_et);
		note_add_bottom_photo = (RelativeLayout) findViewById(R.id.note_add_bottom_photo);
		note_add_bottom_emoji = (RelativeLayout) findViewById(R.id.note_add_bottom_emoji);
		note_add_bottom_tools = (RelativeLayout) findViewById(R.id.note_add_bottom_tools);
		note_add_bottom_content = (LayerFrameLayout) findViewById(R.id.note_add_bottom_content);
		
		note_add_et.setFont(mFont);
		
		initPicturesLayoutView();
		initEmojiLayoutView();
		initToolsLayoutView();
		
		//添加底部所有布局
		note_add_bottom_content.addView(mEmojiLayout);
		note_add_bottom_content.addView(mNavigationViewPager);
		
		//隐藏底部所有布局
		hideBottomLayoutAll();
	}
	
	//初始化底部选择图片布局
	private void initPicturesLayoutView() {
	}
	
	//初始化底部表情布局
	private void initEmojiLayoutView() {
		mEmojiLayout = new EmojiLayout(getApplicationContext());
		mEmojiLayout.attachEditText(note_add_et);
	}
	
	//初始化底部工具布局
	private void initToolsLayoutView() {
		mNavigationViewPager = new NavigationViewPagerLayout(getApplicationContext());
		String fonts = getResources().getString(R.string.node_add_tools_font);
		mTitles = new String[]{fonts};
		mToolsVPViews = new View[mTitles.length];
		ToolsFontLayout fontLayout = new ToolsFontLayout(getApplicationContext());
		fontLayout.setOnFontConfigurationListener(new AddNoteFontConfigurationListener());
		mToolsVPViews[0] = fontLayout;
		mNavigationViewPager.setData(mTitles, mToolsVPViews);
	}
	
	private void setEventForView() {
		//编辑框点击事件
		note_add_et.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideBottomLayoutAll();
			}
		});
		//底部选择图片按钮点击事件
		note_add_bottom_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NoteAddActivity.this, SelectPictureActivity.class);
				startActivityForResult(intent, ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_REQUEST_CODE);
				//隐藏底部布局及键盘
				hideBottomLayoutAll();
				SoftInputHelper.hideSoftInput(getApplicationContext(), v);
			}
		});
		//底部表情按钮点击事件
		note_add_bottom_emoji.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleBottomLayout(mEmojiLayout);
				SoftInputHelper.hideSoftInput(getApplicationContext(), v);
			}
		});
		//底部工具按钮点击事件
		note_add_bottom_tools.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleBottomLayout(mNavigationViewPager);
				SoftInputHelper.hideSoftInput(getApplicationContext(), v);
			}
		});
	}
	
	//隐藏所有底部View
	private void hideBottomLayoutAll() {
		mEmojiLayout.hide();
		mNavigationViewPager.hide();
		mIsBottomShowing = false;
	}
	
	private void toggleBottomLayout(Togglable layout) {
		boolean shouldShow = false;
		if(!layout.isShowing()) {
			shouldShow = true;
		}
		hideBottomLayoutAll();
		if(shouldShow) {
			layout.show();
			mIsBottomShowing = true;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(mIsBottomShowing) {
				hideBottomLayoutAll();
				mIsBottomShowing = false;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//字体配置改变监听器
	private class AddNoteFontConfigurationListener implements ToolsFontLayout.OnFontConfigurationListener {

		@Override
		public void OnFontSizeChange(float oldSize, float newSize) {
			mFont.setSize(newSize);
		}

		@Override
		public void OnFontColorChange(int oldColor, int newColor) {
			mFont.setColor(newColor);
			note_add_et.setTextColor(mFont.getColor());
		}

		@Override
		public void OnFontStyleChange(Set<TextStyle> mTextStyleSet) {
			mFont.setTextStyleSet(mTextStyleSet);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//从选择图片返回
		if(requestCode == ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_REQUEST_CODE
				&& resultCode == ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_RESULT_CODE
				&& data != null) {
			List<String> selectedUrlList = data.getStringArrayListExtra(SelectPictureActivity.SELECTED_URL_INTENT_NAME);
			System.out.println(selectedUrlList);
		}
	}
}
