package com.lazysong.gojob.module.beans;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class PostInformation {
	private int post_id;
	private String company_name;
	private String salary_month;
	private String work_place;
	private Date post_date;
	private String work_type;
	private String experience_requirement;
	private String education_requirement;
	private int position_count;

	@SerializedName("position_type")
	private String position_type;
	private String category_name;
//	public static final String name="PostInformation";
	
	public PostInformation(int post_id, String company_name,
						   String salary_month, String work_place, Date post_date,
						   String work_type, String experience_requirement,
						   String education_requirement, int position_count,
						   String postition_type, String category_name) {
		super();
		this.post_id = post_id;
		this.company_name = company_name;
		this.salary_month = salary_month;
		this.work_place = work_place;
		this.post_date = post_date;
		this.work_type = work_type;
		this.experience_requirement = experience_requirement;
		this.education_requirement = education_requirement;
		this.position_count = position_count;
		this.position_type = postition_type;
		this.category_name = category_name;
	}
	public PostInformation() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getSalary_month() {
		return salary_month;
	}
	public void setSalary_month(String salary_month) {
		this.salary_month = salary_month;
	}
	public String getWork_place() {
		return work_place;
	}
	public void setWork_place(String work_place) {
		this.work_place = work_place;
	}
	public Date getPost_date() {
		return post_date;
	}
	public void setPost_date(Date post_date) {
		this.post_date = post_date;
	}
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	public String getExperience_requirement() {
		return experience_requirement;
	}
	public void setExperience_requirement(String experience_requirement) {
		this.experience_requirement = experience_requirement;
	}
	public String getEducation_requirement() {
		return education_requirement;
	}
	public void setEducation_requirement(String education_requirement) {
		this.education_requirement = education_requirement;
	}
	public int getPosition_count() {
		return position_count;
	}
	public void setPosition_count(int position_count) {
		this.position_count = position_count;
	}
	public String getPosition_type() {
		return position_type;
	}
	public void setPosition_type(String position_type) {
		this.position_type = position_type;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
}
