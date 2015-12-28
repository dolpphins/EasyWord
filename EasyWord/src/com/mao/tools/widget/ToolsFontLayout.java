/**
 * 
 */
package com.mao.tools.widget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mao.adapter.FontColorGridViewAdapter;
import com.mao.adapter.FontSizeGridViewAdapter;
import com.mao.dialog.ColorPicker;
import com.mao.dialog.ColorPicker.OnColorSelectedListener;
import com.mao.easyword.R;
import com.mao.manager.FontManager;
import com.mao.screen.DisplayUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 添加笔记工具字体布局
 * 
 * @author mao
 *
 */
public class ToolsFontLayout extends LinearLayout {

	private final static String TAG = "ToolsFontLayout";
	
	private View mView;
	
	/**
	 * 字体大小相关
	 */
	private GridView note_add_tools_font_size_gv;
	private FontSizeGridViewAdapter fontSizeAdapter;
	private List<String> fontSizeList;
	private float mCurrentFontSize;
	private HorizontalScrollView note_add_tools_font_size_scrollview;
	
	/**
	 * 字体颜色相关
	 */
	private LinearLayout note_add_tools_font_color_gv_parent;
	private GridView note_add_tools_font_color_gv;
	private FontColorGridViewAdapter fontColorAdapter;
	private List<Integer> fontColorList;
	private int mCurrentFontColor;
	
	private ColorPicker mColorPicker;
	
	/**
	 * 字体样式相关
	 */
	private TextView note_add_tools_font_style_bold;
	private TextView note_add_tools_font_style_italic;
	private TextView note_add_tools_font_style_underline;
	private TextView note_add_tools_font_style_deleteline;
	private Set<FontManager.TextStyle> mTextStyleSet = new HashSet<FontManager.TextStyle>();
	
	//配置监听器
	private OnFontConfigurationListener mOnFontConfigurationListener;
	
	public ToolsFontLayout(Context context) {
		this(context, null);
	}
	
	public ToolsFontLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		mView = LayoutInflater.from(getContext()).inflate(R.layout.note_add_tools_font_layout, this, true);
		initPagerLayout();
		setEventForView();
	}
	
	private void initPagerLayout() {
		//字体相关
		note_add_tools_font_size_gv = (GridView) mView.findViewById(R.id.note_add_tools_font_size_gv);
		note_add_tools_font_size_scrollview = (HorizontalScrollView) mView.findViewById(R.id.note_add_tools_font_size_scrollview);
		fontSizeList = FontManager.getFontSizeList();
		note_add_tools_font_size_gv.setNumColumns(fontSizeList.size());
		ViewGroup.LayoutParams params = note_add_tools_font_size_gv.getLayoutParams();
		float tvWidth = FontManager.getMaxTextWidth(getResources().getDimension(R.dimen.note_add_tools_font_size_item_textSize));
		float horizontalPadding = getResources().getDimension(R.dimen.note_add_tools_font_size_gv_horizontalPadding);
		int size = fontSizeList.size();
		if(size > 0) {
			params.width =  (int) ((size - 1) * (tvWidth + horizontalPadding) + tvWidth);
		}
		fontSizeAdapter = new FontSizeGridViewAdapter(getContext(), fontSizeList);
		fontSizeAdapter.setCurrentFontSize(FontManager.getDefaultFontSize());
		mCurrentFontSize = Float.parseFloat(FontManager.getDefaultFontSize());
		note_add_tools_font_size_gv.setAdapter(fontSizeAdapter);
		
		//颜色相关
		note_add_tools_font_color_gv = (GridView) mView.findViewById(R.id.note_add_tools_font_color_gv);
		fontColorList = FontManager.getFontColorList();
		fontColorAdapter = new FontColorGridViewAdapter(getContext(), fontColorList);
		fontColorAdapter.addLastItem(R.drawable.app_more_icon, Color.TRANSPARENT);
		note_add_tools_font_color_gv.setNumColumns(4);
		fontColorAdapter.setTotalWidth(5000)
						.setNumColumns(4)
						.setNumInterval(getResources().getDimensionPixelSize(R.dimen.note_add_tools_font_color_gv_horizontalPadding));
		mCurrentFontColor = FontManager.getDefaultFontColor();
		fontColorAdapter.setCurrentFontColor(mCurrentFontColor);
		note_add_tools_font_color_gv.setAdapter(fontColorAdapter);
		//监听获取GridView实际宽度
		note_add_tools_font_color_gv.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				int totlaWidth = note_add_tools_font_color_gv.getWidth();
				fontColorAdapter.setTotalWidth(totlaWidth);
				fontColorAdapter.notifyDataSetChanged();
				note_add_tools_font_color_gv.getViewTreeObserver().removeOnPreDrawListener(this);
				//滚动到适当的位置
				int index = FontManager.getFontSizeIndex(mCurrentFontSize);
				if(index > 0) {
					float tvWidth = FontManager.getMaxTextWidth(getResources().getDimension(R.dimen.note_add_tools_font_size_item_textSize));
					float horizontalPadding = getResources().getDimension(R.dimen.note_add_tools_font_size_gv_horizontalPadding);
					float scrollOffset = (tvWidth + horizontalPadding) * index;
					System.out.println("scrollOffset:" + scrollOffset);
					note_add_tools_font_size_scrollview.scrollBy((int) scrollOffset, 0);
				}
				
				note_add_tools_font_color_gv.getViewTreeObserver().removeOnPreDrawListener(this);
				
				return true;
			}
		});
		
		//字体样式相关
		note_add_tools_font_style_bold = (TextView) mView.findViewById(R.id.note_add_tools_font_style_bold);
		note_add_tools_font_style_italic = (TextView) mView.findViewById(R.id.note_add_tools_font_style_italic);
		note_add_tools_font_style_underline = (TextView) mView.findViewById(R.id.note_add_tools_font_style_underline);
		note_add_tools_font_style_deleteline = (TextView) mView.findViewById(R.id.note_add_tools_font_style_deleteline);
	}
	
	private void setEventForView() {
		//字体大小GridView Item点击事件
		note_add_tools_font_size_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				float size = Float.parseFloat(fontSizeList.get(position));
				if(mCurrentFontSize != size) {
					if(mOnFontConfigurationListener != null) {
						mOnFontConfigurationListener.OnFontSizeChange(mCurrentFontSize, size);
					}
					mCurrentFontSize = size;
					fontSizeAdapter.setCurrentFontSize(fontSizeList.get(position));
					fontSizeAdapter.notifyDataSetChanged();
				}
				
			}
		});
		//字体颜色GridView Item点击事件
		note_add_tools_font_color_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == fontColorList.size()) {
					//显示颜色选择器对话框
					showColorPickerDialog();
				} else {
					int color = fontColorList.get(position);
					updateColor(color);
				}
			}
		});
		
		//字体样式相关点击事件
		//粗体
		note_add_tools_font_style_bold.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleFontStyleClick(FontManager.TextStyle.BOLD, note_add_tools_font_style_bold);
			}
		});
		//斜体
		note_add_tools_font_style_italic.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				handleFontStyleClick(FontManager.TextStyle.ITALIC, note_add_tools_font_style_italic);
			}
		});
		//下划线
		note_add_tools_font_style_underline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleFontStyleClick(FontManager.TextStyle.UNDERLINE, note_add_tools_font_style_underline);
			}
		});
		//删除线
		note_add_tools_font_style_deleteline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleFontStyleClick(FontManager.TextStyle.DELETELINE, note_add_tools_font_style_deleteline);
			}
		});
	}
	
	private void updateColor(int color) {
		mCurrentFontColor = color;
		fontColorAdapter.setCurrentFontColor(mCurrentFontColor);
		fontColorAdapter.notifyDataSetChanged();
		if(mOnFontConfigurationListener != null) {
			mOnFontConfigurationListener.OnFontColorChange(mCurrentFontColor, color);
		}
	}
	
	private void handleFontStyleClick(FontManager.TextStyle style, TextView tv) {
		if(isInTextStyle(mTextStyleSet, style)) {
			mTextStyleSet.remove(style);
			setFontTextStyle(tv, false);
		} else {
			mTextStyleSet.add(style);
			setFontTextStyle(tv, true);
		}
		if(mOnFontConfigurationListener != null) {
			mOnFontConfigurationListener.OnFontStyleChange(mTextStyleSet);
		}
	}
	
	//判断字体样式
	private boolean isInTextStyle(Set<FontManager.TextStyle> textStyleSet, FontManager.TextStyle style) {
		if(textStyleSet.contains(style)) {
			return true;
		}
		return false;
	}
	
	private void setFontTextStyle(TextView tv, boolean isSelected) {
		if(isSelected) {
			tv.setBackgroundColor(getResources().getColor(R.color.light_blue));
			tv.setTextColor(Color.WHITE);
		} else {
			tv.setBackgroundColor(Color.WHITE);
			tv.setTextColor(getResources().getColor(R.color.note_add_bottom_tools_font_style_textColor));
		}
	}
	
	
	private void showColorPickerDialog() {
		//懒加载
		if(mColorPicker == null) {
			//如果getContext获取到的不是一个Activity对象会抛异常
			try {
				mColorPicker = new ColorPicker(getContext());
				WindowManager.LayoutParams params = mColorPicker.getWindow().getAttributes();
				int screenWidth = DisplayUtils.getScreenWidthPixels((Activity)getContext());
				params.width = screenWidth * 4 / 5;
				params.height = WindowManager.LayoutParams.WRAP_CONTENT; 
				mColorPicker.setOnColorSelectedListener(new OnColorSelectedListener() {
					
					@Override
					public void OnColorSelected(int color) {
						updateColor(color);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mColorPicker.setColor(mCurrentFontColor);//要先设置当前颜色值
		mColorPicker.show();
	}
	/**
	 * 设置配置改变监听器
	 * 
	 * @param listener 要设置的配置改变监听器
	 */
	public void setOnFontConfigurationListener(OnFontConfigurationListener listener) {
		mOnFontConfigurationListener = listener;
	}
	
	/**
	 * 获取配置改变监听器
	 * 
	 * @return 返回配置改变监听器
	 */
	public OnFontConfigurationListener getOnFontConfigurationListener() {
		return mOnFontConfigurationListener;
	}
	
	/**
	 * 字体配置改变接口
	 * 
	 * @author mao
	 *
	 */
	public interface OnFontConfigurationListener {
		
		void OnFontSizeChange(float oldSize, float newSize);
		
		void OnFontColorChange(int oldColor, int newColor);
		
		void OnFontStyleChange(Set<FontManager.TextStyle> mTextStyleSet);
	}
}
