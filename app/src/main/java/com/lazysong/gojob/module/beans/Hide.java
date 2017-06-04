package com.lazysong.gojob.module.beans;

public class Hide {
	private String user_id;
	private long post_id;
	public static final String name="Hide";
	public Hide() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Hide(String user_id, long post_id) {
		super();
		this.user_id = user_id;
		this.post_id = post_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public long getPost_id() {
		return post_id;
	}
	public void setPost_id(long post_id) {
		this.post_id = post_id;
	}
	
}
