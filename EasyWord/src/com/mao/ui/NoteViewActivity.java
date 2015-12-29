package com.mao.ui;

import com.mao.bean.Note;
import com.mao.cache.DiskCacheManager;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.easyword.R;
import com.mao.eventbus.NoteEntry;
import com.mao.loader.DefaultImageSpanHandler;
import com.mao.loader.ImageSpanGenerator;
import com.mao.loader.SpannedLoader;
import com.mao.screen.DisplayUtils;
import com.mao.ui.base.BackActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * <p>查看笔记Activity</p>
 * 你必须使用以下代码传递要查看的笔记数据过来:
 * <pre>
 * 	intent.put("note", value);
 * </pre>
 * 
 * @author mao
 *
 */
public class NoteViewActivity extends BackActivity{

	private final static String TAG = "NoteViewActivity";
	
	private Note mNote;
	
	private TextView note_view_title;
	private TextView note_view_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_view_activity);
		
		checkData();
		initActionBar();
		initView();
		updateUI();
	}
	
	//检查数据
	private void checkData() {
		mNote = (Note) getIntent().getSerializableExtra("note");
		if(mNote == null) {
			finish();
		}
	}
	
	private void initActionBar() {
		setActionBarCenterText(R.string.lookup_note);
		TextView tv = setActionBarRightText(R.string.revise);
		if(tv != null) {
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NoteViewActivity.this, NoteAddOrReviseActivity.class);
					intent.putExtra("note", mNote);
					//startActivityForResult(intent, ActivityRequestResultCode.NOTE_VIEW_TO_REVISE_ACTIVITY_REQUEST_CODE);
					
					startActivity(intent);
				}
			});
		}
	}
	
	private void initView() {
		note_view_title = (TextView) findViewById(R.id.note_view_title);
		note_view_content = (TextView) findViewById(R.id.note_view_content);
	}
	
	private void updateUI() {
		
		//设置标题
		if(note_view_title != null) {
			note_view_title.setText(mNote.getTitle());
		}
		//设置内容
		if(note_view_content != null) {
			SpannedLoader spannedLoader = new SpannedLoader();
			String basePath = DiskCacheManager.getInstance().getPictureCachePath(getApplicationContext(), mNote.getGUID());
			DefaultImageSpanHandler imageSpanHandler = new DefaultImageSpanHandler(getApplicationContext(), basePath);
			imageSpanHandler.setOnImageMeasureListener(
					ImageSpanGenerator.getDefaultImageMeasureListener(
							DisplayUtils.getScreenWidthPixels(this)));
			Spanned span = spannedLoader.loadSpanned(mNote.getContent(), mNote.getCharacterStyleContent(), imageSpanHandler);
			note_view_content.setText(span);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateUI();
	}
	
	public void onEventMainThread(NoteEntry entry) {
		
		if(entry != null && entry.getNewNote() != null) {
			mNote = entry.getNewNote();
			updateUI();
		}
	}
	
	@Override
	protected boolean requestRegisterEventBus() {
		return true;
	}
}
