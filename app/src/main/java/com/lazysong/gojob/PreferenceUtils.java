package com.lazysong.gojob;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
}
