package com.yzdsmart.Dingdingwen.register_login.login;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.LoginRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/26.
 */
public class LoginModel {
    //网络请求监听
    private Subscriber<LoginRequestResponse> loginSubscriber;

    void userLogin(String userName, String password, String loginCode, final RequestListener listener) {
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
        RequestAdapter.getRequestService().userLogin(userName, password, loginCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(loginSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != loginSubscriber) {
            loginSubscriber.unsubscribe();
        }
    }
}
