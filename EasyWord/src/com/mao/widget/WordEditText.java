package com.mao.widget;

import java.util.Set;

import com.mao.bean.Font;
import com.mao.interf.Modifiable;
import com.mao.manager.FontManager;
import com.mao.screen.DisplayUtils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class WordEditText extends EditText implements Modifiable{

	private final static String TAG = "WordEditText";
	
	private Font mFont;
	
	private boolean mFlags;
	
	/** 标记是否修改 */
	private boolean mHasRevised = false;
	
	/** 标记是否允许样式化显示,默认为允许 */
	private boolean mAllowStyleable = true;
	
	//图片显示时的缩放因子
	private float mFactor = (float) (1.0f / 3.0f);
	
	public WordEditText(Context context) {
		this(context, null);
	}
	
	public WordEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		mFlags = false;
		//编辑框文本改变监听
		addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//Log.i(TAG, "onTextChanged");
				//Log.i(TAG, "s:" + s + ",start:" + start + ",before:" + before + ",count:" + count);
				//不用样式化显示
				if(!mAllowStyleable) {
					return;
				}
				//s为替换后文本,从start开始的count个字符是替换之前before个字符的
				mFlags = !mFlags;//一定要放在前面
				if(mFlags && mFont != null) {
					SpannableString ss = new SpannableString(s);
					ss.setSpan(generateForegroundColorSpan(), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(generateAbsoluteSizeSpan(), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					setTextStyle(ss, start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					
					setText(ss);
					setSelection(start + count);
				} 
				mHasRevised = true;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//Log.i(TAG, "beforeTextChanged");
				//Log.i(TAG, "s:" + s + ",start:" + start + ",after:" + after + ",count:" + count);
				//s为替换前文本,从start开始的count个字符替换为after个新的字符
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//Log.i(TAG, "afterTextChanged");
				//Log.i(TAG, s.toString());
				//替换后的文本
			}
		});
	}
	
	/**
	 * 设置字体
	 * 
	 * @param font
	 */
	public void setFont(Font font) {
		mFont = font;
	}
	
	private ForegroundColorSpan generateForegroundColorSpan() {
		return new ForegroundColorSpan(mFont.getColor());
	}
	
	private AbsoluteSizeSpan generateAbsoluteSizeSpan() {
		int textSize = DisplayUtils.sp2px(getContext(), mFont.getSize());
		return new AbsoluteSizeSpan(textSize);
	}
	
	private void setTextStyle(SpannableString ss, int start, int end, int flags) {
		Set<FontManager.TextStyle> fontStyleSet = mFont.getTextStyleSet();
		//粗体
		if(fontStyleSet.contains(FontManager.TextStyle.BOLD)) {
			ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flags);
		}
		//斜体
		if(fontStyleSet.contains(FontManager.TextStyle.ITALIC)) {
			ss.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flags);
		}
		//下划线
		if(fontStyleSet.contains(FontManager.TextStyle.UNDERLINE)) {
			ss.setSpan(new UnderlineSpan(), start, end, flags);
		}
		//删除线
		if(fontStyleSet.contains(FontManager.TextStyle.DELETELINE)) {
			ss.setSpan(new StrikethroughSpan(), start, end, flags);
		}
	}

	@Override
	public boolean hasModified() {
		return mHasRevised;
	}
	
	@Override
	public void setModified(boolean modified) {
		mHasRevised = modified;
	}
	
	/**
	 * 设置是否允许样式化显示,如果设置为false那么该EditText的行为与普通的
	 * EditText一样.
	 * 
	 * @param allow true表示允许,false表示不允许
	 */
	public void requestAllowStyleable(boolean allow) {
		mAllowStyleable = allow;
	}
	
	/**
	 * 获取图片显示时的缩放因子
	 * 
	 * @return 返回图片显示时的缩放因子
	 */
	public float getBitmapScaledFactor() {
		return mFactor;
	}
}
