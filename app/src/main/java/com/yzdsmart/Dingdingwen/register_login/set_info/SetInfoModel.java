package com.yzdsmart.Dingdingwen.register_login.set_info;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.LoginRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/23.
 */

public class SetInfoModel {
    //网络请求监听
    private Subscriber<RequestResponse> registerSubscriber;
    private Subscriber<LoginRequestResponse> loginSubscriber;

    void userRegister(String actioncode, String userName, String password, String cSex, Integer cAge, String cNickName, String regCode, String authorization, final RequestListener listener) {
        registerSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().userRegister(actioncode, userName, password, cSex, cAge, cNickName, regCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(registerSubscriber);
    }

    void userLogin(String userName, String password, String loginCode, String authorization, final RequestListener listener) {
        loginSubscriber = new Subscriber<LoginRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(LoginRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().userLogin(userName, password, loginCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(loginSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != registerSubscriber) {
            registerSubscriber.unsubscribe();
        }
        if (null != loginSubscriber) {
            loginSubscriber.unsubscribe();
        }
    }
}
