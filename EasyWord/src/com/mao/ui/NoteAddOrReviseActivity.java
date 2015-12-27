package com.mao.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mao.bean.Font;
import com.mao.bean.Note;
import com.mao.bean.User;
import com.mao.cache.DiskCacheManager;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.db.DBHelper;
import com.mao.easyword.R;
import com.mao.easyword.UserManager;
import com.mao.interf.Togglable;
import com.mao.loader.CharacterStyleParser;
import com.mao.loader.NoteLoader;
import com.mao.manager.FontManager;
import com.mao.manager.FontManager.TextStyle;
import com.mao.screen.DisplayUtils;
import com.mao.tools.widget.EmojiLayout;
import com.mao.tools.widget.NavigationViewPagerLayout;
import com.mao.tools.widget.ToolsFontLayout;
import com.mao.ui.base.BackActivity;
import com.mao.utils.MethodCompat;
import com.mao.utils.RandomUtils;
import com.mao.utils.SoftInputHelper;
import com.mao.widget.LayerFrameLayout;
import com.mao.widget.NoteTitleEditText;
import com.mao.widget.WordEditText;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author mao
 *
 */
public class NoteAddOrReviseActivity extends BackActivity {

	private final static String TAG = "NoteAddActivity";
	
	private NoteTitleEditText note_add_title;
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
	
	/** 当前Note对象 */
	private Note mNote;
	/** 保存当前笔记用到的所有文件的源路径与目标文件名map,key:目标文件名,value:源路径 */
	private Map<String, String> mfileUrlMap = new LinkedHashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_add_activity);
		
		initActionBar();
		
		initView();
		setEventForView();
		
		//初始化数据
		mNote = (Note) getIntent().getSerializableExtra("note");
		if(mNote == null) {
			mNote = new Note();
			mNote.setGUID(DBHelper.GUID());
		}
		//初始化编辑框内容
		initEditTextContent();
	}
	
	private void initActionBar() {
		setActionBarCenterText(R.string.write_note);
		TextView tv = setActionBarRightText("");
		if(tv != null) {
			Drawable left = MethodCompat.getDrawable(getApplicationContext(), R.drawable.ok_icon);
			left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
			tv.setCompoundDrawables(left, null, null, null);
			//保存
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(check()) {
						if(save()) {
							saveSuccessAfter();
						} else {
							Toast.makeText(getApplicationContext(), R.string.save_fail, Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		}
	}
	
	private void initView() {
		note_add_title = (NoteTitleEditText) findViewById(R.id.note_add_title);
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
		note_add_bottom_content.addView(mNavigationViewPager);
		note_add_bottom_content.addView(mEmojiLayout);
		
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
		ToolsFontLayout fontLayout = new ToolsFontLayout(this);
		fontLayout.setOnFontConfigurationListener(new AddNoteFontConfigurationListener());
		mToolsVPViews[0] = fontLayout;
		mNavigationViewPager.setData(mTitles, mToolsVPViews);
	}
	
	private void setEventForView() {
		//标题编辑框
		note_add_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideBottomLayoutAll();
			}
		});
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
				Intent intent = new Intent(NoteAddOrReviseActivity.this, SelectPictureActivity.class);
				startActivityForResult(intent, ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_REQUEST_CODE);
				//隐藏底部布局及键盘
				hideBottomLayoutAll();
				note_add_et.requestFocus();
				SoftInputHelper.hideSoftInput(getApplicationContext(), v);
			}
		});
		//底部表情按钮点击事件
		note_add_bottom_emoji.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleBottomLayout(mEmojiLayout);
				note_add_et.requestFocus();
				SoftInputHelper.hideSoftInput(getApplicationContext(), v);
			}
		});
		//底部工具按钮点击事件
		note_add_bottom_tools.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleBottomLayout(mNavigationViewPager);
				note_add_et.requestFocus();
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
			} else {
				if(note_add_title.hasModified() || note_add_et.hasModified()) {
					alertAskSaveDialog();
					return true;
				}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//从选择图片返回
		if(requestCode == ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_REQUEST_CODE
				&& resultCode == ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_RESULT_CODE
				&& data != null) {
			List<String> selectedUrlList = data.getStringArrayListExtra(SelectPictureActivity.SELECTED_URL_INTENT_NAME);
			//有图片
			if(selectedUrlList != null && selectedUrlList.size() > 0) {
				for(String srcPath : selectedUrlList) {
					//为每一张图片生成随机字符串作为图片名
					String filename = RandomUtils.generateDigits();
					//记录
					mfileUrlMap.put(filename, srcPath);
					//显示
					String displayText = getResources().getString(R.string.default_note_picture_display_text);
					note_add_et.insertPicture(srcPath, displayText, filename);
				}
			}
		}
	}
	
	//弹出提示是否保存对话框
	private void alertAskSaveDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.tip_message);
		//舍弃
		builder.setNegativeButton(R.string.abandon, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		//保存
		builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//
				if(check()) {
					if(save()) {
						saveSuccessAfter();
					} else {
						Toast.makeText(getApplicationContext(), R.string.save_fail, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		builder.show();
	}
	
	private boolean check() {
		//判断是否已登录
		User user = UserManager.getInstance().getCurrentUser();
		if(user == null) {
			return false;
		}
		String title = note_add_title.getText().toString().trim();
		if(TextUtils.isEmpty(title)) {
			Toast.makeText(getApplicationContext(), R.string.input_title_tip, Toast.LENGTH_SHORT).show();
			return false;
		}
		String content = note_add_et.getText().toString().trim();
		if(TextUtils.isEmpty(content)) {
			Toast.makeText(getApplicationContext(), R.string.input_content_tip, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean save() {
		//构建Note对象
		mNote.setTitle(note_add_title.getText().toString().trim());
		mNote.setContent(note_add_et.getText().toString().trim());
		mNote.setUpdateTime(System.currentTimeMillis());
		mNote.setUsername(UserManager.getInstance().getCurrentUser().getUsername());
		mNote.setCharacterStyleContent(new CharacterStyleParser().toString(note_add_et.getEditableText()));
		
		NoteLoader loader = NoteLoader.getInstance();
		//处理复制文件
		List<String> sourceList = CharacterStyleParser.parseImageSpanSource(note_add_et.getText());
		if(sourceList != null && sourceList.size() > 0) {
			Map<String, String> map = new LinkedHashMap<String, String>(sourceList.size());
			for(String source : sourceList) {
				String srcPath = mfileUrlMap.get(source);
				//注意srcPath可能为空,比如当该ImageSpan时一个表情图片时
				if(!TextUtils.isEmpty(srcPath)) {
					map.put(source, srcPath);
				}
			}
			//复制文件
			String path = DiskCacheManager.getInstance().getPictureCachePath(getApplicationContext(), mNote.getGUID());
			loader.createOrUpdateFile(getApplicationContext(), path, map);
		}

		//开始保存
		return loader.createOrUpdateDB(getApplicationContext(), mNote);
	}
	
	//保存成功后进行一些后续操作
	private void saveSuccessAfter() {
		Intent data = new Intent();
		data.putExtra("note", mNote);
		setResult(ActivityRequestResultCode.NOTE_LIST_TO_ADDORREVISE_ACTIVITY_RESULT_CODE, data);
		finish();
	}
	
	private void initEditTextContent() {
		String title = mNote.getTitle();
		//有标题
		if(!TextUtils.isEmpty(title)) {
			note_add_title.setText(title);
			note_add_title.setSelection(note_add_title.length());
			note_add_title.setModified(false);
		}
		CharacterStyleParser parser = new CharacterStyleParser();
		List<CharacterStyleParser.CharacterStyleEntry> characterStyleList = parser.toCharacterStyle(mNote.getCharacterStyleContent());
		String content = mNote.getContent();
		if(!TextUtils.isEmpty(content)) {
			SpannableString ss = new SpannableString(content);
			if(characterStyleList != null) {
				for(CharacterStyleParser.CharacterStyleEntry entry : characterStyleList) {
					CharacterStyle span = entry.getCharacterStyle();
					//ImageSpan特殊处理
					if(CharacterStyleParser.SpanType.ImageSpan.equals(entry.getCharacterStyleType())) {
						String source = ((ImageSpan)entry.getCharacterStyle()).getSource();
						//可能是表情
						span = note_add_et.generateEmojiImageSpan(source);
						//可能是图片
						String path = DiskCacheManager.getInstance().getPictureCachePath(getApplicationContext(), mNote.getGUID());
						if(span == null) {
							span = note_add_et.generatePictureImageSpan(path + source, source, DisplayUtils.getScreenWidthPixels(this));
						}
					}
					ss.setSpan(span, entry.getStart(), entry.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			note_add_et.requestAllowStyleable(false);
			note_add_et.setText(ss);
			note_add_et.setSelection(ss.length());
			note_add_et.requestAllowStyleable(true);
			note_add_et.setModified(false);
		}
	}
	
	@Override
	public void onActionBarBack() {
		if(note_add_title.hasModified() || note_add_et.hasModified()) {
			alertAskSaveDialog();
		} else {
			super.onActionBarBack();
		}
	}
}
