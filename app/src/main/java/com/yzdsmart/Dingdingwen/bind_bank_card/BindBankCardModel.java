package com.yzdsmart.Dingdingwen.bind_bank_card;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ValidateBankCardRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/12/14.
 */

public class BindBankCardModel {
    //网络请求监听
    private Subscriber<ValidateBankCardRequestResponse> validateBankCardSubscriber;
    private Subscriber<RequestResponse> bindBankCardSubscriber;

    void validateBankCard(String submitCode, String bankCardNum, String authorization, final RequestListener listener) {
        validateBankCardSubscriber = new Subscriber<ValidateBankCardRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ValidateBankCardRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().validateBankCard(submitCode, bankCardNum, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(validateBankCardSubscriber);
    }

    void bindBankCard(String submitCode, String custCode, String bankCode, String bankCardNum, String custName, String authorization, final RequestListener listener) {
        bindBankCardSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getDDWRequestService().bindBankCard(submitCode, custCode, bankCode, bankCardNum, custName, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(bindBankCardSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != bindBankCardSubscriber) {
            bindBankCardSubscriber.unsubscribe();
        }
        if (null != validateBankCardSubscriber) {
            validateBankCardSubscriber.unsubscribe();
        }
    }
}
