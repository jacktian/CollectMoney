package com.yzdsmart.Dingdingwen.settings;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/3.
 */
public class SettingsModel {
    //网络请求监听
    private Subscriber<CustDetailInfoRequestResponse> getCustDetailSubscriber;

    void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode, String authorization, final RequestListener listener) {
        getCustDetailSubscriber = new Subscriber<CustDetailInfoRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(CustDetailInfoRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getCustDetailInfo(actioncode, submitCode, custCode, selfCustCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustDetailSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getCustDetailSubscriber) {
            getCustDetailSubscriber.unsubscribe();
        }
    }
}
