package com.lazysong.gojob;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lazysong.gojob.com.lazysong.gojob.beans.BaseUser;
import com.lazysong.gojob.com.lazysong.gojob.beans.User;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyUserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvUserid;
    private Button btnConfirm;
    private Button btnCancle;
    private EditText edtNickname;
    private EditText edtSign;
    private EditText edtBirthday;
    private EditText edtSex;
    private ImageView imgUser;
    private BaseUser baseUser;
    private Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        Intent intent = getIntent();
        baseUser = (BaseUser) intent.getSerializableExtra("baseUser");
        byte[] imgBytes = intent.getByteArrayExtra("img");
        img = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        initViews();
    }

    private void initViews() {
        tvUserid = (TextView) findViewById(R.id.userid_edit);
        btnConfirm = (Button) findViewById(R.id.confirm_edit);
        btnCancle = (Button) findViewById(R.id.cancle_edit);
        edtNickname = (EditText) findViewById(R.id.nickname_edit);
        edtSign = (EditText) findViewById(R.id.sign_edit);
        edtBirthday = (EditText) findViewById(R.id.birthday_edit);
        edtSex = (EditText) findViewById(R.id.sex_edit);
        imgUser = (ImageView) findViewById(R.id.img_edit);
        btnConfirm.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        if(baseUser != null) {
            tvUserid.setText(baseUser.getUserid());
            edtNickname.setText(baseUser.getNickname());
            edtSign.setText(baseUser.getSign());
            edtBirthday.setText(baseUser.getBirthday().getYear() + "");
            edtSex.setText(baseUser.getSex() + "");
            imgUser.setImageBitmap(img);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm_edit:
                baseUser = getUserFromView();
                if (baseUser != null) {
                    uploadUserInfo(baseUser, img);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.PNG, 100, bao);
                    byte[] imgBytes= bao.toByteArray();
                    Intent intent = new Intent();
                    intent.putExtra("baseUser", baseUser);
                    intent.putExtra("img", imgBytes);
                    setResult(CatUserActivity.MODIFY_SUCCESS, intent);
                }
                finish();
                break;
            case R.id.cancle_edit:
                setResult(CatUserActivity.MODIFY_CANCLE);
                finish();
                break;
        }
    }

    private void uploadUserInfo(BaseUser baseUser, Bitmap img) {

    }

    private User getUserFromView() {
        User user = new User();
        user.setNickname(edtNickname.getText().toString());
        user.setSign(edtSign.getText().toString());
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        try {
            user.setBirthday(format.parse(edtBirthday.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            user.setBirthday(new Date(1990,1,1));
        }
        user.setSex(Integer.parseInt(edtSex.getText().toString()));
//        user.setImg(imgUser.getDrawingCache());
        return user;
    }
}
