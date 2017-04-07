package com.lazysong.gojob;

/**
 * Created by lazysong on 2017/4/4.
 */
public interface UrlConstructor {
    String constructUrlString(String baseUrlString, BaseRequestData data);
}
