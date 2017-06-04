package com.lazysong.gojob.module.beans;

public class Willings {
	private String user_id;
	private byte willing_no;
	private int work_place;
	private int category_id;
	private String salary_month;
	private String position_type;
	public static final String name="Willings";
	public Willings() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Willings(String user_id, byte willing_no, int work_place,
			int category, String salary_month,
			String position_type) {
		super();
		this.user_id = user_id;
		this.willing_no = willing_no;
		this.work_place = work_place;
		this.category_id = category;
		this.salary_month = salary_month;
		this.position_type = position_type;
	}

	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public void setWork_place(int work_place) {
		this.work_place = work_place;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public byte getWilling_no() {
		return willing_no;
	}
	public void setWilling_no(byte willing_no) {
		this.willing_no = willing_no;
	}
	
	public int getWork_place() {
		return work_place;
	}

	public String getSalary_month() {
		return salary_month;
	}
	public void setSalary_month(String salary_month) {
		this.salary_month = salary_month;
	}
	public String getPosition_type() {
		return position_type;
	}
	public void setPosition_type(String position_type) {
		this.position_type = position_type;
	}
	
}
