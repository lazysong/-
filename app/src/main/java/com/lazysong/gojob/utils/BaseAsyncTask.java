package com.lazysong.gojob.utils;

import android.content.Context;
import android.os.AsyncTask;

import okhttp3.OkHttpClient;

/**
 * Created by lazysong on 2017/6/4.
 */
public abstract class BaseAsyncTask extends AsyncTask<Void, Void, String>{
//    protected final String BASE_URL = "http://www.lazysong.cn:8080/GoJob";
    protected final String BASE_URL = "http://192.168.0.104:8080/Test";
    protected final OkHttpClient client = new OkHttpClient();
    protected int requestcode;
    protected Context context;

    public BaseAsyncTask(int requestcode, Context context) {
        this.requestcode = requestcode;
        this.context = context;
    }

    public int getRequestcode() {
        return requestcode;
    }

    public void setRequestcode(int requestcode) {
        this.requestcode = requestcode;
    }

    public interface HandleView {
        public void HandleView(int requestcode, String result);
    }
}
