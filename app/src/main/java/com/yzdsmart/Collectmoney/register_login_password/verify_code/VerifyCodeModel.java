package com.yzdsmart.Collectmoney.register_login_password.verify_code;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyCodeModel {
    //网络请求监听
    private Subscriber<RequestResponse> getVerifyCodeSubscriber;
    private Subscriber<RequestResponse> verifyVerifyCodeSubscriber;

    /**
     * 获取短信验证码
     *
     * @param telNum
     * @param listener
     */
    void getVerifyCode(String telNum, String currDate, final RequestListener listener) {
        getVerifyCodeSubscriber = new Subscriber<RequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(RequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().getVerifyCode(telNum, currDate)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getVerifyCodeSubscriber);
    }

    void validateVerifyCode(String actioncode, String telNum, String verifyCode, final RequestListener listener) {
        verifyVerifyCodeSubscriber = new Subscriber<RequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(RequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().validateVerifyCode(actioncode, telNum, verifyCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(verifyVerifyCodeSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getVerifyCodeSubscriber) {
            getVerifyCodeSubscriber.unsubscribe();
        }
        if (null != verifyVerifyCodeSubscriber) {
            verifyVerifyCodeSubscriber.unsubscribe();
        }
    }
}
