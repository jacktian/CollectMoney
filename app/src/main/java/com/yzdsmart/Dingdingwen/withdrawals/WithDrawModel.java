package com.yzdsmart.Dingdingwen.withdrawals;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.WithdrawRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jacks on 2016/9/23.
 */

public class WithDrawModel {
    //网络请求监听
    private Subscriber<CustInfoRequestResponse> getCustInfoSubscriber;
    private Subscriber<GetCoinRequestResponse> leftCoinsSubscriber;
    private Subscriber<WithdrawRequestResponse> shopWithdrawSubscriber;
    private Subscriber<WithdrawRequestResponse> personalWithdrawSubscriber;

    void getCustInfo(String submitcode, String custCode, final RequestListener listener) {
        getCustInfoSubscriber = new Subscriber<CustInfoRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(CustInfoRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().getCustInfo(submitcode, custCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustInfoSubscriber);
    }

    void getLeftCoins(String action, String submitCode, String bazaCode, final RequestListener listener) {
        leftCoinsSubscriber = new Subscriber<GetCoinRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GetCoinRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().getLeftCoins(action, submitCode, bazaCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(leftCoinsSubscriber);
    }

    void shopWithdrawCoins(String action, String submitCode, String bazaCode, Integer goldNum, final RequestListener listener) {
        shopWithdrawSubscriber = new Subscriber<WithdrawRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(WithdrawRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().shopWithdrawCoins(action, submitCode, bazaCode, goldNum)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(shopWithdrawSubscriber);
    }

    void personalWithdrawCoins(String action, String actiontype, String submitCode, String custCode, Integer goldNum, final RequestListener listener) {
        personalWithdrawSubscriber = new Subscriber<WithdrawRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(WithdrawRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().personalWithdrawCoins(action, actiontype, submitCode, custCode, goldNum)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(personalWithdrawSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getCustInfoSubscriber) {
            getCustInfoSubscriber.unsubscribe();
        }
        if (null != leftCoinsSubscriber) {
            leftCoinsSubscriber.unsubscribe();
        }
        if (null != shopWithdrawSubscriber) {
            shopWithdrawSubscriber.unsubscribe();
        }
        if (null != personalWithdrawSubscriber) {
            personalWithdrawSubscriber.unsubscribe();
        }
    }
}
