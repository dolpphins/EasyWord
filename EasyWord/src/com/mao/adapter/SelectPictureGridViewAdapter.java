package com.mao.adapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mao.cache.MemoryPicturesCacheManager;
import com.mao.conf.ActivityRequestResultCode;
import com.mao.easyword.R;
import com.mao.ui.PicturesHelper;
import com.mao.utils.ImageUtils;
import com.mao.utils.TextUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 选择图片GridView适配器
 * 
 * @author mao
 *
 */
public class SelectPictureGridViewAdapter extends BaseAdapter {

	private Activity mActivity;
	private List<String> mPicturesUrlList;
	
	private int mSideLength = 320;
	
	private Set<String> mSelectedUrlSet = new HashSet<String>();
	private String mTakePhotoUrl;//拍照存放的位置
	
	public SelectPictureGridViewAdapter(Activity at, List<String> picturesUrlList) {
		mActivity = at;
		mPicturesUrlList = picturesUrlList;
	}
	
	@Override
	public int getCount() {
		if(mPicturesUrlList != null) {
			return mPicturesUrlList.size() + 1;
		}
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return mPicturesUrlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//特殊处理
		if(position == 0) {
			RelativeLayout layout = new RelativeLayout(mActivity);
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(mSideLength, mSideLength);
			layout.setLayoutParams(params);
			layout.setGravity(Gravity.CENTER);
			ImageView camera = new ImageView(mActivity);
			Matrix matrix = new Matrix(camera.getMatrix());
			matrix.setScale(1.0f, 1.0f);
			camera.setImageMatrix(matrix);
			camera.setImageResource(R.drawable.camera_icon);
			
			layout.addView(camera);
			setClickEventForTakePhoto(camera);
			return layout;
		}
		
		String url = mPicturesUrlList.get(position - 1);
		
		ImageView iv = null;
		
		//if(convertView == null) {
			//iv = new ImageView(mContext);
			View v = LayoutInflater.from(mActivity).inflate(R.layout.select_pictures_gridview_item, null);
			iv = (ImageView) v.findViewById(R.id.select_pictures_gridview_item_iv);
			ImageView select_pictures_gridview_item_sel = (ImageView) v.findViewById(R.id.select_pictures_gridview_item_sel);
			ViewGroup.LayoutParams params = new AbsListView.LayoutParams(mSideLength, mSideLength);
			v.setLayoutParams(params);
			setClickEvent(select_pictures_gridview_item_sel, url);
//		//} else {
//			//iv = (ImageView) convertView;
//		//}
		iv.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.empty_picture));
		
		MemoryPicturesCacheManager mpcm = MemoryPicturesCacheManager.getInstance();
		Bitmap bm = mpcm.get(url);    
		if(bm == null) {
			new DecodeBitmpaAysncTask(url, iv).execute(new Void[]{});
			//System.out.println("not in memory cache");
		} else {
			iv.setImageBitmap(bm);
			//System.out.println("in memory cache");
		}
		
		if(mSelectedUrlSet.contains(url)) {
			select_pictures_gridview_item_sel.setImageResource(R.drawable.ps);
		} else {
			select_pictures_gridview_item_sel.setImageResource(R.drawable.pt);
		}
		
		return v;
	}
	
	//拍照点击事件
	private void setClickEventForTakePhoto(View v) {
		if(v != null) {
			v.setClickable(true);
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mTakePhotoUrl = "123.jpg";
					PicturesHelper.takePhotoByCamera(mActivity, "", mTakePhotoUrl, ActivityRequestResultCode.TAKE_PHOTO_ACTIVITY_REQUEST_CODE);
				}
			});
		}
	}
	
	//右上角图标点击事件
	private void setClickEvent(final ImageView iv, final String url) {
		if(iv != null) {
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mSelectedUrlSet.contains(url)) {
						if(mSelectedUrlSet.remove(url)) {
							iv.setImageResource(R.drawable.pt);
						}
					} else {
						if(mSelectedUrlSet.add(url)) {
							iv.setImageResource(R.drawable.ps);
						}
					}
				}
			});
		}
	}
	
	private Bitmap decodeBitmap(String url) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(url, opt);
		
		int width = opt.outWidth;
		int height = opt.outHeight;
		opt.inJustDecodeBounds = false;
		int widthRatio = Math.round((float) width / (float) mSideLength);
		int heightRatio = Math.round((float) height / (float) mSideLength);
		opt.inSampleSize = (widthRatio < heightRatio ? widthRatio : heightRatio) * 3;
		
		Bitmap bm = BitmapFactory.decodeFile(url, opt);		
		
		if(bm != null) {
			String s = TextUtils.convertByteCount(ImageUtils.sizeOfBitmap(bm));
			System.out.println(s);
		}
		
		return bm;
	}
	
	/**
	 * 设置图片边长
	 * 
	 * @param length 要设置的长度
	 */
	public void setPictureSideLength(int length) {
		mSideLength = length;
	}
	
	private class DecodeBitmpaAysncTask extends AsyncTask<Void, Void, Bitmap> {
		
		private String url;
		private ImageView iv;
		
		public DecodeBitmpaAysncTask(String url, ImageView iv) {
			this.url = url;
			this.iv = iv;
		}
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			return decodeBitmap(url);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			iv.setImageBitmap(result);
			MemoryPicturesCacheManager mpcm = MemoryPicturesCacheManager.getInstance();
			
			//缓存
			long availableMemory = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory();
			long size = ImageUtils.sizeOfBitmap(result);
			if(availableMemory > 0 && size > 0) {
				if(availableMemory / size >= 50) {
					mpcm.put(url, result);
				}
			}
		}
	}
	
	/**
	 * 获取选择的图片url集合
	 * 
	 * @return 返回选择的图片url集合
	 */
	public Set<String> getSelectedUrlSet() {
		return mSelectedUrlSet;
	}
	
	/**
	 * 获取拍照图片存放的位置
	 * 
	 * @return 返回拍照图片存放的位置
	 */
	public String getTakePhotoUrl() {
		return mTakePhotoUrl;
	}
	
	private static class ViewHolder {
		
		private ImageView select_pictures_gridview_item_iv;
		private ImageView select_pictures_gridview_item_sel;
	}
}



















