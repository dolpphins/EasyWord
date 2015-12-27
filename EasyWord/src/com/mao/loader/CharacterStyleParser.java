package com.mao.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mao.conf.EmotionConfiguration;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;

/**
 * CharacterStyle解析器
 * 
 * @author mao
 *
 */
public class CharacterStyleParser {

	private final static String TAG = "SpannedParser";

	private final static String ABSOLUTESIZESPAN_JSON_KEY = "AbsoluteSizeSpanJSONKey";
	private final static String FOREGROUNDCOLORSPAN_JSON_KEY = "ForegroundColorSpanJSONKey";
	private final static String STYLESPAN_JSON_KEY = "StyleSpanJSONKey";
	private final static String IMAGESPAN_JSON_KEY = "ImageSpanJSONKey";
	
	private final static String SPAN_JSON_KEY_KEY = "index";
	private final static String SPAN_JSON_VALUE_KEY = "value";
	
	private final static String JSON_INDEX_SEPARATOR = "-";
	
	public static enum SpanType {
		AbsoluteSizeSpan, ForegroundColorSpan, StyleSpan, ImageSpan
	}
	
	/**
	 * 将Spanned转换为字符串表示形式,该方法使用JSON数据表示
	 * 
	 * @param s 要转换的Spanned对象
	 * @return 转换成功返回相应的字符串,失败返回null.
	 */
	public String toString(Spanned s) {
		if(s == null) {
			return null;
		}
		return parseCharacterStyle(s);
	}
	
	/**
	 * 将字符串转换为CharacterStyle对象,注意返回的ImageSpan只有url信息,需要自行进一步处理.
	 * 
	 * @param source
	 * @return
	 */
	public List<CharacterStyleEntry> toCharacterStyle(String source) {
		if(TextUtils.isEmpty(source)) {
			return null;
		}
		return recoveryCharacterStyle(source);
	}
	
	//解析CharacterStyle
	private String parseCharacterStyle(Spanned s) {
		
		int length = s.length();
		int[] absoluteSizeSpanInfo = new int[length];
		int[] foregroundColorSpanInfo = new int[length];
		int[] styleSpanInfo = new int[length];
		String[] imageSpanInfo = new String[length];
		
		for(int i = 0; i < length; i++) {
			Object[] objs = s.getSpans(i, i + 1, Object.class);
			if(objs != null && objs.length > 0) {
				for(Object obj : objs) {
					if(obj instanceof AbsoluteSizeSpan) {
						absoluteSizeSpanInfo[i] = ((AbsoluteSizeSpan)obj).getSize();
					} else if(obj instanceof ForegroundColorSpan) {
						foregroundColorSpanInfo[i] = ((ForegroundColorSpan)obj).getForegroundColor();
					} else if(obj instanceof StyleSpan) {
						styleSpanInfo[i] = ((StyleSpan)obj).getStyle();
					} else if(obj instanceof ImageSpan) {
						String source = ((ImageSpan)obj).getSource();
						imageSpanInfo[i] = source;
					}
				}
			}
		}
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(ABSOLUTESIZESPAN_JSON_KEY, buildCharacterStyleJSONArray(absoluteSizeSpanInfo));
			jsonObject.put(FOREGROUNDCOLORSPAN_JSON_KEY, buildCharacterStyleJSONArray(foregroundColorSpanInfo));
			jsonObject.put(STYLESPAN_JSON_KEY, buildCharacterStyleJSONArray(styleSpanInfo));
			jsonObject.put(IMAGESPAN_JSON_KEY, buildCharacterStyleJSONArray(imageSpanInfo));
			
			return jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private JSONArray buildCharacterStyleJSONArray(int[] info) throws JSONException {
		if(info.length <= 0) {
			return null;
		}
		int lastValue = info[0];
		JSONArray jsonArray = new JSONArray();
		StringBuilder sb = new StringBuilder("0");
		sb.append(JSON_INDEX_SEPARATOR);
		for(int i = 1; i < info.length; i++) {
			if(info[i] != lastValue) {
				sb.append(i);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(SPAN_JSON_KEY_KEY, sb.toString());
				jsonObject.put(SPAN_JSON_VALUE_KEY, lastValue);
				jsonArray.put(jsonObject);
				lastValue = info[i];
				sb.delete(0, sb.length());
				sb.append(i);
				sb.append(JSON_INDEX_SEPARATOR);
			}
		}
		sb.append(info.length);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(SPAN_JSON_KEY_KEY, sb.toString());
		jsonObject.put(SPAN_JSON_VALUE_KEY, lastValue);
		jsonArray.put(jsonObject);
		
		return jsonArray;
	}
	
	private JSONArray buildCharacterStyleJSONArray(String[] info) throws JSONException {
		if(info.length <= 0) {
			return null;
		}

		JSONArray jsonArray = new JSONArray();
		int i = 0;
		for(;;) {
			//寻找下一个非空的位置
			while(i < info.length && TextUtils.isEmpty(info[i])) {
				i++;
			}
			if(i >= info.length) {
				break;
			}
			String lastValue = info[i];
			StringBuilder sb = new StringBuilder();
			sb.append(i);
			sb.append(JSON_INDEX_SEPARATOR);
			//寻找该ImageSapn结束的位置
			while(i < info.length && lastValue.equals(info[i])) {
				i++;
			}
			sb.append(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(SPAN_JSON_KEY_KEY, sb.toString());
			jsonObject.put(SPAN_JSON_VALUE_KEY, lastValue);
			jsonArray.put(jsonObject);
		}
		
		return jsonArray;
	}
	
	private List<CharacterStyleEntry> recoveryCharacterStyle(String source) {
		try {
			List<CharacterStyleEntry> spannedList = new ArrayList<CharacterStyleEntry>();
			
			JSONObject jsonObject = new JSONObject(source);
			spannedList.addAll(resolveCharacterStyle(SpanType.AbsoluteSizeSpan, jsonObject.getJSONArray(ABSOLUTESIZESPAN_JSON_KEY)));
			spannedList.addAll(resolveCharacterStyle(SpanType.ForegroundColorSpan, jsonObject.getJSONArray(FOREGROUNDCOLORSPAN_JSON_KEY)));
			spannedList.addAll(resolveCharacterStyle(SpanType.StyleSpan, jsonObject.getJSONArray(STYLESPAN_JSON_KEY)));
			spannedList.addAll(resolveCharacterStyle(SpanType.ImageSpan, jsonObject.getJSONArray(IMAGESPAN_JSON_KEY)));
			
			return spannedList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<CharacterStyleEntry> resolveCharacterStyle(SpanType type, JSONArray jsonArray) throws JSONException {
		if(jsonArray != null) {
			List<CharacterStyleEntry> characterStyleList = new ArrayList<CharacterStyleEntry>();
			int length = jsonArray.length();
			for(int i = 0; i < length; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String index = jsonObject.getString(SPAN_JSON_KEY_KEY);
				String value = jsonObject.getString(SPAN_JSON_VALUE_KEY);
				String[] indexs = index.split(JSON_INDEX_SEPARATOR);
				CharacterStyleEntry entry = new CharacterStyleEntry();
				entry.setStart(Integer.parseInt(indexs[0]));
				entry.setEnd(Integer.parseInt(indexs[1]));
				CharacterStyle characterStyle = generateCharacterStyle(type, value);
				if(characterStyle != null) {
					entry.setCharacterStyle(characterStyle);
					entry.setCharacterStyleType(type);
					characterStyleList.add(entry);
				}
			}
			return characterStyleList;
		}
		return null;
	}
	
	private CharacterStyle generateCharacterStyle(SpanType type, String value) {
		if(type == null) {
			return null;
		}
		CharacterStyle characterStyle = null;
		switch (type) {
		case AbsoluteSizeSpan:
			characterStyle = new AbsoluteSizeSpan(Integer.parseInt(value));
			break;
		case ForegroundColorSpan:
			characterStyle = new ForegroundColorSpan(Integer.parseInt(value));
			break;
		case StyleSpan:
			characterStyle = new StyleSpan(Integer.parseInt(value));
			break;
		case ImageSpan:
			characterStyle = new ImageSpan(null, value); 
			break;
		}
		return characterStyle;
	}
	
	/**
	 * 解析Spanned对象中的所有ImageSpan的source属性
	 * 
	 * @param s 要解析的Spanned对象
	 * @return 解析成功返回source属性集合,失败返回null.
	 */
	public static List<String> parseImageSpanSource(Spanned s) {
		if(s == null) {
			return null;
		}
		ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
		if(imageSpans != null) {
			List<String> sourceList = new ArrayList<String>();
			for(ImageSpan span : imageSpans) {
				sourceList.add(span.getSource());
			}
			return sourceList;
		}
		return null;
	}
	
	/**
	 * CharacterStyleEntry类
	 * 
	 * @author mao
	 *
	 */
	public static class CharacterStyleEntry {
		
		private CharacterStyle characterStyle;
		
		private SpanType characterStyleType;
		
		private int start;
		
		private int end;

		public CharacterStyle getCharacterStyle() {
			return characterStyle;
		}

		public void setCharacterStyle(CharacterStyle characterStyle) {
			this.characterStyle = characterStyle;
		}
		
		public SpanType getCharacterStyleType() {
			return characterStyleType;
		}

		public void setCharacterStyleType(SpanType characterStyleType) {
			this.characterStyleType = characterStyleType;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(30);
			sb.append("[start:");
			sb.append(start);
			sb.append(",end:");
			sb.append(end);
			sb.append(",");
			sb.append(characterStyle.toString());
			sb.append("]");
			return sb.toString();
		}
	}
}
