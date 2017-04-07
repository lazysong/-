package com.lazysong.gojob;

import com.lazysong.gojob.com.lazysong.gojob.beans.User;

/**
 * Created by lazysong on 2017/4/4.
 */
public class UploadUserRequestData extends BaseRequestData{
    private User user;

    public UploadUserRequestData(User user) {
        this.user = user;
    }

    public UploadUserRequestData(int requestCode, User user) {
        super(requestCode);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
