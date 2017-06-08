package com.lazysong.gojob.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by lazysong on 2017/6/8.
 */
public class BitmapLoader extends AsyncTask<Void, Void, Void> {
    private final String bitmapUrl;
    private ImageView view;
    private Bitmap bitmap;

    public BitmapLoader(String bitmapUrl, ImageView view) {
        this.bitmapUrl = bitmapUrl;
        this.view = view;
    }
    @Override
    protected Void doInBackground(Void... params) {
        bitmap = Util.getbitmap(bitmapUrl);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (bitmap != null)
            view.setImageBitmap(bitmap);
    }
}
