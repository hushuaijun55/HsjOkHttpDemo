package com.hsj.okhttpdemo.net.interceptor;

import com.hsj.logger.HsjLogger;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class NetInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        ResponseBody responseBody = response.body();
        String body = null;
        if (responseBody != null) {
            body = responseBody.source().readString(Charset.forName("UTF-8"));
        }

        HsjLogger.d("response code = " + response.code());
        HsjLogger.d("response msg = " + body);

        return response;
    }
}