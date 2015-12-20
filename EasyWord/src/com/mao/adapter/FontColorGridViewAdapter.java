package com.mao.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mao.bean.Size;
import com.mao.easyword.R;
import com.mao.screen.FontScreen;
import com.mao.utils.ImageUtils;
import com.mao.utils.MethodCompat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FontColorGridViewAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Integer> mFontColorList;
	
	private int totalWidth;
	private int numColumns;
	private int interval;
	
	private int mLastDrawableID;
	
	//当前字体颜色
	private int mCurrentFontColor;
	
	public FontColorGridViewAdapter(Context context, List<Integer> fontColorList) {
		mContext = context;
		//复制
		mFontColorList = new ArrayList<Integer>();
		mFontColorList.addAll(fontColorList);
	}
	
	@Override
	public int getCount() {
		if(mFontColorList != null) {
			return mFontColorList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mFontColorList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = null;
		ViewGroup.LayoutParams params;
		if(convertView == null) {
			iv = new ImageView(mContext);
			params = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			iv.setLayoutParams(params);
		} else {
			iv = (ImageView) convertView;
			params = iv.getLayoutParams();
		}
		Size size = FontScreen.getFontColorItemSize(totalWidth, numColumns, interval);
		params.width = size.getWidth();
		params.height = size.getHeight();
		
		//最后一个
		if(position == mFontColorList.size() - 1) {
			iv.setImageDrawable(MethodCompat.getDrawable(mContext, mLastDrawableID));
		} else {
			Integer color = (Integer) getItem(position);
			List<Bitmap> bmList = new ArrayList<Bitmap>();
			Bitmap bm = Bitmap.createBitmap(params.width, params.height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bm);
			canvas.drawColor(color);
			bmList.add(bm);
			//设置选中状态
			if(color.equals(mCurrentFontColor)) {
				bmList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.font_color_sel));
			}
			
			bm = ImageUtils.createOverlapBitmap(params.width, params.height, bmList);
			iv.setImageBitmap(bm);
			
			//if(bm != null && !bm.isRecycled()) {
			//	bm.recycle();
			//}
			//回收
			for(Bitmap bitmap : bmList) {
				if(!bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
		
		return iv;
	}

	/**
	 * 设置GridView占用的总宽度,单位:px
	 * 
	 * @param totalWidth
	 * @return 当前适配器对象
	 */
	public FontColorGridViewAdapter setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
		return this;
	}
	
	/**
	 * GridView列数
	 * 
	 * @param numColumns
	 * @return 当前适配器对象
	 */
	public FontColorGridViewAdapter setNumColumns(int numColumns) {
		this.numColumns = numColumns;
		return this;
	}
	
	/**
	 * GridView两列之间的间隔,单位:px
	 * 
	 * @param interval
	 * @return 当前适配器对象
	 */
	public FontColorGridViewAdapter setNumInterval(int interval) {
		this.interval = interval;
		return this;
	}
	
	/**
	 * 添加最后一个Item
	 * 
	 * @param text 要显示的Drawable id
	 * @param backgroundColor 背景颜色
	 */
	public void addLastItem(int id, int backgroundColor) {
		if(mFontColorList != null) {
			mLastDrawableID = id;
			mFontColorList.add(backgroundColor);
		}
	}
	
	/**
	 * 设置当前字体颜色
	 * 
	 * @param color 指定的字体颜色
	 */
	public void setCurrentFontColor(int color) {
		mCurrentFontColor = color;
	}
	
	/**
	 * 获取GridView高度
	 * 
	 * @return 返回GridView高度
	 */
	public int getHeight() {
		Size size = FontScreen.getFontColorItemSize(totalWidth, numColumns, interval);
		int row = mFontColorList.size() / numColumns;
		if(mFontColorList.size() % numColumns != 0) {
			row++;
		}
		Resources res = mContext.getResources();
		if(row > 0) {
			int verticalPadding = res.getDimensionPixelSize(R.dimen.note_add_tools_font_color_gv_verticalPadding);
			
			return (row - 1) * (size.getHeight() + verticalPadding) + size.getHeight();
		}
		return 0;
	}
}
