package com.lazysong.gojob.com.lazysong.gojob.beans;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by lazysong on 2017/3/1.
 */
public class User {
    private String userid;
    private String nickname;
    private String imgname;
    private int sex;
    private Date birthday;

    public User() {
    }

    public User(String userid, String nickname, String imgname, int sex, Date birthday) {
        this.userid = userid;
        this.nickname = nickname;
        this.imgname = imgname;
        this.sex = sex;
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}