package com.mao.interf;

/**
 * 内容可修改接口
 * 
 * @author mao
 *
 */
public interface Modifiable {

	/**
	 * 是否已修改
	 * 
	 * @return 修改返回true,否则返回false.
	 */
	boolean hasModified();
	
	/**
	 * 设置是否已修改
	 * 
	 * @param modified true表示已修改,false表示未修改
	 */
	void setModified(boolean modified);
}
