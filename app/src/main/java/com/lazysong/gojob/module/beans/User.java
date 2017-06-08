package com.lazysong.gojob.module.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by lazysong on 2017/4/2.
 */
public class User implements Serializable{
    @SerializedName("user_id")
    protected String user_id;
    @SerializedName("nickname")
    protected String nickname;
    @SerializedName("img_name")
    protected String img_name;//存储url
    @SerializedName("sex")
    protected int sex;
    @SerializedName("birthday")
    protected Date birthday;
    @SerializedName("sign")
    protected String sign;
    @SerializedName("password")
    protected String password;

    public User() {
    }

    public User(String user_id, String nickname, String img_name, int sex, Date birthday, String sign, String password) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.img_name = img_name;
        this.sex = sex;
        this.birthday = birthday;
        this.sign = sign;
        this.password = password;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
