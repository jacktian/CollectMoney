package com.yzdsmart.Dingdingwen.publish_tasks;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/4.
 */
public class PublishTasksModel {
    //网络请求监听
    private Subscriber<RequestResponse> publishTaskSubscriber;
    private Subscriber<GetCoinRequestResponse> leftCoinsSubscriber;

    void publishTask(String submitCode, String bazaCode, Integer totalGold, Integer totalNum, Integer goldType, String beginTime, String endTime, String authorization, final RequestListener listener) {
        publishTaskSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getDDWRequestService().publishTasks(submitCode, bazaCode, totalGold, totalNum, goldType, beginTime, endTime, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(publishTaskSubscriber);
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

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != publishTaskSubscriber) {
            publishTaskSubscriber.unsubscribe();
        }
        if (null != leftCoinsSubscriber) {
            leftCoinsSubscriber.unsubscribe();
        }
    }
}
