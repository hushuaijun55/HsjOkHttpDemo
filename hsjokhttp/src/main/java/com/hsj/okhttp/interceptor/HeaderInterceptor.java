package com.hsj.okhttp.interceptor;

import android.text.TextUtils;

import com.hsj.logger.HsjLogger;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class HeaderInterceptor implements Interceptor {
    private Map<String, String> params;

    public HeaderInterceptor(Map<String, String> p) {
        this.params = p;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        //设置头部信息
        Request.Builder requestBuilder = originalRequest.newBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (TextUtils.isEmpty(value) || "null".equals(value)) {
                    value = "";
                }
                requestBuilder.addHeader(entry.getKey(), value);
            }
        }

        requestBuilder.method(originalRequest.method(), originalRequest.body());
        Request request = requestBuilder.build();

        //打印请求信息，正式项目中可以注释掉
        RequestBody requestBody = request.body();
        String body = null;
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            body = buffer.readString(Charset.forName("UTF-8"));
        }
        HsjLogger.longInfo(String.format("发送请求\nmethod：%s\nurl：%s\nheaders: %sbody：%s",
                request.method(), request.url(), request.headers(), body));

        return chain.proceed(request);
    }
}