package com.lazysong.gojob.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Context context;

    public LocalInfoManager(Context context) {
        sp = context.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        editor = sp.edit();
        this.context = context;
    }
    /**
     * 向本地SharedPreference文件中写入登录状态
     **/
    public void setLogin(boolean isLogin) {
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
    public JSONObject getLogPref() {
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
    public void setUser(User user) {
        if (user != null) {
            editor.putString("userId", user.getUserid());
            editor.putString("nickname", user.getNickname());
            editor.putString("imgName", user.getImgName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            editor.putString("birthday", format.format(user.getBirthday()));
//            editor.putString("birthday", "1990-01-01");
//            editor.putString("birthday", user.getBirthday().toString() + " ");
            editor.putString("sex", user.getSex() + "");
            editor.putString("sign", user.getSign());
            if(user.getImgName() != null) {
                try {
                    Log.v("user", user.getImgName());
                    FileOutputStream outputStream = context.openFileOutput(user.getImgName(), context.MODE_PRIVATE);
                    byte[] buffer = new byte[1024 * 100];
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bilibili);
                    if (user.getImg() != null)
                        user.getImg().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    else
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.write(buffer);
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            editor.putString("userId", "unknown");
        }
        editor.commit();
    }

    /**
     * 从本地SharedPreference文件中构造User实体
     * */
    public JSONObject getUserInfo() {
        User user = new User();
        user.setUserid(sp.getString("userId", "unknown"));
        user.setNickname(sp.getString("nickname", "unknown"));
        user.setImgName(sp.getString("imgName", "userPic"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            user.setBirthday(format.parse(sp.getString("birthday", "1990-01-01")));
        } catch (ParseException e) {
            e.printStackTrace();
            user.setBirthday(new Date(1990, 1, 1));
        }
//        user.setBirthday(new Date(1990, 1, 1));
        try {
            FileInputStream inputStream = context.openFileInput(user.getImgName());
            byte[] buffer = new byte[1024*100];
            inputStream.read(buffer);
            user.setImg(BitmapFactory.decodeByteArray(buffer, 0, buffer.length));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
