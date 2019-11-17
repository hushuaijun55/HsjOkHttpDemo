package com.hsj.okhttp;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.hsj.logger.HsjLogger;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Create by hsj55
 * 2019/11/17
 */
public abstract class NetCallback<T> extends DisposableObserver<ResponseBody> implements APICallback<T> {
    /**
     * 处理各种业务逻辑及错误
     * @param body
     */
    @Override
    public void onNext(ResponseBody body) {
        try {
            String result = body.string();
            HsjLogger.longInfo(result);
            JSONObject jsonObject = new JSONObject(result);
            String resultCode = jsonObject.getString("code");
            if ("20000".equals(resultCode)) {
                Gson gson = new Gson();
                String data = jsonObject.getString("data");
                if (!TextUtils.isEmpty(data)) {
                    HsjLogger.longInfo(data);
                    T t = gson.fromJson(data, getType());
                    onSuccess(t);
                }
            } else if ("00011".equals(resultCode)) {
                //token过期，需要重新登录
                logout();
            } else {
                String errorMsg = jsonObject.getString("message");
                onFault(0, errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求出错，包含网络错误
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        HsjLogger.d(e.getMessage());
        netError();
    }

    @Override
    public void onComplete() {

    }

    private final Type getType() {
        /**获取第一个泛型类型*/
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return parameterizedType.getActualTypeArguments()[0];
    }
}
