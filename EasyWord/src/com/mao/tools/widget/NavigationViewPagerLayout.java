package com.mao.tools.widget;

import com.mao.easyword.R;
import com.mao.interf.Togglable;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationViewPagerLayout extends LinearLayout implements Togglable{

	private Context mContext;
	
	private View mContentView;
	private ToggleWrapView mNoteToolsWrapView;
	
	//private TextView note_add_bottom_tools_navigation_arrowdown;
	private HorizontalScrollView note_add_bottom_tools_navigation_scrollview;
	private LinearLayout note_add_bottom_tools_navigation;
	private TextView note_add_bottom_tools_navigation_tip;
	private ViewPager note_add_bottom_tools_vp;
	private NoteToolsViewPagerAdapter viewPagerAdapter;
	private NoteToolsOnPageChangeListener viewPagerPageChanageListener;
	
	private int mNavigationTextWidth;
	
	private String[] mTiltes;
	private TextView[] mNavigationViews;
	private View[] mViewPagerViews;
	private int mNorNavigationTextColor;//未选中颜色
	private int mSelNavigationTextColor;//选中颜色
	
	private OnNoteToolsLayoutListener mOnNoteToolsLayoutListener;
	
	public NavigationViewPagerLayout(Context context) {
		this(context, null);
	}
	
	public NavigationViewPagerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
		setEvent();
	}

	private void init() {
		removeAllViews();
		setClickable(true);
		mContentView = LayoutInflater.from(mContext).inflate(R.layout.note_add_bottom_tools_layout, this, false);
		addView(mContentView);
		mNoteToolsWrapView = new ToggleWrapView(this, true);
//		Animation showAnim = AnimationUtils.loadAnimation(mContext, R.anim.note_add_bottom_tools_enter);
//		showAnim.setFillAfter(true);
//		mNoteToolsWrapView.setShowAnimation(showAnim);
//		Animation exitAnim = AnimationUtils.loadAnimation(mContext, R.anim.note_add_bottom_tools_exit);
//		mNoteToolsWrapView.setHideAnimation(exitAnim);
		
		initUI();
	}
	
	private void setEvent() {
//		note_add_bottom_tools_navigation_arrowdown.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mNoteToolsWrapView.hide();
//			}
//		});
		//导航点击事件
		if(mNavigationViews != null && mNavigationViews.length > 0) {
			for(int i = 0; i < mNavigationViews.length; i++) {
				final int t = i;
				mNavigationViews[i].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setCurrentItem(t);
					}
				});
			}
		}
	}
	
	public void setOnNoteToolsLayoutListener(OnNoteToolsLayoutListener listener) {
		mOnNoteToolsLayoutListener = listener;
	}
	
	public OnNoteToolsLayoutListener getOnNoteToolsLayoutListener() {
		return mOnNoteToolsLayoutListener; 
	}
	
	@Override
	public void toggle() {
		mNoteToolsWrapView.toggle();
	}
	
	@Override
	public void show() {
		mNoteToolsWrapView.show();
	}
	
	@Override
	public void hide() {
		mNoteToolsWrapView.hide();
	}
	
	@Override
	public boolean isShowing() {
		return mNoteToolsWrapView.isShowing();
	}
	
	public void setData(String[] titles, View[] views) {
		if(titles != null && views != null) {
			if(titles.length != views.length) {
				throw new IllegalArgumentException("titles's length must be the same as views's length");
			} else {
				mTiltes = titles;
				mViewPagerViews = views;
				updateUI();
			}
		}
	}
	
	private void updateUI() {
		initUI();
		setEvent();
		invalidate();
	}
	
	private void initUI() {
//		if(note_add_bottom_tools_navigation_arrowdown == null) {
//			note_add_bottom_tools_navigation_arrowdown = (TextView) mContentView.findViewById(R.id.note_add_bottom_tools_navigation_arrowdown);
//		}
		if(note_add_bottom_tools_navigation_scrollview == null) {
			note_add_bottom_tools_navigation_scrollview = (HorizontalScrollView) mContentView.findViewById(R.id.note_add_bottom_tools_navigation_scrollview);
		}
		if(note_add_bottom_tools_navigation == null) {
			note_add_bottom_tools_navigation = (LinearLayout) mContentView.findViewById(R.id.note_add_bottom_tools_navigation);
		}
		if(note_add_bottom_tools_navigation_tip == null) {
			note_add_bottom_tools_navigation_tip = (TextView) mContentView.findViewById(R.id.note_add_bottom_tools_navigation_tip);
		}
		if(note_add_bottom_tools_vp == null) {
			note_add_bottom_tools_vp = (ViewPager) mContentView.findViewById(R.id.note_add_bottom_tools_vp);
		}
		if(viewPagerAdapter == null) {
			viewPagerAdapter = new NoteToolsViewPagerAdapter();	
		}
		note_add_bottom_tools_vp.setAdapter(viewPagerAdapter);
		if(viewPagerPageChanageListener == null) {
			viewPagerPageChanageListener = new NoteToolsOnPageChangeListener();
		}
		note_add_bottom_tools_vp.setOnPageChangeListener(viewPagerPageChanageListener);
		
		note_add_bottom_tools_navigation.removeAllViews();
		if(mTiltes != null) {
			mNavigationViews = new TextView[mTiltes.length];
			for(int i = 0; i < mTiltes.length; i++) {
				mNavigationViews[i] = buildNavigationTextView(note_add_bottom_tools_navigation, mTiltes[i]);
				note_add_bottom_tools_navigation.addView(mNavigationViews[i]); 
			}
		}
		if(mNavigationViews != null && mNavigationViews.length > 0) {
			mNavigationViews[0].getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					mNavigationTextWidth = mNavigationViews[0].getWidth();
					if(mNavigationTextWidth > 0) {
						mNavigationViews[0].getViewTreeObserver().removeOnPreDrawListener(this);
					}
					note_add_bottom_tools_navigation_tip.setWidth(mNavigationTextWidth);
					return true;
				}
			});
		}
		//导航条文本正常颜色
		mNorNavigationTextColor = mContext.getResources().getColor(R.color.note_add_bottom_tools_navigation_norTextColor);
		//导航条文本选中颜色
		mSelNavigationTextColor = mContext.getResources().getColor(R.color.note_add_bottom_tools_navigation_selTextColor);
	}
	
	private TextView buildNavigationTextView(ViewGroup container, String text) {
		TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.note_add_bottom_tools_navigation_textview, container, false);
		tv.setText(text);
		return tv;
	}
	
	/**
	 * 设置当前选择的Item
	 * 
	 * @param item
	 */
	public void setCurrentItem(int item) {
		//setNavigationTipPosition(item * mNavigationTextWidth);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) note_add_bottom_tools_navigation_tip.getLayoutParams();
		params.setMargins(item * mNavigationTextWidth, 0, 0, 0);
		if(mNavigationViews != null) {
			for(int i = 0; i < mNavigationViews.length; i++) {
				if(i == item) {
					mNavigationViews[i].setTextColor(mSelNavigationTextColor);
				} else {
					mNavigationViews[i].setTextColor(mNorNavigationTextColor);
				}
			}
		}
		note_add_bottom_tools_vp.setCurrentItem(item);
		note_add_bottom_tools_navigation_scrollview.smoothScrollBy(note_add_bottom_tools_navigation_scrollview.getScrollX() + item * mNavigationTextWidth, 0);
	}
	
//	private void setNavigationTipPosition(int offset) {
//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) note_add_bottom_tools_navigation_tip.getLayoutParams();
//		params.setMargins(offset, 0, 0, 0);
//	}
	
	private class NoteToolsViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if(mViewPagerViews != null) {
				return mViewPagerViews.length;
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViewPagerViews[position]);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mViewPagerViews[position]);
			return mViewPagerViews[position];
			
		}
	}
	
	//ViewPager滑动监听器
	private class NoteToolsOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//			System.out.println("position:" + position);
//			System.out.println("positionOffset:" + positionOffset);
//			System.out.println("positionOffsetPixels:" + positionOffsetPixels);
//			setNavigationTipPosition(position * mNavigationTextWidth + positionOffsetPixels);
		}

		@Override
		public void onPageSelected(int position) {
			setCurrentItem(position);
		}
	}
	
	/**
	 * 监听器
	 * 
	 * @author mao
	 *
	 */
	public interface OnNoteToolsLayoutListener {
		
		
	}
}
