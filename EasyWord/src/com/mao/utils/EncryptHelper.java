package com.mao.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

/**
 * 加密解密相关
 * 
 * @author mao
 *
 */
public class EncryptHelper {

	/**
	 * MD5加密
	 * 
	 * @param s 要加密的字符串,注意不能为空
	 * @return 加密成功返回加密后的字符串,失败返回null.
	 */
	public static String md5(String s) {
		if(TextUtils.isEmpty(s)) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte[] b = md.digest();
			return com.mao.utils.TextUtils.byte2String(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
