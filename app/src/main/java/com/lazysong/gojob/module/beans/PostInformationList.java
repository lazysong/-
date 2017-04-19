package com.lazysong.gojob.module.beans;

import java.util.ArrayList;

/**
 * Created by lazysong on 2017/3/3.
 */
public class PostInformationList {
    private ArrayList<PostInformation> list;

    public PostInformationList() {
    }

    public PostInformationList(ArrayList<PostInformation> list) {
        super();
        this.list = list;
    }

    public ArrayList<PostInformation> getList() {
        return list;
    }

    public void setList(ArrayList<PostInformation> list) {
        this.list = list;
    }
}

