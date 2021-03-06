package com.mao.ui;

import java.util.ArrayList;
import java.util.List;

import com.mao.adapter.NoteListViewAdapter;
import com.mao.adapter.SlidingMenuAdapter;
import com.mao.bean.Note;
import com.mao.bean.User;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.easyword.R;
import com.mao.easyword.UserManager;
import com.mao.eventbus.NoteEntry;
import com.mao.executor.ImageLoader;
import com.mao.service.NoteListIntentService;
import com.mao.ui.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 主Activity,即笔记列表
 * 
 * @author mao
 *
 */
public class MainActivity extends BaseActivity {
	
	private final static String TAG = "MainActivity";
	
	private DrawerLayout mDrawer;
	private View mActionBar;
	private ImageView mHeadPictrueIv;
	
	private List<Note> mNoteList;
	private ListView main_note_lv;
	private BaseAdapter mNoteListViewAdapter;
	private ImageView main_note_add;
	
	/**
	 * 侧滑菜单
	 */
	private View slide_menu_personality;
	private ListView slide_menu_lv;
	private ImageView slide_menu_headpicture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list_activity);
		
		initActionBar();
		
		initView();
		
		setEventForView();
		
		//异步获取数据
		Intent intent = new Intent(this, NoteListIntentService.class);
		startService(intent);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateUI();
	}
	
	public void onEventMainThread(List<Note> noteList) {
		if(noteList != null) {
			mNoteList.clear();
			mNoteList.addAll(noteList);
			//更新UI
			updateUI();
		}
	}
	
	//如果是新建Note而不是修改则preNote为null
	public void onEventMainThread(NoteEntry entry) {
		
		if(entry != null && entry.getNewNote() != null) {
			createOrUpdateList(entry.getNewNote());
		}
	}
	
	private void updateUI() {
		if(mNoteListViewAdapter != null) {
			mNoteListViewAdapter.notifyDataSetChanged();
		}
		//侧滑菜单头像
		String url = UserManager.getInstance().getCurrentUser().getHpUrl();
		ImageLoader.getInstance().displayImage(slide_menu_headpicture, url);
	}
	
	private void initActionBar() {
		//头像
		mHeadPictrueIv = (ImageView) findViewById(R.id.app_common_actionbar_left_iv);
	}
	
	private void initView() {
		//setActionBarCenterText(R.string.all_document);
		
		mDrawer = (DrawerLayout) findViewById(R.id.drawer);
		main_note_lv = (ListView) findViewById(R.id.main_note_lv);
		main_note_add = (ImageView) findViewById(R.id.main_note_add);
		
		mNoteList = new ArrayList<Note>();
		mNoteListViewAdapter = new NoteListViewAdapter(getApplicationContext(), mNoteList);
		main_note_lv.setAdapter(mNoteListViewAdapter);
		
		initSlidingMenu();
	}
	
	private void initSlidingMenu() {
		slide_menu_personality = findViewById(R.id.slide_menu_personality);
		
		slide_menu_headpicture = (ImageView) findViewById(R.id.slide_menu_headpicture);
		TextView slide_menu_username = (TextView) findViewById(R.id.slide_menu_username);
		
		slide_menu_headpicture.setImageResource(R.drawable.avatar_default);
		User user = UserManager.getInstance().getCurrentUser();
		slide_menu_username.setText(user.getUsername());
		
		slide_menu_lv = (ListView) findViewById(R.id.slide_menu_lv);
		slide_menu_lv.setAdapter(new SlidingMenuAdapter(getApplicationContext()));
	}
	
	private void setEventForView() {
		//添加笔记点击事件
		main_note_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NoteAddOrReviseActivity.class);
				//startActivityForResult(intent, ActivityRequestResultCode.NOTE_LIST_TO_ADDORREVISE_ACTIVITY_REQUEST_CODE);
				startActivity(intent);
			}
		});
		//ListView Item点击事件
		main_note_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, NoteViewActivity.class);
				intent.putExtra("note", mNoteList.get(position));
				startActivityForResult(intent, ActivityRequestResultCode.NOTE_LIST_TO_VIEW_ACTIVITY_REQUEST_CODE);
			}
		});
		//左上角头像点击
		mHeadPictrueIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleDrawer(Gravity.START);
			}
		});
		//侧滑菜单顶部
		slide_menu_personality.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, PersonalityActivity.class);
				startActivity(intent);
				mDrawer.closeDrawer(Gravity.START);
			}
		});
		//侧滑菜单Item点击事件
		slide_menu_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					mDrawer.closeDrawer(Gravity.START);
					break;
				case 1:
					Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
					startActivity(intent);
					mDrawer.closeDrawer(Gravity.START);
					break;
				default:
					break;
				}
			}
		});
	}
	
	//打开或者关闭Drawer
	private void toggleDrawer(int gravity) {
		if(mDrawer.isDrawerOpen(gravity)) {
			mDrawer.closeDrawer(gravity);
		} else {
			mDrawer.openDrawer(gravity);
		}
	}
	
	private boolean createOrUpdateList(Note note) {
		int length = mNoteList.size();
		for(int i = 0; i < length; i++) {
			//已经存在,那么更新
			if(mNoteList.get(i).getGUID().equals(note.getGUID())) {
				mNoteList.set(i, note);
				//移到最前面
				Note temp = mNoteList.get(i);
				mNoteList.remove(i);
				mNoteList.add(0, temp);
				return true;
			}
		}
		//不存在,那么添加
		mNoteList.add(0, note);
		return true;
	}
	
	@Override
	protected boolean requestRegisterEventBus() {
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(mDrawer.isDrawerOpen(Gravity.START)) {
				mDrawer.closeDrawer(Gravity.START);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
