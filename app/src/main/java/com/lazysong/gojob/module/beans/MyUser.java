package com.lazysong.gojob.module.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by lazysong on 2017/3/1.
 */
public class MyUser extends User implements Parcelable {
    protected Bitmap img;

    public MyUser() {
    }

    public MyUser(String userid, String nickname, Bitmap img, String imgName, int sex, Date birthday, String sign, String password) {
        super(userid, nickname, imgName, sex, birthday, sign, password);
        this.img = img;
    }

    public MyUser(User user, Bitmap bitmap) {
        user_id = user.getUser_id();
        nickname = user.getNickname();
        img_name = user.getImg_name();
        sex = user.getSex();
        birthday = user.getBirthday();
        sign = user.getSign();
        img = bitmap;
    }

    public User getBaseuser() {
        User user = new User();
        user.setUser_id(user_id);
        user.setNickname(nickname);
        user.setImg_name(img_name);
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setSign(sign);
        return user;
    }
    protected MyUser(Parcel in) {
        user_id = in.readString();
        nickname = in.readString();
        img = in.readParcelable(Bitmap.class.getClassLoader());
        img_name = in.readString();
        sex = in.readInt();
        sign = in.readString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthday = new Date(format.parse(in.readString()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<MyUser> CREATOR = new Creator<MyUser>() {
        @Override
        public MyUser createFromParcel(Parcel in) {
            return new MyUser(in);
        }

        @Override
        public MyUser[] newArray(int size) {
            return new MyUser[size];
        }
    };

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
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
        dest.writeString(user_id);
        dest.writeString(nickname);
        dest.writeParcelable(img, flags);
        dest.writeString(img_name);
        dest.writeInt(sex);
        dest.writeString(sign);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dest.writeString(format.format(birthday));
    }

}