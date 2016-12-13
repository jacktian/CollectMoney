package com.yzdsmart.Dingdingwen.card_bag;

import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/12/13.
 */

public class CardBagModel {
    //网络请求监听
    private Subscriber<List<BankCard>> getBankCardListSubscriber;

    void getBankCardList(String submitCode, String custCode, String authorization, final RequestListener listener) {
        getBankCardListSubscriber = new Subscriber<List<BankCard>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(List<BankCard> bankCardList) {
                listener.onSuccess(bankCardList);
            }
        };
        RequestAdapter.getDDWRequestService().getBankCardList(submitCode, custCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getBankCardListSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getBankCardListSubscriber) {
            getBankCardListSubscriber.unsubscribe();
        }
    }
}
