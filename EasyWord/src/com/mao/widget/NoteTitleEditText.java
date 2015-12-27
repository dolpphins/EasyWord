package com.mao.widget;

import com.mao.interf.Modifiable;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 添加或修改笔记Title EditText
 * 
 * @author mao
 *
 */
public class NoteTitleEditText extends EditText implements Modifiable{

	/** 标记是否修改 */
	private boolean mHasRevised = false;
	
	public NoteTitleEditText(Context context) {
		this(context, null);
	}
	
	public NoteTitleEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mHasRevised = true;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public boolean hasModified() {
		return mHasRevised;
	}

	@Override
	public void setModified(boolean modified) {
		mHasRevised = modified;
	}

}
