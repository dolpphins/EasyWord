package com.mao.ui;

import java.util.ArrayList;
import java.util.List;

import com.mao.cache.DiskCacheManager;
import com.mao.easyword.R;
import com.mao.executor.ImageLoader;
import com.mao.ui.base.BackActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * <p>查看本地图片Activity</p>
 * 你应该使用以下代码传递要显示的图片信息过来:
 * <pre>
 * 	intent.putExtra("picturesUrlList", value);
 * </pre>
 * 通过如下代码可以指定初始要显示哪张图片:
 * <pre>
 * 	intent.putExtra("currentIndex", value);
 * </pre>
 * 
 * @author mao
 *
 */
public class DisplayPicturesActivity extends BackActivity implements OnPageChangeListener {

	private final static String TAG = "DisplayPicturesActivity";
		
	//数据集
	private List<String> picturesUrlList;
	//PhotoViewAttacher集合
	private List<PhotoViewAttacher> photoViewAttacherList;
	
	private ViewPager app_dispaly_pictures_viewpager;

	private RelativeLayout[] mWrapLayouts;
	private ImageView[] mImageViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_dispaly_pictures_activity);
		setActionBarBackgroundColor(Color.parseColor("#000000"));
		
		app_dispaly_pictures_viewpager = (ViewPager) findViewById(R.id.app_dispaly_pictures_viewpager);
			
		picturesUrlList = getIntent().getStringArrayListExtra("picturesUrlList");
		
		if(picturesUrlList != null) {
			
			int currentIndex = getIntent().getIntExtra("currentIndex", 0);
			app_dispaly_pictures_viewpager.setOnPageChangeListener(this);
			app_dispaly_pictures_viewpager.setAdapter(new PicturesViewPagerAdapter());
			app_dispaly_pictures_viewpager.setCurrentItem(currentIndex);
			setTitleTip(currentIndex);
			createImageViews();
			//开始获取图片
			//String[] params = (String[]) picturesUrlList.toArray();//报类转型异常
			//String[] params = new String[picturesUrlList.size()];
			for(int i = 0; i < picturesUrlList.size(); i++) {
				//params[i] = picturesUrlList.get(i);
				ImageLoader.getInstance().displayImage(mImageViews[i], picturesUrlList.get(i));
			}
		}
	}
	
	private void createImageViews() {
		photoViewAttacherList = new ArrayList<PhotoViewAttacher>();
		mWrapLayouts = new RelativeLayout[picturesUrlList.size()];
		mImageViews = new ImageView[picturesUrlList.size()];
		for(int i = 0; i < mImageViews.length; i++) {
			mWrapLayouts[i] = new RelativeLayout(getApplicationContext());
			mWrapLayouts[i].setBackgroundColor(Color.parseColor("#000000"));
			mImageViews[i] = new ImageView(getApplicationContext());
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			mImageViews[i].setLayoutParams(params);
			mWrapLayouts[i].addView(mImageViews[i]);
			PhotoViewAttacher attacher = new PhotoViewAttacher(mImageViews[i]);
			photoViewAttacherList.add(attacher);
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int position) {
		setTitleTip(position);
	}
	
	//注意position从0开始
	private void setTitleTip(int position) {
		int index = 0;
		if(picturesUrlList.size() > 0) {
			index = position + 1;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(index);
		sb.append("/");
		sb.append(picturesUrlList.size());
		TextView tv = setActionBarCenterText(sb.toString());
		if(tv != null) {
			tv.setTextColor(Color.parseColor("#ffffff"));
		}
	}
	
	private class PicturesViewPagerAdapter extends PagerAdapter {
		
		@Override
		public int getCount() {
			if(picturesUrlList != null) {
				return picturesUrlList.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mWrapLayouts[position % mWrapLayouts.length]);
			
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mWrapLayouts[position % mWrapLayouts.length], 0);
			return mWrapLayouts[position % mWrapLayouts.length];
		}
		
	}
}
