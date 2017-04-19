package com.lazysong.gojob.module;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.controler.UrlConstructor;
import com.lazysong.gojob.module.beans.BaseUser;
import com.lazysong.gojob.module.beans.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lazysong on 2017/3/19.
 * 这个类用来向服务器发送不同的请求，如查询请求，修改请求等
 */
public class ServerInfoManager{
    private Context context;
    private HttpResponse response;
    private HttpEntity entity;
    private int requestCode;
    private BaseRequestData requestData;
    private static final String BASE_URL = "http://192.168.18.188:8080";

    public ServerInfoManager(Context context) {
        this.context = context;
    }

    public ServerInfoManager constrRequest(BaseRequestData requestData) {
        this.requestData = requestData;
        return this;
    }

    /*
    * 在constrRequest(BaseRequestData data)之后被调用
    * **/
    public Map<String, Object> request() {
        Map<String, Object> result = new HashMap<String, Object>();
        requestCode = requestData.getRequestCode();
        // 构造url字符串
        String urlStr = requestData.constructUrlString(BASE_URL);
        User user;
        switch (requestCode) {
            case RequestCode.CAT_USER:
                InputStream input = null;
                BaseUser baseUser = null;
                try {
                    input = downladUrl(urlStr);
                    baseUser = readStreamUser(input);
                    input.close();
                    if(baseUser == null)
                        return null;
                    Bitmap bitmap = downloadImg(BASE_URL + "/img/" + baseUser.getImgName());
                    user = new User(baseUser, bitmap);
                    result.put("user", user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RequestCode.EDIT_USER:
                user = (User) requestData.getData().get("user");
                String fileName = user.getImgName();
                File file = new File(context.getFilesDir(), fileName);
                try {
                    FileOutputStream outputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    user.getImg().compress(Bitmap.CompressFormat.PNG, 100, bao);
                    byte[] imgBytes= bao.toByteArray();
                    outputStream.write(imgBytes);
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Map<String, Object> map = new HashMap<String, Object>();
                Gson gson = new Gson();
                String baseUserStr = gson.toJson(user.getBaseuser(), BaseUser.class);
                map.put("baseUser", baseUserStr);
                postFile(urlStr, map, file);
                return null;
        }
        return result;
    }

    private void postFile(final String url, final Map<String, Object> map, File file) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("headImage", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(this).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq" ,"onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("lfq", response.message() + " , body " + str);

                } else {
                    Log.i("lfq" ,response.message() + " error : body " + response.body().string());
                }
            }
        });

    }
    private Bitmap downloadImg(String urlString) throws IOException {
        InputStream input = null;
        Bitmap bitmap = null;
        try {
            input = downladUrl(urlString);
            bitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null)
                input.close();
        }
        return bitmap;
    }

    private BaseUser readStreamUser(InputStream input) {
        BaseUser user = null;
        String result = "";
        if (input != null) {
            // Converts Stream to String with max length of 500.
            try {
                result = readStream(input, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            user = gson.fromJson(result, BaseUser.class);
        }

        return user;
    }

    private String readStream(InputStream input, int maxLength) throws IOException {
        String result = "";
        InputStreamReader reader = new InputStreamReader(input, "UTF-8");
        char[] buffer = new char[maxLength];
        int numChars = 0;
        int readSize = 0;
        while (numChars < maxLength && readSize != -1) {
            numChars += readSize;
            readSize = reader.read(buffer, numChars, buffer.length - numChars);
        }
        if (numChars != -1) {
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }
        return result;
    }

    private InputStream downladUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(10000/*毫秒*/);
        conn.setConnectTimeout(15000/*毫秒*/);
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    public boolean userExists() {

        return false;
    }

    public User getUserInfo() {

        User user = new User();
        return user;
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
}