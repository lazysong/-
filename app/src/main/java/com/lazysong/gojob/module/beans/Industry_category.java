package com.lazysong.gojob.module.beans;

import com.google.gson.annotations.SerializedName;

public class Industry_category {
	@SerializedName("category_id")
	private int category_id;

	@SerializedName("category_name")
	private String category_name;

	public static final String name="Industry_category";
	public Industry_category() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Industry_category(int category_id, String category_name) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
}
