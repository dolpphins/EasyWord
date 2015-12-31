package com.mao.ui;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.mao.adapter.PersonalityInfoAdapter;
import com.mao.bean.User;
import com.mao.cache.DiskCacheManager;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.easyword.R;
import com.mao.easyword.UserManager;
import com.mao.ui.base.BackActivity;
import com.mao.utils.FileUtils;
import com.mao.utils.ImageUtils;
import com.mao.utils.IoUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 个人信息Activity
 * 
 * @author mao
 *
 */
public class PersonalityActivity extends BackActivity {

	private ListView app_personlity_lv;
	private PersonalityInfoAdapter mPersonalityInfoAdapter;
	
	private Uri mHeadPictureUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_personality_activity);
		initActionBar();
		initView();
		setClickEventForListView();
	}
	
	private void initActionBar() {
		setActionBarCenterText(R.string.personality_info);
	}
	
	private void initView() {
		app_personlity_lv = (ListView) findViewById(R.id.app_personlity_lv);
		
		User user = UserManager.getInstance().getCurrentUser();
		mPersonalityInfoAdapter = new PersonalityInfoAdapter(this, user);
		app_personlity_lv.setAdapter(mPersonalityInfoAdapter);
	}
	
	private void setClickEventForListView() {
		app_personlity_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				//更换头像
				case 0:
					handleForClickHead();
					break;
				//更换性别
				case 1:
					handleForClickSex();
					break;
				//更换手机
				case 2:
					handleForClickPhone();
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void handleForClickHead() {

		final User user = UserManager.getInstance().getCurrentUser();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] options = new String[]{"拍照", "从相册中选择"};
		builder.setTitle("请选择");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String filePath = DiskCacheManager.getInstance().getCacheRootPath(getApplicationContext());
				String fileName = user.getUsername();
				switch(which)
				{
				// 调用系统相机拍照更换头像
				case 0:
					mHeadPictureUri = PicturesHelper.takePhotoByCamera(PersonalityActivity.this, filePath, fileName, ActivityRequestResultCode.TAKE_PHOTO_ACTIVITY_REQUEST_CODE);
					break;
				// 从相册中选择更换头像
				case 1:
					PicturesHelper.takePhotoByGallery(PersonalityActivity.this, filePath, fileName, ActivityRequestResultCode.GALLERY_ACTIVITY_REQUEST_CODE);
					break;
				}
			}
		});
		builder.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == -1) {
			//拍照
			if (requestCode == ActivityRequestResultCode.TAKE_PHOTO_ACTIVITY_REQUEST_CODE) {
				PicturesHelper.crop(this, mHeadPictureUri, ActivityRequestResultCode.CROP_ACTIVITY_REQUEST_CODE);
			//从相册选择
			} else if(requestCode == ActivityRequestResultCode.GALLERY_ACTIVITY_REQUEST_CODE && data != null) {
				PicturesHelper.crop(this, data.getData(), ActivityRequestResultCode.CROP_ACTIVITY_REQUEST_CODE);
			//裁切
			} else if(requestCode == ActivityRequestResultCode.CROP_ACTIVITY_REQUEST_CODE && data != null) {
				Bitmap bm = data.getParcelableExtra("data");
				updateHeadPicture(bm);
			}
		}
	}
	
	private void updateHeadPicture(Bitmap bm) {
		User user = UserManager.getInstance().getCurrentUser();
		int quality = getHeadPictureCompressQuality(bm);
		final String path = DiskCacheManager.getInstance().getMainDirectory() + user.getUsername();
		boolean success = ImageUtils.saveBitmap(bm, quality, path);
		if(success) {
			BmobProFile.getInstance(getApplicationContext()).upload(path, new UploadListener() {
				
				@Override
				public void onError(int arg0, String arg1) {	
					FileUtils.removeFileOrDirectory(path);
				}
				
				@Override
				public void onSuccess(String arg0, String arg1, BmobFile arg2) {
					updateUserHeadPicture(path, arg2.getUrl());
				}
				
				@Override
				public void onProgress(int progress) {		
					System.out.println("progress:" + progress);
				}
			});
		}
	}
	
	private int getHeadPictureCompressQuality(Bitmap bm) {
		long size = ImageUtils.sizeOfBitmap(bm);
		//小于128KB
		if(size < 128 * 1024) {
			return 100;
		//小于512KB
		} else if(size <  512 * 1024) {
			return 50;
		} else if(size < 10 * 1024 * 1024) {
			return 20;
		} else {
			return 5;
		}
	}
	
	private void updateUserHeadPicture(final String path, final String url) {
		User user = IoUtils.copyObject(UserManager.getInstance().getCurrentUser());
		if(user != null) {
			user.setHpUrl(url);
			user.update(getApplicationContext(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					UserManager.getInstance().getCurrentUser().setHpUrl(url);
					mPersonalityInfoAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					FileUtils.removeFileOrDirectory(path);
				}
			});
		}
	}
	
	private void handleForClickSex() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		final Resources res = getResources();
		builder.setTitle(R.string.select_sex);
		final String[] items = new String[]{res.getString(R.string.male), res.getString(R.string.female)};
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0) {
					//男
					updateSex(res.getString(R.string.male));
				} else if(which == 1) {
					updateSex(res.getString(R.string.female));
				}
			}
		});
		builder.show();
	}
	
	private void updateSex(final String sex) {
		User user = IoUtils.copyObject(UserManager.getInstance().getCurrentUser());
		if(user != null) {
			user.setSex(sex);
			user.update(getApplicationContext(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					UserManager.getInstance().getCurrentUser().setSex(sex);
					mPersonalityInfoAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
				}
			});
		}
	}
	
	private void handleForClickPhone() {

		User user = UserManager.getInstance().getCurrentUser();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		Resources res = getResources();
		builder.setTitle(res.getString(R.string.change_phone_tip));
		final EditText et = new EditText(this);
		et.setText(user.getPhone());
		et.setMaxLines(15);
		et.setSingleLine();
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		et.setBackgroundResource(R.drawable.app_common_et_bg);
		et.setBackgroundColor(Color.TRANSPARENT);
		et.setSelection(et.getText().length());
		builder.setView(et);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String phone = et.getText().toString().trim();
				if(!TextUtils.isEmpty(phone)) {
					updatePhone(phone);
				}
			}
		});
		
		builder.show();
	}
	
	private void updatePhone(final String phone) {
		User user = IoUtils.copyObject(UserManager.getInstance().getCurrentUser());
		if(user != null) {
			user.setPhone(phone);
			user.update(getApplicationContext(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					UserManager.getInstance().getCurrentUser().setPhone(phone);
					mPersonalityInfoAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
				}
			});
		}
	}
}
