package com.mao.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.mao.adapter.SelectPictureGridViewAdapter;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.easyword.R;
import com.mao.screen.DisplayUtils;
import com.mao.ui.base.BackActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 选择图片Activity
 * 
 * @author mao
 *
 */
public class SelectPictureActivity extends BackActivity {

	private GridView mPicturesGridView;
	private SelectPictureGridViewAdapter mPicturesGridViewAdapter;
	private List<String> mPicturesUrlList = new ArrayList<String>();
	
	/** 列数 */
	private final static int COLUMN_NUM = 3;
	/** 列间间距，单位：dip */
	private final static float COLUMN_INTERVAL = 2;
	/** 行间间距，单位：dip */
	private final static float ROW_INTERVAL = 5;
	
	/** 传递被选择的url集合时使用的Intent键名 */
	public final static String SELECTED_URL_INTENT_NAME = "selectedUrlList";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		//初始化GridView
		initPicturesGridView();
		setContentView(mPicturesGridView);
		
		setActionBarCenterText(R.string.select_picture);
		TextView okTv = setActionBarRightText(R.string.ok);
		setOkClickEvent(okTv);
		
		//初始化数据,异步操作,并行执行
		new ReadPicturesAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{});
	}
	
	private void initPicturesGridView() {
		mPicturesGridView = new GridView(getApplicationContext());
		mPicturesGridView.setNumColumns(COLUMN_NUM);
		mPicturesGridViewAdapter = new SelectPictureGridViewAdapter(this, mPicturesUrlList);
		int horizontalSpacing = DisplayUtils.dp2px(getApplicationContext(), COLUMN_INTERVAL);
		int verticalSpacing = DisplayUtils.dp2px(getApplicationContext(), ROW_INTERVAL);
		int screenWidth = DisplayUtils.getScreenWidthPixels(this) / COLUMN_NUM;
		mPicturesGridView.setHorizontalSpacing(horizontalSpacing);
		mPicturesGridView.setVerticalSpacing(verticalSpacing);
		int length = screenWidth;
		if(COLUMN_NUM > 0) {
			length -= horizontalSpacing * (COLUMN_NUM - 1);
		}
		mPicturesGridViewAdapter.setPictureSideLength(length);
		mPicturesGridView.setAdapter(mPicturesGridViewAdapter);
	}
	
	private void addPictureUrl(String url) {
		if(!TextUtils.isEmpty(url)) {
			File f = new File(url);
			if(f.exists() && f.isFile()) {
				mPicturesUrlList.add(url);
			}
		}
	}
	
	private void setOkClickEvent(TextView okTv) {
		if(okTv != null) {
			okTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent data = null;
					if(mPicturesGridViewAdapter != null) {
						Set<String> selectedUrlSet = mPicturesGridViewAdapter.getSelectedUrlSet();
						if(selectedUrlSet != null) {
							ArrayList<String> list = new ArrayList<String>();
							for(String url : selectedUrlSet) {
								list.add(url);
							}
							data = new Intent();
							data.putExtra(SELECTED_URL_INTENT_NAME, list);
						}
					}
					setResult(ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_RESULT_CODE, data);
					finish();
				}
			});
		}
	}
	
	private class ReadPicturesAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			initData();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mPicturesGridViewAdapter.notifyDataSetChanged();
		}
	}
	
	private void initData() {
		Cursor cursor = null;
		try {
			Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			// 只查询jpeg和png的图片
			cursor = getContentResolver().query(uri, null, 
					MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
					new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
			if(cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
				while(!cursor.isAfterLast()) {
					String url = cursor.getString(index);
					addPictureUrl(url);
					if(!cursor.moveToNext()) {
						break;
					}
				}
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("resultCode:" + resultCode);
		if(requestCode == ActivityRequestResultCode.TAKE_PHOTO_ACTIVITY_REQUEST_CODE && resultCode == -1) {
			String url = mPicturesGridViewAdapter.getTakePhotoUrl();
			System.out.println("url:" + url);
			if(!TextUtils.isEmpty(url)) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(url);
				data = new Intent();
				data.putExtra(SELECTED_URL_INTENT_NAME, list);
				setResult(ActivityRequestResultCode.SELECT_PICTURE_ACTIVITY_RESULT_CODE, data);
				finish();
			}
		}
	}
	
}
