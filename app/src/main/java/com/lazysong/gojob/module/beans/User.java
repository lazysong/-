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
public class User extends BaseUser implements Parcelable {
    protected Bitmap img;

    public User() {
    }

    public User(String userid, String nickname, Bitmap img, String imgName, int sex, Date birthday, String sign, String password) {
        super(userid, nickname, imgName, sex, birthday, sign, password);
        this.img = img;
    }

    public User(BaseUser baseUser, Bitmap bitmap) {
        userid = baseUser.getUserid();
        nickname = baseUser.getNickname();
        imgName = baseUser.getImgName();
        sex = baseUser.getSex();
        birthday = baseUser.getBirthday();
        sign = baseUser.getSign();
        img = bitmap;
    }

    public BaseUser getBaseuser() {
        BaseUser baseUser = new BaseUser();
        baseUser.setUserid(userid);
        baseUser.setNickname(nickname);
        baseUser.setImgName(imgName);
        baseUser.setSex(sex);
        baseUser.setBirthday(birthday);
        baseUser.setSign(sign);
        return baseUser;
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
            birthday = (Date) format.parse(in.readString());
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