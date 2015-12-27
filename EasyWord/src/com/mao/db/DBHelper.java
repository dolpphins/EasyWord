package com.mao.db;

import java.util.UUID;

/**
 * 数据库帮助类
 * 
 * @author mao
 *
 */
public class DBHelper {

	/**
	 * 随机产生GUID
	 * 
	 * @return 返回生成的GUID
	 */
	public static String GUID() {
		return UUID.randomUUID().toString();
	}
}
