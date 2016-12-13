package com.yzdsmart.Dingdingwen.personal;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustLevelRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoByPersRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.UploadFileRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/10/25.
 */

public class PersonalModel {
    //网络请求监听
    private Subscriber<CustLevelRequestResponse> getCustLevelSubscriber;
    private Subscriber<CustInfoRequestResponse> getCustInfoSubscriber;
    private Subscriber<ShopInfoByPersRequestResponse> getShopInfoSubscriber;
    private Subscriber<GetGalleyRequestResponse> getShopGalleySubscriber;
    private Subscriber<UploadFileRequestResponse> uploadShopAvaterSubscriber;

    /**
     * 获取用户等级和星级
     *
     * @param code
     * @param submitcode
     */
    void getCustLevel(String code, String submitcode, String action, String authorization, final RequestListener listener) {
        getCustLevelSubscriber = new Subscriber<CustLevelRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(CustLevelRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getCustLevel(code, submitcode, action, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustLevelSubscriber);
    }

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

    void getShopInfo(String actioncode, String submitCode, String bazaCode, String authorization, final RequestListener listener) {
        getShopInfoSubscriber = new Subscriber<ShopInfoByPersRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopInfoByPersRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getShopInfoByPers(actioncode, submitCode, bazaCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopInfoSubscriber);
    }

    void getShopGalley(String action, String submitCode, String bazaCode, String authorization, final RequestListener listener) {
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
        RequestAdapter.getDDWRequestService().getShopGalley(action, submitCode, bazaCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopGalleySubscriber);
    }

    void uploadShopAvater(String action, String fileName, String fileData, String bazaCode, String authorization, final RequestListener listener) {
        uploadShopAvaterSubscriber = new Subscriber<UploadFileRequestResponse>() {
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
        RequestAdapter.getDDWRequestService().uploadShopImage(action, fileName, fileData, bazaCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(uploadShopAvaterSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getCustLevelSubscriber) {
            getCustLevelSubscriber.unsubscribe();
        }
        if (null != getCustInfoSubscriber) {
            getCustInfoSubscriber.unsubscribe();
        }
        if (null != getShopInfoSubscriber) {
            getShopInfoSubscriber.unsubscribe();
        }
        if (null != getShopGalleySubscriber) {
            getShopGalleySubscriber.unsubscribe();
        }
        if (null != uploadShopAvaterSubscriber) {
            uploadShopAvaterSubscriber.unsubscribe();
        }
    }
}
