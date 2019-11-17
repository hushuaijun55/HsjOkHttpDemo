package com.hsj.okhttp.interceptor;

import android.content.Context;

import com.hsj.okhttp.HsjNetConstants;
import com.hsj.okhttp.util.NetUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class CacheInterceptor implements Interceptor {
    private Context mContext;
    public CacheInterceptor(Context context) {
        mContext = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtil.isNetworkConnected(mContext)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (NetUtil.isNetworkConnected(mContext)) {
            int maxAge = 0;
            // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader(HsjNetConstants.CACHE_NAME)// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        }  else {
            // 无网络时，设置超时为4周
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader(HsjNetConstants.CACHE_NAME)
                    .build();
        }
        return response;
    }
}
