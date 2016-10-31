package com.yzdsmart.Dingdingwen.http;

/**
 * Created by YZD on 2016/7/13.
 */
public interface RequestListener {
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
    void onError(String err);

    /**
     * 网络请求结束
     */
    void onComplete();
}
