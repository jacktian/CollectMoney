package com.yzdsmart.Collectmoney.listener;

/**
 * Created by YZD on 2016/8/23.
 */
public interface HttpRequestListener {
    /**
     * 请求网络成功
     *
     * @param result
     */
    void onSuccess(Object result);

    /**
     * 请求网络失败
     *
     * @param err
     */
    void onError(Object err);

    /**
     * 网络请求结束
     */
    void onComplete();
}
