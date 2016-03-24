package com.mao.service;

import java.util.List;

import com.mao.bean.Note;
import com.mao.loader.NoteLoader;

import android.app.IntentService;
import android.content.Intent;
import de.greenrobot.event.EventBus;

/**
 * 获取笔记列表IntentService
 * 
 * @author mao
 *
 */
public class NoteListIntentService extends IntentService {

	private final static String TAG = "NoteListIntentService";
	
	private final static String DEFAULT_NAME = "NoteListIntentService";
	
	public NoteListIntentService() {
		this(DEFAULT_NAME);
	}
	
	public NoteListIntentService(String name) {
		super(name);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		List<Note> noteList = NoteLoader.getInstance().queryAll(getApplicationContext());
		EventBus.getDefault().post(noteList);
	}

}
