package com.yzdsmart.Collectmoney.personal_coin_list;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.GetCoinsLogRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/5.
 */
public class PersonalCoinsModel {
    //网络请求监听
    private Subscriber<GetCoinsLogRequestResponse> getCoinsLogSubscriber;

    void getCoinsLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, final RequestListener listener) {
        getCoinsLogSubscriber = new Subscriber<GetCoinsLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GetCoinsLogRequestResponse getCoinsLogRequestResponse) {
                listener.onSuccess(getCoinsLogRequestResponse);
            }
        };
        RequestAdapter.getRequestService().getCoinsLog(action,submitCode,custCode,pageIndex,pageSize)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCoinsLogSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getCoinsLogSubscriber) {
            getCoinsLogSubscriber.unsubscribe();
        }
    }
}
