package com.hsj.okhttpdemo.net.subscribe;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class HsjSubscribe {
    /**
     * 获取标签
     * @param subscriber
     */
    public static void getTag(DisposableObserver<ResponseBody> subscriber) {
        Map<String,String> map = new HashMap<>();
        map.put("dimension", "4");
        map.put("language", "zh_CN");
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().getTag(RequestBody.create(new Gson().toJson(map), BaseSubscribe.JSON));
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    public static void getCodeSms(String mobile, DisposableObserver<ResponseBody> subscriber) {
        Map<String,String> map = new HashMap<>();
        map.put("mobile", mobile);
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().getCodeSms(map);
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    /**
     * 获取标签
     * @param subscriber
     */
    public static void getToken(DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().getToken();
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    /**
     * 上传头像接口
     * type 1=image 2=audio
     */
    public static void uploadFileToGetUrl(int type, File file, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().uploadImgFile(filesToMultipartBody(file, type));
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    private static MultipartBody filesToMultipartBody(File file, int type) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody = RequestBody.create(file, MediaType.parse(type == 1 ? "image/*" : "audio/*"));
        builder.addFormDataPart("file", file.getName(), requestBody);
        return builder.build();
    }
}
