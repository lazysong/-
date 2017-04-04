package com.lazysong.gojob.com.lazysong.gojob.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lazysong on 2017/4/2.
 */
public class BaseUser implements Serializable{
    protected String userid;
    protected String nickname;
    protected String imgName;
    protected int sex;
    protected Date birthday;
    protected String sign;

    public BaseUser() {
    }

    public BaseUser(String userid, String nickname, String imgName, int sex, Date birthday, String sign) {
        this.userid = userid;
        this.nickname = nickname;
        this.imgName = imgName;
        this.sex = sex;
        this.birthday = birthday;
        this.sign = sign;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public void initUser() {
        nickname = "点击登录";
        birthday = new Date(1990, 1, 1);
        sign = "未设置签名";
    }
}
