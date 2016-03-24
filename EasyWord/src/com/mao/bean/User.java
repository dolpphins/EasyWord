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
	
	private String hpUrl;
	
	private String signature;
	
	private String sex;

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

	public String getHpUrl() {
		return hpUrl;
	}

	public void setHpUrl(String hpUrl) {
		this.hpUrl = hpUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
}
