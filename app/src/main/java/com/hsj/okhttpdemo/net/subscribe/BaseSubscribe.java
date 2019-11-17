package com.hsj.okhttpdemo.net.subscribe;

import android.text.TextUtils;

import com.hsj.okhttp.HsjNetConstants;
import com.hsj.okhttp.NetBuilder;
import com.hsj.okhttp.interceptor.CacheInterceptor;
import com.hsj.okhttp.interceptor.HeaderInterceptor;
import com.hsj.okhttpdemo.App;
import com.hsj.okhttpdemo.net.NetConstants;
import com.hsj.okhttpdemo.net.api.OkHttpApi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class BaseSubscribe {
    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TOKEN = "";
    private static final String USERID = "";

    private static OkHttpApi okHttpApi;

    public static OkHttpApi getOkHttpApi() {
        if (okHttpApi==null) {
            okHttpApi = newApi(NetConstants.BASE_URL, getHeaderParams(TOKEN, USERID), OkHttpApi.class);
        }
        return okHttpApi;
    }

    /**
     * 关键方法，可以根据需求产出不同的api
     * @param baseUrl
     * @param params
     * @param apiClass
     * @param <T>
     * @return
     */
    public static <T>T newApi(String baseUrl, Map<String, String> params, Class<T> apiClass) {
        NetBuilder builder = new NetBuilder()
                .setConnectTimeout(HsjNetConstants.DEFAULT_CONNECT_TIMEOUT)
                .setReadTimeout(HsjNetConstants.DEFAULT_READ_TIMEOUT)
                .setWriteTimeout(HsjNetConstants.DEFAULT_WRITE_TIMEOUT)
                .setRetryOnConnectionFailure(true);
        //添加header
        if (params != null && params.size()>0) {
            HeaderInterceptor headerInterceptor = new HeaderInterceptor(params);
            builder.addInterceptor(headerInterceptor);
        }
        //添加缓存
        File cacheFile = new File(App.getAppContext().getExternalCacheDir(), HsjNetConstants.CACHE_NAME);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        builder.addCache(cache);

        //添加缓存拦截器
        CacheInterceptor cacheInterceptor = new CacheInterceptor(App.getAppContext());
        builder.addInterceptor(cacheInterceptor);

//        builder.addNetInterceptor(new NetInterceptor());

        return builder.build(baseUrl).create(apiClass);
    }

    /**
     * 设置订阅 和 所在的线程环境
     * @param o
     * @param s
     */
    public static void toSubscribe(Observable<ResponseBody> o, DisposableObserver<ResponseBody> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(1)//请求失败重连次数
                .subscribe(s);
    }

    private static Map<String, String> getHeaderParams(String token, String userId) {
        String timestamp, signature;

        timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(token)) {
            params.put("token", token);
        }
        if (!TextUtils.isEmpty(userId) && !"null".equals(userId)) {
            params.put("userId", userId);
        }
        params.put("nounce", "");
        params.put("timestamp", timestamp);
        signature = "";
        params.put("signature", signature);

        return params;
    }
}
