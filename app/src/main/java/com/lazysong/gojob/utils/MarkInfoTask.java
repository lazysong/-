package com.lazysong.gojob.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lazysong on 2017/6/4.
 */
public class MarkInfoTask extends BaseAsyncTask {
    protected List<Map<String, String>> params;

    public MarkInfoTask(int requestcode, List<Map<String, String>> params, Context context) {
        super(requestcode, context);
        this.params = params;
    }

    @Override
    protected String doInBackground(Void... paras) {
        String urlStr = BASE_URL + "/a.scaction?requestcode=" + requestcode;
        for (int i = 0; i < params.size(); i++) {
            urlStr = urlStr + "&" + params.get(i).get("name") + "=" + params.get(i).get("value");
        }
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
        ((OnDataGotListener)context).onDataGot(requestcode, result);
    }

    public interface OnDataGotListener {
        void onDataGot(int requestcode, String result);
    }
}
