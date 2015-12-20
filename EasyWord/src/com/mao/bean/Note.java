package com.mao.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * 笔记实体类
 * 
 * @author mao
 *
 */
public class Note {

	@DatabaseField
	private int id;
	
	@DatabaseField
	private String username;
}
