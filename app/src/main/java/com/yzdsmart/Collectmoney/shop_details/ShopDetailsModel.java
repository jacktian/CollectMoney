package com.yzdsmart.Collectmoney.shop_details;

import com.yzdsmart.Collectmoney.bean.ShopScanner;
import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/26.
 */
public class ShopDetailsModel {
    //网络请求监听
    private Subscriber<ShopInfoRequestResponse> getShopInfoSubscriber;
    private Subscriber<RequestResponse> setFollowSubscriber;
    private Subscriber<List<ShopScanner>> getShopFollowersSubscribe;

    void getShopInfo(String actioncode, String submitCode, String bazaCode, String custCode, final RequestListener listener) {
        getShopInfoSubscriber = new Subscriber<ShopInfoRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(ShopInfoRequestResponse shopInfo) {
                listener.onSuccess(shopInfo);
            }
        };
        RequestAdapter.getRequestService().getShopInfo(actioncode, submitCode, bazaCode, custCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopInfoSubscriber);
    }

    void setFollow(String action, String submitCode, String custCode, String bazaCode, final RequestListener listener) {
        setFollowSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().setFollow(action, submitCode, custCode, bazaCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(setFollowSubscriber);
    }

    void getShopFollowers(String action, String submitCode, String bazaCode, String custCode, Integer pageIndex, Integer pageSize, final RequestListener listener) {
        getShopFollowersSubscribe = new Subscriber<List<ShopScanner>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(List<ShopScanner> shopScanners) {
                listener.onSuccess(shopScanners);
            }
        };
        RequestAdapter.getRequestService().getShopFollowers(action, submitCode, bazaCode, custCode, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopFollowersSubscribe);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getShopInfoSubscriber) {
            getShopInfoSubscriber.unsubscribe();
        }
        if (null != setFollowSubscriber) {
            setFollowSubscriber.unsubscribe();
        }
        if (null != getShopFollowersSubscribe) {
            getShopFollowersSubscribe.unsubscribe();
        }
    }
}
