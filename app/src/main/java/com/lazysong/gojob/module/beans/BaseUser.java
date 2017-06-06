package com.lazysong.gojob.module.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by lazysong on 2017/4/2.
 */
public class BaseUser implements Serializable{
    @SerializedName("user_id")
    protected String userid;
    @SerializedName("nickname")
    protected String nickname;
    @SerializedName("img_name")
    protected String imgName;
    @SerializedName("sex")
    protected int sex;
    @SerializedName("birthday")
    protected Date birthday;
    @SerializedName("sign")
    protected String sign;
    @SerializedName("password")
    protected String password;

    public BaseUser() {
    }

    public BaseUser(String userid, String nickname, String imgName, int sex, Date birthday, String sign, String password) {
        this.userid = userid;
        this.nickname = nickname;
        this.imgName = imgName;
        this.sex = sex;
        this.birthday = birthday;
        this.sign = sign;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void initUser() {
        nickname = "点击登录";
        birthday = new Date(1990, 1, 1);
        sign = "未设置签名";
    }
}
