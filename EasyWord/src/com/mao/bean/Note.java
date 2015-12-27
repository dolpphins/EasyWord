package com.mao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * 笔记实体类
 * 
 * @author mao
 *
 */
public class Note implements Serializable{

	private static final long serialVersionUID = 2712926435434674820L;

	@DatabaseField(generatedId = true, uniqueIndex = true)
	private int id;
	
	@DatabaseField
	private String GUID;
	
	@DatabaseField
	private String username;
	
	@DatabaseField
	private String title;
	
	@DatabaseField
	private String content;

	@DatabaseField(columnName = "updateTime")
	private long updateTime;
	
	@DatabaseField
	private String characterStyleContent;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public String getCharacterStyleContent() {
		return characterStyleContent;
	}

	public void setCharacterStyleContent(String characterStyleContent) {
		this.characterStyleContent = characterStyleContent;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public static String getUpdateTimeColumnName() {
		return "updateTime";
	}
	
	
}
