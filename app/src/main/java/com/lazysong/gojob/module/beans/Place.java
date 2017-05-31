package com.lazysong.gojob.module.beans;

/**
 * Created by lazysong on 2017/5/31.
 */
public class Place {
    String placeName;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Place(String placeName) {

        this.placeName = placeName;
    }
}
