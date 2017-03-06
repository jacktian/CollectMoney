package com.yzdsmart.Dingdingwen.payment;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.PayRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ScanCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopDiscountRequestResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/12/18.
 */

public class PaymentModel {
    //网络请求监听
    private Subscriber<ScanCoinRequestResponse> personalLeftCoinsSubscriber;
    private Subscriber<ScanCoinRequestResponse> shopLeftCoinsSubscriber;
    private Subscriber<ShopDiscountRequestResponse> getShopDiscountsSubscriber;
    private Subscriber<PayRequestResponse> submitPaymentSubscriber;

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

    void getShopDiscounts(String submitCode, String bazaCode, String authorization, final RequestListener listener) {
        getShopDiscountsSubscriber = new Subscriber<ShopDiscountRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopDiscountRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getShopDiscounts(submitCode, bazaCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopDiscountsSubscriber);
    }

    void submitPayment(String action, String paymentPara, String authorization, final RequestListener listener) {
        submitPaymentSubscriber = new Subscriber<PayRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(PayRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paymentPara);
        RequestAdapter.getDDWRequestService().submitPayment(action, body, "application/json", "application/json", authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(submitPaymentSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != personalLeftCoinsSubscriber) {
            personalLeftCoinsSubscriber.unsubscribe();
        }
        if (null != shopLeftCoinsSubscriber) {
            shopLeftCoinsSubscriber.unsubscribe();
        }
        if (null != getShopDiscountsSubscriber) {
            getShopDiscountsSubscriber.unsubscribe();
        }
        if (null != submitPaymentSubscriber) {
            submitPaymentSubscriber.unsubscribe();
        }
    }
}
