package com.lazysong.gojob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.lazysong.gojob.com.lazysong.gojob.beans.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lazysong on 2017/3/19.
 */
public class ServerInfoManager {
    private Context context;
    private HttpResponse response;
    private HttpEntity entity;

    public ServerInfoManager(Context context) {
        this.context = context;
    }

    public boolean userExists(String userId) {
        return false;
    }

    /**
     * 根据userId从服务器获取User信息
     * */
    public JSONObject getUserInfo(String userId) {
        User user = new User();
        user.setUserid("123434");
        user.setNickname("我是服务器的Nickname");
        user.setImg(BitmapFactory.decodeFile("picName.png"));
        return null;
    }

    public void saveUserInfo(User user) {
        Toast.makeText(context, "用户信息成功保存到服务器", Toast.LENGTH_SHORT).show();
    }

    private String getResult() {
        int limit = 10;
        // 生成一个请求对象
        String url = "http://www.lazysong.cn:8080/Test/a.scaction";
        url = url + "?requestCode=" + RequestCode.GET_RECOMAND;
        url = url + "&?limit" + limit;
        HttpGet httpGet = new HttpGet(url);

        //建立一个NameValuePair数组，用于存储欲传送的参数
        List<Map<Object, Object>> params=new ArrayList<Map<Object, Object>>();
//        params.add(new BasicNameValuePair("task","check_all"));
        //添加参数
        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (Exception e) {
            Log.v("mylog", "添加参数失败");
        }

        // 生成一个Http客户端对象
        HttpClient httpClient = new DefaultHttpClient();

        // 下面使用Http客户端发送请求，并获取响应内容
        InputStream inputStream = null;

        // 发送请求并获得响应对象
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获得响应的消息实体
        entity = response.getEntity();
//        status = response.sta

        // 获取一个输入流
        try {
            inputStream = entity.getContent();
        } catch (IOException e) {
            Log.v("mylog", "获取输入流失败");
        }

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));

        String result = "";
        String line = "";

        try {
            while (null != (line = bufferedReader.readLine())) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            if(inputStream != null)
                inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
