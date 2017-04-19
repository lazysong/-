package com.lazysong.gojob.module.beans;

import java.util.Date;

/**
 * Created by Administrator on 2017/3/2.
 */
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
    private String position_type;

    public PostInformation() {
    }

    public PostInformation(int post_id, String company_name, String salary_month, String work_place,
                           Date post_date, String work_type, String experience_requirement,
                           String education_requirement, int position_count, String position_type) {
        this.post_id = post_id;
        this.company_name = company_name;
        this.salary_month = salary_month;
        this.work_place = work_place;
        this.post_date = post_date;
        this.work_type = work_type;
        this.experience_requirement = experience_requirement;
        this.education_requirement = education_requirement;
        this.position_count = position_count;
        this.position_type = position_type;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getSalary_month() {
        return salary_month;
    }

    public String getWork_place() {
        return work_place;
    }

    public Date getPost_date() {
        return post_date;
    }

    public String getWork_type() {
        return work_type;
    }

    public String getExperience_requirement() {
        return experience_requirement;
    }

    public String getEducation_requirement() {
        return education_requirement;
    }

    public int getPosition_count() {
        return position_count;
    }

    public String getPosition_type() {
        return position_type;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setSalary_month(String salary_month) {
        this.salary_month = salary_month;
    }

    public void setWork_place(String work_place) {
        this.work_place = work_place;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public void setExperience_requirement(String experience_requirement) {
        this.experience_requirement = experience_requirement;
    }

    public void setEducation_requirement(String education_requirement) {
        this.education_requirement = education_requirement;
    }

    public void setPosition_count(int position_count) {
        this.position_count = position_count;
    }

    public void setPosition_type(String position_type) {
        this.position_type = position_type;
    }
}
