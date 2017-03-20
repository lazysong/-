package com.lazysong.gojob.com.lazysong.gojob.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lazysong on 2017/3/1.
 */
public class User implements Parcelable {
    private String userid;
    private String nickname;
    private Bitmap img;
    private String imgName;
    private int sex;
    private Date birthday;
    private String sign;

    public User() {
    }

    public User(String userid, String nickname, Bitmap img, String imgName, int sex, Date birthday, String sign) {
        this.userid = userid;
        this.nickname = nickname;
        this.img = img;
        this.imgName = imgName;
        this.sex = sex;
        this.birthday = birthday;
        this.sign = sign;
    }

    protected User(Parcel in) {
        userid = in.readString();
        nickname = in.readString();
        img = in.readParcelable(Bitmap.class.getClassLoader());
        imgName = in.readString();
        sex = in.readInt();
        sign = in.readString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthday = format.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
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
        img = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(nickname);
        dest.writeParcelable(img, flags);
        dest.writeString(imgName);
        dest.writeInt(sex);
        dest.writeString(sign);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dest.writeString(format.format(birthday));
    }
}