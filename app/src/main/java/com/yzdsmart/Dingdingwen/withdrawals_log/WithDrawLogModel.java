package com.yzdsmart.Dingdingwen.withdrawals_log;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.PersonalWithdrawLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopWithdrawLogRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/24.
 */

public class WithDrawLogModel {
    //网络请求监听
    private Subscriber<PersonalWithdrawLogRequestResponse> personalLogSubscriber;
    private Subscriber<ShopWithdrawLogRequestResponse> shopLogSubscriber;

    void getPersonalWithdrawLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization, final RequestListener listener) {
        personalLogSubscriber = new Subscriber<PersonalWithdrawLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(PersonalWithdrawLogRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().getPersonalWithdrawLog(action, submitCode, custCode, pageIndex, pageSize, lastsequence, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(personalLogSubscriber);
    }

    void getShopWithdrawLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization, final RequestListener listener) {
        shopLogSubscriber = new Subscriber<ShopWithdrawLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopWithdrawLogRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().getShopWithdrawLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(shopLogSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != personalLogSubscriber) {
            personalLogSubscriber.unsubscribe();
        }
        if (null != shopLogSubscriber) {
            shopLogSubscriber.unsubscribe();
        }
    }
}
