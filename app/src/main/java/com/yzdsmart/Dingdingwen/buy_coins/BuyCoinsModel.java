package com.yzdsmart.Dingdingwen.buy_coins;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsPayRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/4.
 */
public class BuyCoinsModel {
    //网络请求监听
    private Subscriber<RequestResponse> buyCoinsSubscriber;
    private Subscriber<BuyCoinsPayRequestResponse> buyCoinsPaySubscriber;
    private Subscriber<BuyCoinsLogRequestResponse> buyCoinsLogSubscriber;

    void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum, String authorization, final RequestListener listener) {
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
        RequestAdapter.getRequestService().buyCoins(action, submitCode, bazaCode, goldNum, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(buyCoinsSubscriber);
    }

    void buyCoinsPay(String payPara, String authorization, final RequestListener listener) {
        buyCoinsPaySubscriber = new Subscriber<BuyCoinsPayRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(BuyCoinsPayRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), payPara);
        RequestAdapter.getRequestService().buyCoinsPay(body, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(buyCoinsPaySubscriber);
    }

    void buyCoinsLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization, final RequestListener listener) {
        buyCoinsLogSubscriber = new Subscriber<BuyCoinsLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(BuyCoinsLogRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().buyCoinsLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(buyCoinsLogSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != buyCoinsSubscriber) {
            buyCoinsSubscriber.unsubscribe();
        }
        if (null != buyCoinsPaySubscriber) {
            buyCoinsPaySubscriber.unsubscribe();
        }
        if (null != buyCoinsLogSubscriber) {
            buyCoinsLogSubscriber.unsubscribe();
        }
    }
}
