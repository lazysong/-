package com.lazysong.gojob.module;

import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.BaseRequestData;
import com.lazysong.gojob.module.beans.User;

/**
 * Created by lazysong on 2017/4/4.
 */
public class UploadUserRequestData extends BaseRequestData {
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
