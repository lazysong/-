package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lazysong.gojob.R;
import com.lazysong.gojob.view.activity.CatUserActivity;

public class UserManageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button catUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);

        catUser = (Button) findViewById(R.id.catUser);

        catUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        switch (id) {
            case R.id.catUser:
                intent.setClass(this, CatUserActivity.class);
                break;
        }
        startActivity(intent);
    }
}
