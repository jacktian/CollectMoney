package com.yzdsmart.Collectmoney.publish_tasks;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.PublishTaskLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

import java.util.Observable;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/4.
 */
public class PublishTasksModel {
    //网络请求监听
    private Subscriber<RequestResponse> publishTaskSubscriber;
    private Subscriber<PublishTaskLogRequestResponse> publishTaskLogSubscriber;

    void publishTask(String submitCode, String bazaCode, Integer totalGold, Integer sMinGold, Integer sMaxGold, String beginTime, String endTime, final RequestListener listener) {
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
        RequestAdapter.getRequestService().publishTasks(submitCode, bazaCode, totalGold, sMinGold, sMaxGold, beginTime, endTime)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(publishTaskSubscriber);
    }

    void publishTaskLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, final RequestListener listener) {
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
        RequestAdapter.getRequestService().publishTaskLog(action, submitCode, bazaCode, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(publishTaskLogSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != publishTaskSubscriber) {
            publishTaskSubscriber.unsubscribe();
        }
        if (null != publishTaskLogSubscriber) {
            publishTaskLogSubscriber.unsubscribe();
        }
    }
}
