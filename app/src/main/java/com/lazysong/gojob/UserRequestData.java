package com.lazysong.gojob;

/**
 * Created by lazysong on 2017/4/4.
 */
public class UserRequestData extends BaseRequestData {
    private String userId;

    public UserRequestData(int requestCode, String userId) {
        super(requestCode);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}