package com.mao.adapter;

import java.util.List;

import com.mao.easyword.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 工具选择字体大小GridView适配器
 * 
 * @author mao
 *
 */
public class FontSizeGridViewAdapter extends BaseAdapter {

	private final static String TAG = "FontSizeGridViewAdapter";
	
	private Context mContext;
	
	private List<String> mFontSizeList;
	
	//默认字体大小
	private String mCurrentFontSize;
	
	public FontSizeGridViewAdapter(Context context, List<String> fontSizeList) {
		mContext = context;
		mFontSizeList = fontSizeList;
	}
	
	@Override
	public int getCount() {
		if(mFontSizeList != null) {
			return mFontSizeList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mFontSizeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = null;
		if(convertView == null) {
			tv = new TextView(mContext);
			//注意默认是以sp为单位!
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.note_add_tools_font_size_item_textSize));
			//居中
			tv.setGravity(Gravity.CENTER);
		} else {
			tv = ((TextView) convertView); 
		}
		tv.setTextColor(Color.BLACK);//防止错乱
		
		tv.setText(mFontSizeList.get(position));
		if(mFontSizeList.get(position).equals(mCurrentFontSize)) {
			tv.setTextColor(Color.BLUE);
		}
		
		return tv;
	}
	
	/**
	 * 设置当前字体大小
	 * 
	 * @param fontSize 指定的字体大小
	 */
	public void setCurrentFontSize(String fontSize) {
		mCurrentFontSize = fontSize;
	}
	
	/**
	 * 获取当前字体大小(字符串形式)
	 * 
	 * @return 返回当前字体大小
	 */
	public String getCurrentFontSize() {
		return mCurrentFontSize;
	}
}
