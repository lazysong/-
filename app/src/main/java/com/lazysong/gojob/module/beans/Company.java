package com.lazysong.gojob.module.beans;

/**
 * Created by lazysong on 2017/6/1.
 */
public class Company {
    private String companyName;

    public Company(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
