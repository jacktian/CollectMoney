package com.yzdsmart.Dingdingwen.galley;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;
import com.yzdsmart.Collectmoney.http.response.UploadFileRequestResponse;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/22.
 */

public class GalleyModel {
    private Subscriber<GetGalleyRequestResponse> getGalleySubscriber;
    private Subscriber<RequestResponse> deletePersonalGalleySubscriber;
    private Subscriber<GetGalleyRequestResponse> getShopGalleySubscriber;
    private Subscriber<RequestResponse> deleteShopGalleySubscriber;
    private Subscriber<UploadFileRequestResponse> uploadGalleySubscriber;
    private Subscriber<UploadFileRequestResponse> uploadShopImageSubscriber;

    void getPersonalGalley(String action, String submitCode, String custCode, final RequestListener listener) {
        getGalleySubscriber = new Subscriber<GetGalleyRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GetGalleyRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().getPersonalGalley(action, submitCode, custCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getGalleySubscriber);
    }

    void deletePersonalGalley(String action, String submitCode, String custCode, List<Integer> fileIdList, final RequestListener listener) {
        deletePersonalGalleySubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().deletePersonalGalley(action, submitCode, custCode, fileIdList)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(deletePersonalGalleySubscriber);
    }

    void getShopGalley(String action, String submitCode, String bazaCode, final RequestListener listener) {
        getShopGalleySubscriber = new Subscriber<GetGalleyRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GetGalleyRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().getShopGalley(action, submitCode, bazaCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopGalleySubscriber);
    }

    void deleteShopGalley(String action, String submitCode, String bazaCode, List<Integer> fileIdList, final RequestListener listener) {
        deleteShopGalleySubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().deleteShopGalley(action, submitCode, bazaCode, fileIdList)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(deleteShopGalleySubscriber);
    }

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
        if (null != getGalleySubscriber) {
            getGalleySubscriber.unsubscribe();
        }
        if (null != deletePersonalGalleySubscriber) {
            deletePersonalGalleySubscriber.unsubscribe();
        }
        if (null != getShopGalleySubscriber) {
            getShopGalleySubscriber.unsubscribe();
        }
        if (null != deleteShopGalleySubscriber) {
            deleteShopGalleySubscriber.unsubscribe();
        }
        if (null != uploadGalleySubscriber) {
            uploadGalleySubscriber.unsubscribe();
        }
        if (null != uploadShopImageSubscriber) {
            uploadShopImageSubscriber.unsubscribe();
        }
    }
}
