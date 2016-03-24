package com.mao.loader;

import java.util.List;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;

/**
 * Spanned加载器
 * 
 * @author mao
 *
 */
public class SpannedLoader {

	/**
	 * 将spanned的字符串描述转换为Spanned对象
	 * 
	 * @param content 要处理的内容
	 * @param spannedContent spanned字符串
	 * @param imageSpanGetter ImageSpan处理器
	 * @return 成功返回Spanned对象,失败返回null.
	 */
	public Spanned loadSpanned(String content, String spannedContent, ImageSpanHandler imageSpanHandler) {
		if(!TextUtils.isEmpty(spannedContent)) {
			CharacterStyleParser parser = new CharacterStyleParser();
			List<CharacterStyleParser.CharacterStyleEntry> characterStyleList = parser.toCharacterStyle(spannedContent);
			SpannableString ss = new SpannableString(content);
			if(characterStyleList != null) {
				for(CharacterStyleParser.CharacterStyleEntry entry : characterStyleList) {
					CharacterStyle span = entry.getCharacterStyle();
					//ImageSpan特殊处理
					if(CharacterStyleParser.SpanType.ImageSpan.equals(entry.getCharacterStyleType())) {
						String source = ((ImageSpan)entry.getCharacterStyle()).getSource();
						if(imageSpanHandler != null) {
							span = imageSpanHandler.getImageSpan(source);
						}
					}
					ss.setSpan(span, entry.getStart(), entry.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			return ss;
		}
		return null;
	}
	
	
	/**
	 * ImageSpan处理接口
	 * 
	 * @author mao
	 *
	 */
	public interface ImageSpanHandler {
		
		/**
		 * 获取一个ImageSpan对象
		 * 
		 * @param source 
		 * @return
		 */
		ImageSpan getImageSpan(String source);
		
		
	}
}
