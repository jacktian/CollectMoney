package com.yzdsmart.Dingdingwen.buy_coins;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PayRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopPayLogRequestResponse;

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
    private Subscriber<PayRequestResponse> buyCoinsSubscriber;
    private Subscriber<PayRequestResponse> buyCoinsPaySubscriber;
    private Subscriber<BuyCoinsLogRequestResponse> buyCoinsLogSubscriber;
    private Subscriber<ShopPayLogRequestResponse> shopPayLogSubscriber;
    private Subscriber<PayRequestResponse> notPayChargeSubscriber;

    //    void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum, String authorization, final RequestListener listener) {
//        buyCoinsSubscriber = new Subscriber<RequestResponse>() {
//            @Override
//            public void onCompleted() {
//                listener.onComplete();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                listener.onError(e.getMessage());
//            }
//
//            @Override
//            public void onNext(RequestResponse response) {
//                listener.onSuccess(response);
//            }
//        };
//        RequestAdapter.getDDWRequestService().buyCoins(action, submitCode, bazaCode, goldNum, authorization)
//                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
//                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
//                .subscribe(buyCoinsSubscriber);
//    }
    void buyCoins(String action, String buyCoinPara, String authorization, final RequestListener listener) {
        buyCoinsSubscriber = new Subscriber<PayRequestResponse>() {
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), buyCoinPara);
        RequestAdapter.getDDWRequestService().buyCoins(action, body, "application/json", "application/json", authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(buyCoinsSubscriber);
    }

    void buyCoinsPay(String payPara, String authorization, final RequestListener listener) {
        buyCoinsPaySubscriber = new Subscriber<PayRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(PayRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), payPara);
        RequestAdapter.getDDWRequestService().buyCoinsPay(body, "application/json", "application/json", authorization)
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
        RequestAdapter.getDDWRequestService().buyCoinsLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(buyCoinsLogSubscriber);
    }

    void getShopPayLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization, final RequestListener listener) {
        shopPayLogSubscriber = new Subscriber<ShopPayLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopPayLogRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getShopPayLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(shopPayLogSubscriber);
    }

    void getNotPayCharge(String submitCode, String chargeId, String authorization, final RequestListener listener) {
        notPayChargeSubscriber = new Subscriber<PayRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(PayRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getNotPayCharge(submitCode, chargeId, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(notPayChargeSubscriber);
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
        if (null != shopPayLogSubscriber) {
            shopPayLogSubscriber.unsubscribe();
        }
        if (null != notPayChargeSubscriber) {
            notPayChargeSubscriber.unsubscribe();
        }
    }
}
