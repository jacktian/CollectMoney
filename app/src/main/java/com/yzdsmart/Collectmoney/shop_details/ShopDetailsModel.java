package com.yzdsmart.Collectmoney.shop_details;

import com.yzdsmart.Collectmoney.bean.ShopDetails;
import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/26.
 */
public class ShopDetailsModel {
    //网络请求监听
    private Subscriber<ShopDetails> getShopDetailsSubscriber;

    void getShopDetails(String submitCode, String bazaCode, final RequestListener listener) {
        getShopDetailsSubscriber = new Subscriber<ShopDetails>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopDetails shopDetails) {
                listener.onSuccess(shopDetails);
            }
        };
        RequestAdapter.getRequestService().getShopDetails(submitCode, bazaCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopDetailsSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getShopDetailsSubscriber) {
            getShopDetailsSubscriber.unsubscribe();
        }
    }
}
