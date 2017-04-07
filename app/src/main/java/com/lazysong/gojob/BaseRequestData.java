package com.lazysong.gojob;

/**
 * Created by lazysong on 2017/4/4.
 */
public class BaseRequestData {
    private int requestCode;
    public BaseRequestData() {

    }
    public BaseRequestData(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}