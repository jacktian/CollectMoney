package com.yzdsmart.Dingdingwen.crop;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.UploadFileRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/24.
 */
public class ImageCropModel {
    //获取验证码观察者，与被观察者绑定进行监听
    private Subscriber<UploadFileRequestResponse> uploadSubscriber;

    void uploadPortrait(String action, String fileName, String fileData, String tcAccount, String authorization, final RequestListener listener) {
        uploadSubscriber = new Subscriber<UploadFileRequestResponse>() {
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
        RequestAdapter.getDDWRequestService().uploadPortrait(action, fileName, fileData, tcAccount, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(uploadSubscriber);
    }

    void unRegisterSubscribe() {
        if (null != uploadSubscriber) {
            //解除引用关系，以避免内存泄露的发生
            uploadSubscriber.unsubscribe();
        }
    }
}
