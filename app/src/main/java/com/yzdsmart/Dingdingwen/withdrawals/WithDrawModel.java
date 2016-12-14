package com.yzdsmart.Dingdingwen.withdrawals;

import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.WithdrawRequestResponse;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    private Subscriber<List<BankCard>> getBankCardListSubscriber;

    void getCustInfo(String submitcode, String custCode, String authorization, final RequestListener listener) {
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
        RequestAdapter.getDDWRequestService().getCustInfo(submitcode, custCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustInfoSubscriber);
    }

    void getLeftCoins(String action, String submitCode, String bazaCode, String authorization, final RequestListener listener) {
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
        RequestAdapter.getDDWRequestService().getLeftCoins(action, submitCode, bazaCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(leftCoinsSubscriber);
    }

    void shopWithdrawCoins(String action, String shopWithdrawPara, String authorization, final RequestListener listener) {
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), shopWithdrawPara);
        RequestAdapter.getDDWRequestService().shopWithdrawCoins(action, body, "application/json", "application/json", authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(shopWithdrawSubscriber);
    }

    void personalWithdrawCoins(String action, String actiontype, String personalWithdrawPara, String authorization, final RequestListener listener) {
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), personalWithdrawPara);
        RequestAdapter.getDDWRequestService().personalWithdrawCoins(action, actiontype,  body, "application/json", "application/json",  authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(personalWithdrawSubscriber);
    }

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
        if (null != getBankCardListSubscriber) {
            getBankCardListSubscriber.unsubscribe();
        }
    }
}
