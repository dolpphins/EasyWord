package com.mao.widget;

import java.util.Set;

import com.mao.bean.Font;
import com.mao.conf.EmotionConfiguration;
import com.mao.interf.Modifiable;
import com.mao.manager.FontManager;
import com.mao.screen.DisplayUtils;
import com.mao.utils.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class WordEditText extends EditText implements Modifiable{

	private final static String TAG = "WordEditText";
	
	private Font mFont;
	
	private boolean mFlags;
	
	/** 标记是否修改 */
	private boolean mHasRevised = false;
	
	/** 标记是否允许样式化显示,默认为允许 */
	private boolean mAllowStyleable = true;
	
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
	 * 通过url得到ImageSpan对象,注意如果此时通过getWidth获取编辑框宽度不为0时
	 * totalWidth将会被getWidth取代
	 * 
	 * @param srcPath
	 * @param imageSpanUrl 
	 * @param totalWidth 编辑框宽度,用于计算缩放因子
	 * @return
	 */
	public ImageSpan generatePictureImageSpan(String srcPath, String imageSpanUrl, int totalWidth) {
		if(!TextUtils.isEmpty(srcPath)) {
			Bitmap bm = BitmapFactory.decodeFile(srcPath);
			if(bm != null) {
				if(getWidth() > 0) {
					totalWidth = getWidth();
				}
				float factor = computeBitmapScaledFactor(bm.getWidth(), bm.getHeight(), totalWidth);
				Bitmap scaledBitmap = ImageUtils.createScaledBitmap(bm, factor);
				bm.recycle();
				Drawable drawable = new BitmapDrawable(getResources(), scaledBitmap);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				ImageSpan span = new ImageSpan(drawable, imageSpanUrl, ImageSpan.ALIGN_BOTTOM);
				return span;
			}
		}
		return null;
	}
	
	/**
	 * 插入图片
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @param imageSpanUrl
	 */
	public void insertPicture(String srcPath, String displayText, String imageSpanSource) {
		ImageSpan span = generatePictureImageSpan(srcPath, imageSpanSource, getWidth());
		SpannableString ss = new SpannableString(displayText);
		ss.setSpan(span, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		getText().insert(getSelectionStart(), ss);
	}
	
	//计算位图显示缩放因子
	private float computeBitmapScaledFactor(int width, int heigth, int totalWidth) {
		if(width <= 0 || heigth <=0 || totalWidth <= 0) {
			return 0.0f;
		}
		if(3 * width < totalWidth) {
			return (float) ((1.0 * totalWidth) / (3.0 * width)); 
		} else if(3 * width > 2 * totalWidth) {
			return (float) ((2.0 * totalWidth) / (3.0 * width)); 
		} else {
			return 1.0f;
		}
	}
	
	/**
	 * 通过表情字符串获取表情ImageSpan
	 * 
	 * @param emojiString 表情字符串
	 * @return 字符串对应的表情存在返回ImageSpan对象,不存在返回null.
	 */
	public ImageSpan generateEmojiImageSpan(String emojiString) {
		Drawable drawable = EmotionConfiguration.getEmojiDrawableFromString(getContext(), emojiString);
		if(drawable != null) {
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			return new ImageSpan(drawable, emojiString);
		}
		return null;
	}
}
