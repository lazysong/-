package com.lazysong.gojob.com.lazysong.gojob.beans;

/**
 * Created by lazysong on 2017/3/1.
 */
public class User {
    private String userid;
    private String nickname;

    public User() {
    }

    public User(String userid, String nickname) {
        this.userid = userid;
        this.nickname = nickname;
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
}