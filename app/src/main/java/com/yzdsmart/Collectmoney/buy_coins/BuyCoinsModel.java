package com.yzdsmart.Collectmoney.buy_coins;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.BuyCoinLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/4.
 */
public class BuyCoinsModel {
    //网络请求监听
    private Subscriber<RequestResponse> buyCoinsSubscriber;
    private Subscriber<BuyCoinLogRequestResponse> buyCoinsLogSubscriber;

    void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum, final RequestListener listener) {
        buyCoinsSubscriber = new Subscriber<RequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(RequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().buyCoins(action, submitCode, bazaCode, goldNum)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(buyCoinsSubscriber);
    }

    void buyCoinsLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, final RequestListener listener) {
        buyCoinsLogSubscriber = new Subscriber<BuyCoinLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(BuyCoinLogRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().buyCoinsLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(buyCoinsLogSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != buyCoinsSubscriber) {
            buyCoinsSubscriber.unsubscribe();
        }
        if (null != buyCoinsLogSubscriber) {
            buyCoinsLogSubscriber.unsubscribe();
        }
    }
}
