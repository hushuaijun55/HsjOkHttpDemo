package com.hsj.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class NetBuilder {

    private Retrofit retrofit;
    private OkHttpClient.Builder okHttpBuilder;
    public NetBuilder() {
        //手动创建一个OkHttpClient并设置超时时间
        okHttpBuilder = new OkHttpClient.Builder();
    }

    /**
     * 连接超时
     * @param outTime
     * @return
     */
    public NetBuilder setConnectTimeout(int outTime) {
        okHttpBuilder.connectTimeout(outTime, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 读超时
     * @param outTime
     * @return
     */
    public NetBuilder setReadTimeout(int outTime) {
        okHttpBuilder.readTimeout(outTime, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 写超时
     * @param outTime
     * @return
     */
    public NetBuilder setWriteTimeout(int outTime) {
        okHttpBuilder.writeTimeout(outTime, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 错误重连
     * @param retry
     * @return
     */
    public NetBuilder setRetryOnConnectionFailure(boolean retry) {
        okHttpBuilder.retryOnConnectionFailure(retry);
        return this;
    }

    /**
     * 添加拦截器
     * @param interceptor
     * @return
     */
    public NetBuilder addInterceptor(Interceptor interceptor) {
        okHttpBuilder.addInterceptor(interceptor);
        return this;
    }

    /**
     * 添加倒数第二层拦截器
     * @param interceptor
     * @return
     */
    public NetBuilder addNetInterceptor(Interceptor interceptor) {
        okHttpBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    /**
     * 添加缓存文件
     * @param cache
     * @return
     */
    public NetBuilder addCache(Cache cache) {
        okHttpBuilder.cache(cache);
        return this;
    }

    public Retrofit build(String url) {
        retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        return retrofit;
    }
}
