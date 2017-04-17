package com.lazysong.gojob;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lazysong.gojob.com.lazysong.gojob.beans.BaseUser;
import com.lazysong.gojob.com.lazysong.gojob.beans.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class ModifyUserActivity extends AppCompatActivity implements View.OnClickListener, UrlConstructor {
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
//    private final static String BASEURL = "http://192.168.196.184:8080";//虚拟机地址
    private final static String BASEURL = "http://192.168.18.112:8080";//本地地址

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
        imgUser.setOnClickListener(this);

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
                getUserFromView();
                if (baseUser != null) {
                    UploadUserInfoTask task = new UploadUserInfoTask();
                    User user = new User(baseUser, img);
                    task.execute(user);
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

    private void getUserFromView() {

        baseUser.setNickname(edtNickname.getText().toString());
        baseUser.setSign(edtSign.getText().toString());
        baseUser.setImgName(baseUser.getUserid() + ".png");
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        try {
            baseUser.setBirthday(format.parse(edtBirthday.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            baseUser.setBirthday(new Date(1990,1,1));
        }
        baseUser.setSex(Integer.parseInt(edtSex.getText().toString()));
//        user.setImg(imgUser.getDrawingCache());
    }

    private class UploadUserInfoTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            User user = users[0];
            String fileName = user.getImgName();
            File file = new File(getFilesDir(), fileName);
            try {
                FileOutputStream outputStream = openFileOutput(fileName, MODE_PRIVATE);
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
            String url = constructUrlString(BASEURL, null);
            postFile(url, null, file);
            return null;
        }
    }
    

    @Override
    public String constructUrlString(String baseUrlString, BaseRequestData data) {
        String urlString = baseUrlString + "/Test/a.scaction?requestCode=" + RequestCode.UPLOAD_USER;
        return urlString;
    }

    protected void postFile(final String url, final Map<String, Object> map, File file) {
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
}
