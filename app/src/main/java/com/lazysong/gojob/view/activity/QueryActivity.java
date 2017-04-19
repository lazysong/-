package com.lazysong.gojob.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.PostInformation;
import com.lazysong.gojob.module.beans.PostInformationList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryActivity extends AppCompatActivity {
    private HttpResponse response;
    private HttpEntity entity;
    private ListView listview;
    private List<PostInformation> postInfoList;
    private PostInformation postInfo;
    private TextView tvPostInfo;
    private PostInformationList list;
    private String limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        listview = (ListView) findViewById(R.id.listviewPost);
        tvPostInfo = (TextView) findViewById(R.id.textviewPstInfo);
        Thread thread = new Thread(networkTask);
        thread.start();
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            String result = getResult();
            Bundle data = new Bundle();
            data.putString("result", result);
//            data.putString("result", "result replace");
            Message message = new Message();
            message.setData(data);
            handler.sendMessage(message);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            String result = data.getString("result");

            Gson gson = new Gson();
//            list = gson.fromJson(result, PostInformationList.class);
//            postInfoList = list.getList();
//            String jsonBean = "[{\"post_id\":890,\"company_name\":\"达富电脑（常熟）有限公司\",\"salary_month\":\"面议\",\"work_place\":\"苏州\",\"post_date\":\"Sep 26, 2016 12:00:00 AM\",\"work_type\":\"全职\",\"experience_requirement\":\"不限\",\"education_requirement\":\"不限\",\"position_count\":1,\"position_type\":\"软件测试\"},{\"post_id\":891,\"company_name\":\"苏州达内信息科技有限公司第一分公司\",\"salary_month\":\"4001-6000元/月\",\"work_place\":\"苏州\",\"post_date\":\"Mar 2, 2017 12:00:00 AM\",\"work_type\":\"全职\",\"experience_requirement\":\"不限\",\"education_requirement\":\"不限\",\"position_count\":5,\"position_type\":\"用户界面（UI）设计\"}]";
            postInfoList = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
            String postInfoAll = "";
            for(int i = 0; i < postInfoList.size(); i++) {
                postInfo = postInfoList.get(i);
                postInfoAll += "公司：" + postInfo.getCompany_name() + "\n";
                postInfoAll += "学历要求：" + postInfo.getEducation_requirement() + "\n";
                postInfoAll += "工作经验：" + postInfo.getExperience_requirement() + "\n";
                postInfoAll += "工作地点：" + postInfo.getPosition_type() + "\n";
                postInfoAll += "月薪：" + postInfo.getSalary_month() + "\n";
                postInfoAll += "工作地点：" + postInfo.getWork_place() + "\n";
                postInfoAll += "工作性质：" + postInfo.getWork_type() + "\n";
                postInfoAll += "发布日期：" + postInfo.getPost_date() + "\n";
                postInfoAll += "--------------------------------------------------------------\n";
            }
            tvPostInfo.setText("招聘信息\n" + postInfoAll + "\n");
//            Toast.makeText(QueryActivity.this, "result:" + result, Toast.LENGTH_SHORT).show();
        }
    };

    private String getResult() {
        limit = getIntent().getStringExtra("limit");
        // 生成一个请求对象
        HttpGet httpGet = new HttpGet("http://www.lazysong.cn:8080/Test/a.scaction?limit=" + limit);
        //HttpGet httpGet = new HttpGet("http://192.168.196.153:8080/Test/a.scaction?limit=" + limit);
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

    /*public void getData() {
        user_id = user_id_text.getText().toString();
        user_name = user_name_text.getText().toString();
        user_password = user_password_text.getText().toString();
        user_password_confirm = user_password_confirm_text.getText().toString();
        String group_string = user_group_text.getText().toString();
        if(!group_string.isEmpty())
            user_group =Integer.parseInt(group_string);
        else
            user_group = 1;
    }*/

}
