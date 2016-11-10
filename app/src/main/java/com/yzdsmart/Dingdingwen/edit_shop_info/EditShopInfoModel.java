package com.yzdsmart.Dingdingwen.edit_shop_info;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoByPersRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/25.
 */

public class EditShopInfoModel {
    //网络请求监听
    private Subscriber<ShopInfoByPersRequestResponse> getShopInfoSubscriber;
    private Subscriber<RequestResponse> setShopInfoSubscriber;

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
        RequestAdapter.getRequestService().getShopInfoByPers(actioncode, submitCode, bazaCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopInfoSubscriber);
    }

    void setShopInfos(String submitCode, String bazaCode, String bazaName, String bazaPers, String bazaTel, String bazaAddr, String remark, String coor, String authorization, final RequestListener listener) {
        setShopInfoSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().setShopInfos(submitCode, bazaCode, bazaName, bazaPers, bazaTel, bazaAddr, remark, coor, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(setShopInfoSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getShopInfoSubscriber) {
            getShopInfoSubscriber.unsubscribe();
        }
        if (null != setShopInfoSubscriber) {
            setShopInfoSubscriber.unsubscribe();
        }
    }
}
