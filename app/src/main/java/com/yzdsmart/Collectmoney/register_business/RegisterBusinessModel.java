package com.yzdsmart.Collectmoney.register_business;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RegisterBusinessRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/4.
 */
public class RegisterBusinessModel {
    //网络请求监听
    private Subscriber<RegisterBusinessRequestResponse> registerBusinessSubscriber;

    void registerBusiness(String submitCode, String custCode, String bazaName, String bazaPers, String bazaTel, String bazaAddr, String remark, String coor, final RequestListener listener) {
        registerBusinessSubscriber = new Subscriber<RegisterBusinessRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(RegisterBusinessRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().registerBusiness(submitCode,custCode,bazaName,bazaPers,bazaTel,bazaAddr,remark,coor)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(registerBusinessSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != registerBusinessSubscriber) {
            registerBusinessSubscriber.unsubscribe();
        }
    }
}
