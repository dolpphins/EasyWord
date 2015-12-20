package com.mao.ui;

import com.mao.adapter.NoteExpandableListViewAdapter;
import com.mao.easyword.R;
import com.mao.ui.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

public class MainActivity extends BaseActivity {
	
	private ExpandableListView main_note_lv;
	private ImageView main_note_add;
	
	private BaseExpandableListAdapter mListViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list_activity);
		
		initView();
		
		setEventForView();
	}
	
	private void initView() {
		setActionBarCenterText(R.string.all_document);
		
		main_note_lv = (ExpandableListView) findViewById(R.id.main_note_lv);
		main_note_add = (ImageView) findViewById(R.id.main_note_add);
		
		mListViewAdapter = new NoteExpandableListViewAdapter();
		main_note_lv.setAdapter(mListViewAdapter);
	}
	
	private void setEventForView() {
		
		main_note_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NoteAddActivity.class);
				startActivity(intent);
			}
		});
	}
}
