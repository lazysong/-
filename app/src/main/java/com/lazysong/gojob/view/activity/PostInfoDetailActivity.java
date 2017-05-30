package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.PostInformation;

public class PostInfoDetailActivity extends AppCompatActivity {
    private PostInformation postInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info_detail);

        Intent intent = getIntent();
        String postInfoStr = intent.getStringExtra("postInfo");
        final Gson gson = new Gson();
        postInformation = gson.fromJson(postInfoStr, PostInformation.class);

    }
}
