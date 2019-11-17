package com.hsj.okhttpdemo.net.callback;

import com.hsj.okhttp.NetCallback;

/**
 * Create by hsj55
 * 2019/11/17
 */
public abstract class BaseCallback<T> extends NetCallback<T> {
    /**
     * 统一处理的逻辑，都在类似这样的方法中处理。暴露在每个最外层的callback只处理指定场景下的业务逻辑。
     */
    @Override
    public void logout() {

    }
}
