package com.yzdsmart.Collectmoney.shop_scanned_log;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.ScannedLogRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/10/9.
 */

public class ShopScannedLogModel {
    //网络请求监听
    private Subscriber<ScannedLogRequestResponse> getScannedLogSubscriber;

    void getScannedLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, final RequestListener listener) {
        getScannedLogSubscriber = new Subscriber<ScannedLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ScannedLogRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().getScannedLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getScannedLogSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getScannedLogSubscriber) {
            getScannedLogSubscriber.unsubscribe();
        }
    }
}
