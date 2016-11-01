package com.yzdsmart.Dingdingwen.publish_tasks_log;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PublishTaskLogRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/18.
 */
public class PublishTasksLogModel {
    //网络请求监听
    private Subscriber<PublishTaskLogRequestResponse> publishTaskLogSubscriber;
    private Subscriber<GetCoinRequestResponse> leftCoinsSubscriber;

    void publishTaskLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, final RequestListener listener) {
        publishTaskLogSubscriber = new Subscriber<PublishTaskLogRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(PublishTaskLogRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().publishTaskLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(publishTaskLogSubscriber);
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

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != publishTaskLogSubscriber) {
            publishTaskLogSubscriber.unsubscribe();
        }
        if (null != leftCoinsSubscriber) {
            leftCoinsSubscriber.unsubscribe();
        }
    }
}
