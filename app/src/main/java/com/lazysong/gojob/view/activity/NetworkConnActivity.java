package com.lazysong.gojob.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lazysong.gojob.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkConnActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button getNetworkPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_conn);
        imageView = (ImageView) findViewById(R.id.imageView);
        getNetworkPic = (Button) findViewById(R.id.getNetworkPic);
        getNetworkPic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.getNetworkPic:
                DownloadPicTask task = new DownloadPicTask();
                task.execute("http://192.168.196.177:8080/Test/img/bilibili.png");
                break;
        }
    }

    private class DownloadPicTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlString = urls[0];
            try {
                InputStream input = downloadUrl(urlString);
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* 毫秒 */);
        conn.setConnectTimeout(15000 /* 毫秒 */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        return conn.getInputStream();
    }
}
