package com.mao.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftInputHelper {

	public static void hideSoftInput(Context context, View v) {
		if(context == null || v == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	public static void showSoftInput(Context context) {
		if(context == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
}
