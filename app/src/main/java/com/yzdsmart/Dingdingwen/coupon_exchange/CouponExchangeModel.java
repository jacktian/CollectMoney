package com.yzdsmart.Dingdingwen.coupon_exchange;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.ScanCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopExchangeRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/12/19.
 */

public class CouponExchangeModel {
    //网络请求监听
    private Subscriber<ScanCoinRequestResponse> personalLeftCoinsSubscriber;
    private Subscriber<ScanCoinRequestResponse> shopLeftCoinsSubscriber;
    private Subscriber<ShopExchangeRequestResponse> getShopExchangeSubscriber;
    private Subscriber<ShopExchangeRequestResponse> getCoinExchangeSubscriber;
    private Subscriber<RequestResponse> exchangeCouponSubscriber;


    void getPersonalLeftCoins(String action, String actiontype, String submitCode, String custCode, Integer goldType, String authorization, final RequestListener listener) {
        personalLeftCoinsSubscriber = new Subscriber<ScanCoinRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ScanCoinRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getPersonalLeftCoins(action, actiontype, submitCode, custCode, goldType, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(personalLeftCoinsSubscriber);
    }

    void getShopLeftCoins(String action, String submitCode, String bazaCode, Integer goldType, String authorization, final RequestListener listener) {
        shopLeftCoinsSubscriber = new Subscriber<ScanCoinRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ScanCoinRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getDDWRequestService().getShopLeftCoins(action, submitCode, bazaCode, goldType, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(shopLeftCoinsSubscriber);
    }

    void getShopExchangeList(String action, String submitCode, String bazaCode, String custCode, String authorization, final RequestListener listener) {
        getShopExchangeSubscriber = new Subscriber<ShopExchangeRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopExchangeRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getShopExchangeList(action, submitCode, bazaCode, custCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopExchangeSubscriber);
    }

    void getCoinExchangeList(String action, String submitCode, Integer goldType, String custCode, String authorization, final RequestListener listener) {
        getCoinExchangeSubscriber = new Subscriber<ShopExchangeRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopExchangeRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getCoinExchangeList(action, submitCode, goldType, custCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCoinExchangeSubscriber);
    }

    void exchangeCoupon(String submitCode, String exchangeId, String custCode, String authorization, final RequestListener listener) {
        exchangeCouponSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getDDWRequestService().exchangeCoupon(submitCode, exchangeId, custCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(exchangeCouponSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != personalLeftCoinsSubscriber) {
            personalLeftCoinsSubscriber.unsubscribe();
        }
        if (null != shopLeftCoinsSubscriber) {
            shopLeftCoinsSubscriber.unsubscribe();
        }
        if (null != getShopExchangeSubscriber) {
            getShopExchangeSubscriber.unsubscribe();
        }
        if (null != getCoinExchangeSubscriber) {
            getCoinExchangeSubscriber.unsubscribe();
        }
        if (null != exchangeCouponSubscriber) {
            exchangeCouponSubscriber.unsubscribe();
        }
    }
}
