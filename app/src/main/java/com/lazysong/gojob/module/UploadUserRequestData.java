package com.lazysong.gojob.module;

import com.lazysong.gojob.module.beans.MyUser;

/**
 * Created by lazysong on 2017/4/4.
 */
public class UploadUserRequestData extends BaseRequestData {
    private MyUser user;

    public UploadUserRequestData(MyUser user) {
        this.user = user;
    }

    public UploadUserRequestData(int requestCode, MyUser user) {
        super(requestCode);
        this.user = user;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
