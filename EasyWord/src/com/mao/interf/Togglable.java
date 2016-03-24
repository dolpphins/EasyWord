package com.mao.interf;

/**
 * 可隐藏显示View
 * 
 * @author mao
 *
 */
public interface Togglable {

	void toggle();
	
	void show();
	
	void hide();
	
	boolean isShowing();
}
