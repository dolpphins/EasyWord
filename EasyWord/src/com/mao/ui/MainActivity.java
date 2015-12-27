package com.mao.ui;

import java.util.ArrayList;
import java.util.List;

import com.mao.adapter.NoteListViewAdapter;
import com.mao.bean.Note;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.easyword.R;
import com.mao.service.NoteListIntentService;
import com.mao.ui.base.BackActivity;
import com.mao.ui.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import de.greenrobot.event.EventBus;

/**
 * 主Activity,即笔记列表
 * 
 * @author mao
 *
 */
public class MainActivity extends BaseActivity {
	
	private final static String TAG = "MainActivity";
	
	private List<Note> mNoteList;
	private ListView main_note_lv;
	private BaseAdapter mNoteListViewAdapter;
	private ImageView main_note_add;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list_activity);
		
		initView();
		
		setEventForView();
		
		registerEventBus();
		//异步获取数据
		Intent intent = new Intent(this, NoteListIntentService.class);
		startService(intent);
	}
	
	private void registerEventBus() {
		if(!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
	}
	
	private void unRegisterEventBus() {
		if(EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
	}
	
	public void onEventMainThread(List<Note> noteList) {
		if(noteList != null) {
			mNoteList.clear();
			mNoteList.addAll(noteList);
			//更新UI
			updateUI();
		}
	}
	
	private void updateUI() {
		if(mNoteListViewAdapter != null) {
			mNoteListViewAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterEventBus();
	}
	
	private void initView() {
		setActionBarCenterText(R.string.all_document);
		
		main_note_lv = (ListView) findViewById(R.id.main_note_lv);
		main_note_add = (ImageView) findViewById(R.id.main_note_add);
		
		mNoteList = new ArrayList<Note>();
		mNoteListViewAdapter = new NoteListViewAdapter(getApplicationContext(), mNoteList);
		main_note_lv.setAdapter(mNoteListViewAdapter);
	}
	
	private void setEventForView() {
		//添加笔记点击事件
		main_note_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NoteAddOrReviseActivity.class);
				startActivityForResult(intent, ActivityRequestResultCode.NOTE_LIST_TO_ADDORREVISE_ACTIVITY_REQUEST_CODE);
			}
		});
		//ListView Item点击事件
		main_note_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, NoteAddOrReviseActivity.class);
				intent.putExtra("note", mNoteList.get(position));
				startActivityForResult(intent, ActivityRequestResultCode.NOTE_LIST_TO_ADDORREVISE_ACTIVITY_REQUEST_CODE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//从NoteAddOrReviseActivity返回且已修改数据
		if(requestCode == ActivityRequestResultCode.NOTE_LIST_TO_ADDORREVISE_ACTIVITY_REQUEST_CODE
				&& resultCode == ActivityRequestResultCode.NOTE_LIST_TO_ADDORREVISE_ACTIVITY_RESULT_CODE
				&& data != null) {
			Note note = (Note) data.getSerializableExtra("note");
			if(note != null) {
				if(createOrUpdateList(note)) {
					//更新UI
					updateUI();
				}
			}
		}
	}
	
	private boolean createOrUpdateList(Note note) {
		int length = mNoteList.size();
		for(int i = 0; i < length; i++) {
			//已经存在,那么更新
			if(mNoteList.get(i).getGUID().equals(note.getGUID())) {
				mNoteList.set(i, note);
				return true;
			}
		}
		//不存在,那么添加
		mNoteList.add(0, note);
		return true;
	}
}
