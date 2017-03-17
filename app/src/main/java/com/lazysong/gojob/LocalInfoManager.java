package com.lazysong.gojob;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lazysong.gojob.com.lazysong.gojob.beans.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lazysong on 2017/3/17.
 *
 * 这个类主要用来从本地的sharedPreference文件中构造不同的Bean实体，如User对象
 */
public class LocalInfoManager {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public LocalInfoManager(Context context) {
        sp = context.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        editor = sp.edit();
    }
    /**
     * 向本地SharedPreference文件中写入登录状态
     **/
    public void setLogin(Context context, boolean isLogin) {
        if (isLogin) {
            editor.putBoolean("logined", true);
        }
        else {
            editor.putBoolean("logined", false);
        }
        editor.commit();
    }

    /**
     * 从本地SharedPreference文件中获取登录状态
     * */
    public JSONObject getLogPref(Context context) {
        boolean logined = sp.getBoolean("logined", false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("logined", logined);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 向本地的SharedPreference文件中写入登录User实体
     * */
    public void setUser(Context context, User user) {
        if (user != null) {
            editor.putString("userId", user.getUserid());
            editor.putString("nickname", user.getNickname());
            editor.putString("imgName", user.getImgname());
            editor.putString("birthday", user.getBirthday().toString());
            editor.putString("sex", user.getSex() + "");
            editor.putString("sign", user.getSign());
        }
        else {
            editor.putString("userId", "unknown");
        }
        editor.commit();
    }

    /**
     * 从本地SharedPreference文件中构造User实体
     * */
    public JSONObject getUserInfo(Context context) {
        User user = new User();
        user.setUserid(sp.getString("userId", "unknown"));
        user.setNickname(sp.getString("nickname", "unknown"));
        user.setImgname(sp.getString("imgName", "userPic"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            user.setBirthday(format.parse(sp.getString("birthday", "unknown")));
        } catch (ParseException e) {
            e.printStackTrace();
            user.setBirthday(new Date(1990, 1, 1));
        }
        user.setSex(Integer.parseInt(sp.getString("sex", "1")));
        user.setSign(sp.getString("sign", "unknown"));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
