package com.yzdsmart.Collectmoney.register_forget_password;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/25.
 */
public class RegisterForgetPasswordModel {
    //网络请求监听
    private Subscriber<String> isUserExistSubscriber;

    /**
     * 手机号是否已注册
     *
     * @param telNum
     * @param listener
     */
    void isUserExist(String telNum, final RequestListener listener) {
        isUserExistSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        };
        RequestAdapter.getRequestService().isUserExist(telNum)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(isUserExistSubscriber);
    }

    void unRegisterSubscribe() {
        if (null != isUserExistSubscriber) {
            //解除引用关系，以避免内存泄露的发生
            isUserExistSubscriber.unsubscribe();
        }
    }
}
