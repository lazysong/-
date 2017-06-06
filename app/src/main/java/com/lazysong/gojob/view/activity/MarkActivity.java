package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.adapter.ResultCompanyAdapter;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.beans.PostInformation;
import com.lazysong.gojob.utils.MarkInfoTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkActivity extends AppCompatActivity implements MarkInfoTask.OnDataGotListener {

    private Toolbar toolbar;
    private TextView titleActionbar;
    private RecyclerView recyclePostInfoMark;

    private MarkInfoTask markTask;
    private String userId;
    private ArrayList<PostInformation> listPostinfoMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.mipmap.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkActivity.this.finish();
            }
        });
        titleActionbar = (TextView) findViewById(R.id.titleToolbar);
        titleActionbar.setText("我的收藏");

        recyclePostInfoMark = (RecyclerView) findViewById(R.id.recycleviewPostInfoMark);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        List<Map<String, String>> params = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "USER_ID");
        map.put("value", userId);
        params.add(map);
        markTask = new MarkInfoTask(RequestCode.CAT_MARK_INFO, params, this);
        markTask.execute();
    }

    @Override
    public void onDataGot(int requestcode, String result) {
        //TODO remove test statement
        result = "[{\"post_id\":1,\"company_name\":\"苏州微木智能系统有限公司\",\"salary_month\":\"6000-10000元/月\",\"work_place\":\"苏州\",\"post_date\":\"2017-03-23\",\"work_type\":\"全职\",\"experience_requirement\":\"不限\",\"education_requirement\":\"本科\",\"position_count\":1,\"position_type\":\"软件工程师\",\"category_name\":\"软件开发\"}]";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        if (requestcode == RequestCode.CAT_MARK_INFO) {
            listPostinfoMark = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
            recyclePostInfoMark.setAdapter(new ResultCompanyAdapter(listPostinfoMark, MarkActivity.this));
            recyclePostInfoMark.setLayoutManager(new LinearLayoutManager(MarkActivity.this));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (markTask != null) {
            markTask.cancel(true);
        }
    }
}
