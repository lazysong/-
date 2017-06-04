package com.lazysong.gojob.module.beans;

public class Mark_com {
	private String user_id;
	private int company_id;
	public static final String name="Mark_com";
	public Mark_com() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Mark_com(String user_id, int company_id) {
		super();
		this.user_id = user_id;
		this.company_id = company_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	
}
