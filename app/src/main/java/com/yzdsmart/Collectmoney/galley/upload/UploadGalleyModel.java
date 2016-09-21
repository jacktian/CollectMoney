package com.yzdsmart.Collectmoney.galley.upload;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.UploadFileRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jacks on 2016/9/17.
 */
public class UploadGalleyModel {
    //获取验证码观察者，与被观察者绑定进行监听
    private Subscriber<UploadFileRequestResponse> uploadGalleySubscriber;
    private Subscriber<UploadFileRequestResponse> uploadShopImageSubscriber;

    void uploadGalley(String action, String fileName, String fileData, String custCode, final RequestListener listener) {
        uploadGalleySubscriber = new Subscriber<UploadFileRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(UploadFileRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().uploadGalley(action, fileName, fileData, custCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(uploadGalleySubscriber);
    }

    void uploadShopImage(String action, String fileName, String fileData, String bazaCode, final RequestListener listener) {
        uploadShopImageSubscriber = new Subscriber<UploadFileRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(UploadFileRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().uploadShopImage(action, fileName, fileData, bazaCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(uploadShopImageSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != uploadGalleySubscriber) {
            uploadGalleySubscriber.unsubscribe();
        }
        if (null != uploadShopImageSubscriber) {
            uploadShopImageSubscriber.unsubscribe();
        }
    }
}
