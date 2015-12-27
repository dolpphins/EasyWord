package com.mao.bean;

import cn.bmob.v3.BmobObject;

/**
 * 用户类
 * 
 * @author mao
 *
 */
public class User extends BmobObject{

	private int id;
	
	private String username;
	
	private String phone;
	
	private String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
