package com.lazysong.gojob.module.beans;

public class Resume {
	private String user_id;
	private byte resume_no;
	private String education_experience;
	private String major;
	private String job_experience;
	public static final String name="Resume";
	public Resume() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Resume(String user_id, byte resume_no, String education_experience,
			String major, String job_experience) {
		super();
		this.user_id = user_id;
		this.resume_no = resume_no;
		this.education_experience = education_experience;
		this.major = major;
		this.job_experience = job_experience;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public byte getResume_no() {
		return resume_no;
	}
	public void setResume_no(byte resume_no) {
		this.resume_no = resume_no;
	}
	public String getEducation_experience() {
		return education_experience;
	}
	public void setEducation_experience(String education_experience) {
		this.education_experience = education_experience;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getJob_experience() {
		return job_experience;
	}
	public void setJob_experience(String job_experience) {
		this.job_experience = job_experience;
	}
	
}
