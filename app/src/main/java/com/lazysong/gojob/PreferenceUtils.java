package com.lazysong.gojob;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;

import com.ashokvarma.bottomnavigation.utils.Utils;
import com.lazysong.gojob.com.lazysong.gojob.beans.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lazysong on 2017/3/14.
 */
public class PreferenceUtils {
    //将登录状态写入loginpref文件中
    public static void setLoginPref(Context context, boolean isLogin, String openId) {
        SharedPreferences sp = context.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (isLogin && !TextUtils.isEmpty(openId)) {
            editor.putBoolean("logined", true);
            editor.putString("userId", openId);
        }
        else {
            editor.putBoolean("logined", false);
            editor.putString("userId", "unknown");
        }
        editor.commit();
    }

    public static JSONObject getLogPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        boolean logined = sp.getBoolean("logined", false);
        String userId =sp.getString("userId", "unknown");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("logined", logined);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void writeUserInfo(Context context, boolean isLogin, User user) {
        SharedPreferences sp = context.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (isLogin && user != null) {
            editor.putBoolean("logined", true);
            editor.putString("userId", user.getUserid());
            editor.putString("nickname", user.getNickname());
            editor.putString("imgName", user.getImgname());
            editor.putString("birthday", user.getBirthday().toString());
            editor.putString("sex", user.getSex() + "");
            editor.putString("sign", user.getSign());
        }
        else {
            editor.putBoolean("logined", false);
            editor.putString("userId", "unknown");
        }
        editor.commit();
    }

    public static JSONObject readUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        boolean logined = sp.getBoolean("logined", false);
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
            jsonObject.put("logined", logined);
            jsonObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
