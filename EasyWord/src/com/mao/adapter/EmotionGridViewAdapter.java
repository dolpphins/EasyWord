package com.mao.adapter;

import com.mao.conf.EmotionConfiguration;
import com.mao.screen.DisplayUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * <p>
 * 	ViewPager某一表情页GridView适配器
 * </p>
 * 
 * @author 麦灿标
 * */
public class EmotionGridViewAdapter extends BaseAdapter{

	private final static String TAG = "EmotionGridViewAdapter";
	
	private Context mContext;
	
	private int mCurrentPageIndex;
	
	/**
	 * 构造函数
	 * 
	 * @param context 上下文
	 * @param pageIndex 当前页索引,注意该值必须有意义
	 * */
	public EmotionGridViewAdapter(Context context, int pageIndex) {
		mContext = context;
		mCurrentPageIndex = pageIndex;
	}
	
	@Override
	public int getCount() {
		return EmotionConfiguration.getEmotionCountOnPage(mCurrentPageIndex);
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(mContext);
		int padding = DisplayUtils.dp2px(mContext, EmotionConfiguration.EmotionPadding);
		iv.setPadding(padding, padding, padding, padding);
		Bitmap bm = EmotionConfiguration.getEmotionBitmapOnPosition(mContext, mCurrentPageIndex, position);
		iv.setImageBitmap(bm);
		
		return iv;
	}
	
}
