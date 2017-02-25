package com.yzdsmart.Dingdingwen.activity_details;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.SignDataRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2017/1/20.
 */

public class ActivityDetailsModel {
    //网络请求监听
    private Subscriber<SignDataRequestResponse> getSignActivityListSubscriber;

    void getSignActivityList(String action, String submitCode, String custCode, String activityCode, String authorization, final RequestListener listener) {
        getSignActivityListSubscriber = new Subscriber<SignDataRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(SignDataRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getSignActivityList(action, submitCode, custCode, activityCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getSignActivityListSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getSignActivityListSubscriber) {
            getSignActivityListSubscriber.unsubscribe();
        }
    }
}
