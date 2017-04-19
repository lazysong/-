package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lazysong.gojob.module.BaseRequestData;
import com.lazysong.gojob.R;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.controler.UrlConstructor;
import com.lazysong.gojob.module.ServerInfoManager;
import com.lazysong.gojob.module.UserRequestData;
import com.lazysong.gojob.module.beans.BaseUser;
import com.lazysong.gojob.module.beans.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CatUserActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnCatUser;
    private EditText editTextUserId;
    private TextView tvNickname;
    private TextView tvSex;
    private TextView tvBirthday;
    private ImageView userImage;
    private User user;
    private Button modifyUser;
    private TextView tvSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_user);

        initViews();
    }

    private void initViews() {
        btnCatUser = (Button) findViewById(R.id.btnCatUser);
        editTextUserId = (EditText) findViewById(R.id.textUserId);
        tvNickname = (TextView) findViewById(R.id.tvNickname);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvBirthday = (TextView) findViewById(R.id.tvBirthday);
        tvSign = (TextView) findViewById(R.id.tvSign);
        userImage = (ImageView) findViewById(R.id.imageUser);
        modifyUser = (Button) findViewById(R.id.btnModifyUser);

        btnCatUser.setOnClickListener(this);
        modifyUser.setOnClickListener(this);
    }

    public final static int MODIFY_USER_INFO = 1;
    public final static int MODIFY_SUCCESS = 2;
    public final static int MODIFY_CANCLE = 3;
//    public final static String BASE_URL_STR = "http://192.168.196.184:8080";//虚拟机地址
    public final static String BASE_URL_STR = "http://192.168.18.188:8080";//本地地址

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnCatUser:
                DownUserTask task = new DownUserTask();
                task.execute(editTextUserId.getText().toString());
                break;
            case R.id.btnModifyUser:
                if (user != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, ModifyUserActivity.class);
                    BaseUser baseUser = user.getBaseuser();
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    user.getImg().compress(Bitmap.CompressFormat.PNG, 100, bao);
                    byte[] imgBytes = bao.toByteArray();
                    intent.putExtra("baseUser", baseUser);
                    intent.putExtra("img", imgBytes);
                    startActivityForResult(intent, MODIFY_USER_INFO);
                }
                else {
                    Toast.makeText(CatUserActivity.this, "user = null", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFY_USER_INFO && resultCode == MODIFY_SUCCESS) {
            BaseUser baseUser = (BaseUser) data.getSerializableExtra("baseUser");
            byte[] imgBytes = data.getByteArrayExtra("img");
            Bitmap img = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            user = new User(baseUser, img);
            postViewData(user);
        }
    }

    private class DownUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... urls) {
            String userid = urls[0];
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("userid", userid);
            BaseRequestData requestData = new BaseRequestData(RequestCode.CAT_USER, data);
            ServerInfoManager manager = new ServerInfoManager(CatUserActivity.this);
            Map<String, Object> result = manager.constrRequest(requestData).request();
            user = (User) result.get("user");
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if(user != null && user.getUserid() != null) {
                CatUserActivity.this.user = user;
                postViewData(user);
            }
            else {
                Toast.makeText(CatUserActivity.this, "未查询到相关用户信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postViewData(User user) {
        tvNickname.setText(user.getNickname());
        tvSex.setText(user.getSex() + "");
        tvBirthday.setText(user.getBirthday().getYear() + "年" + user.getBirthday().getMonth() + "月");
        userImage.setImageBitmap(user.getImg());
        tvSign.setText(user.getSign());
    }
}
