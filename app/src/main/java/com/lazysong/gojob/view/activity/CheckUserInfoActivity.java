package com.lazysong.gojob.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.User;

import java.text.SimpleDateFormat;

public class CheckUserInfoActivity extends AppCompatActivity {
    private ImageView img;
    private TextView nickname;
    private TextView sign;
    private TextView sex;
    private TextView birthday;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user_info);

        initViews();
        user = (User) getIntent().getParcelableExtra("user");
        img.setImageBitmap(user.getImg());
        nickname.setText(user.getNickname());
        sign.setText(user.getSign());
        sex.setText(user.getSex() + " ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        birthday.setText(format.format(user.getBirthday()));
    }

    private void initViews() {
        img = (ImageView) findViewById(R.id.img_check);
        nickname = (TextView) findViewById(R.id.nickname_check);
        sign = (TextView) findViewById(R.id.sign_check);
        sex = (TextView) findViewById(R.id.sex_check);
        birthday = (TextView) findViewById(R.id.birthday_check);
    }
}
