package com.lazysong.gojob;

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
import com.lazysong.gojob.com.lazysong.gojob.beans.BaseUser;
import com.lazysong.gojob.com.lazysong.gojob.beans.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CatUserActivity extends AppCompatActivity implements View.OnClickListener, UrlConstructor {
    private Button btnCatUser;
    private EditText editTextUserId;
    private TextView tvNickname;
    private TextView tvSex;
    private TextView tvBirthday;
    private ImageView userImage;
    private User user;
    private Button modifyUser;

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
        userImage = (ImageView) findViewById(R.id.imageUser);
        modifyUser = (Button) findViewById(R.id.btnModifyUser);

        btnCatUser.setOnClickListener(this);
        modifyUser.setOnClickListener(this);
    }

    public final static int MODIFY_USER_INFO = 1;
    public final static int MODIFY_SUCCESS = 2;
    public final static int MODIFY_CANCLE = 3;
//    public final static String BASE_URL_STR = "http://192.168.196.184:8080";//虚拟机地址
    public final static String BASE_URL_STR = "http://192.168.18.112:8080";//本地地址

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnCatUser:
                DownUserTask task = new DownUserTask();
                task.execute(BASE_URL_STR + "/Test/a.scaction", editTextUserId.getText().toString());
                break;
            case R.id.btnModifyUser:
                if (user != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, ModifyUserActivity.class);
                    Bundle data = new Bundle();
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
            String urlString = urls[0];
            String userid = urls[1];
            UserRequestData data;
            if (!urlString.isEmpty() && !userid.isEmpty())
                data = new UserRequestData(RequestCode.CAT_USER, userid);
            else
                data = new UserRequestData(RequestCode.CAT_USER, "1");
            InputStream input = null;
            BaseUser baseUser = null;
            User user = null;
            try {
                int requestCode = data.getRequestCode();
                urlString = constructUrlString(urlString, data);
                input = downladUrl(urlString, data);
                baseUser = readStreamUser(input);
                input.close();
                if(baseUser == null)
                    return null;
                Bitmap bitmap = downloadImg(BASE_URL_STR + "/img/" + baseUser.getImgName());
                user = new User(baseUser, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    }

    private Bitmap downloadImg(String urlString) throws IOException {
        InputStream input = null;
        Bitmap bitmap = null;
        try {
            input = downladUrl(urlString, null);
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

    private InputStream downladUrl(String urlString, BaseRequestData data) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(10000/*毫秒*/);
        conn.setConnectTimeout(15000/*毫秒*/);
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    @Override
    public String constructUrlString(String baseUrlString, BaseRequestData data) {
        String urlString = baseUrlString +
                "?requestCode=" + RequestCode.CAT_USER +
                "&userid=" + ((UserRequestData)data).getUserId();
        return urlString;
    }
}
