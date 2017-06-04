package com.lazysong.gojob.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.beans.Industry_category;
import com.lazysong.gojob.module.beans.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lazysong on 2017/6/4.
 */
public class GetAllTask extends BaseAsyncTask {

    public GetAllTask(int requestcode, Context context) {
        super(requestcode, context);
    }

    @Override
    protected String doInBackground(Void... params) {
        String urlStr = BASE_URL + "/a.scaction?requestcode=" + requestcode;
        Request request = new Request.Builder().url(urlStr).build();
        Response response;
        String result = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
            else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "似乎出了点问题", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ((HandleView)context).HandleView(requestcode, result);
    }
}
