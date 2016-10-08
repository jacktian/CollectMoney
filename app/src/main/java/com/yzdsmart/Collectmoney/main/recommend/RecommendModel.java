package com.yzdsmart.Collectmoney.main.recommend;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.ExpandListRequestResponse;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/28.
 */
public class RecommendModel {
    //网络请求监听
//    private Subscriber<List<ExpandListRequestResponse>> getExpandListSubscriber;
//
//    void getExpandList(String submitCode, Integer pageIndex, Integer pageSize, final RequestListener listener) {
//        getExpandListSubscriber = new Subscriber<List<ExpandListRequestResponse>>() {
//            @Override
//            public void onCompleted() {
//                listener.onComplete();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                listener.onError(e.getMessage());
//            }
//
//            @Override
//            public void onNext(List<ExpandListRequestResponse> expands) {
//                listener.onSuccess(expands);
//            }
//        };
//        RequestAdapter.getRequestService().getExpandList(submitCode, pageIndex, pageSize)
//                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
//                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
//                .subscribe(getExpandListSubscriber);
//    }

    private Subscriber<ResponseBody> getExpandListSubscriber;

    void getExpandList(String submitCode, Integer pageIndex, Integer pageSize, final RequestListener listener) {
        getExpandListSubscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                listener.onSuccess(responseBody);
            }
        };
        RequestAdapter.getRequestService().getExpandList(submitCode, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getExpandListSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getExpandListSubscriber) {
            getExpandListSubscriber.unsubscribe();
        }
    }
}
