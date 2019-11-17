package com.hsj.okhttp;

/**
 * Create by hsj55
 * 2019/11/17
 */
public interface APICallback<T> {
    void logout();

    void onSuccess(T result);

    void onFault(int code, String errorMsg);

    void netError();
}
