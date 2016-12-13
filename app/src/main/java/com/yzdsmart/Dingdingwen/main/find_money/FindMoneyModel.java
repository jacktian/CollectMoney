package com.yzdsmart.Dingdingwen.main.find_money;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.PersonRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopListRequestResponse;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/23.
 */
public class FindMoneyModel {
    //获取周边标注信息观察者，与被观察者绑定进行监听
    private Subscriber<List<ShopListRequestResponse>> getShopListSubscriber;
    private Subscriber<RequestResponse> uploadCoorSubscriber;
    private Subscriber<PersonRequestResponse> getPersonBearbySubscriber;

    void getShopList(String submitCode, String coor, Integer range, Integer pageIndex, Integer pageSize, String authorization, final RequestListener listener) {
        getShopListSubscriber = new Subscriber<List<ShopListRequestResponse>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(List<ShopListRequestResponse> shops) {
                listener.onSuccess(shops);
            }
        };
        RequestAdapter.getDDWRequestService().getShopList(submitCode, coor, range, pageIndex, pageSize, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopListSubscriber);
    }

    void uploadCoor(String submitCode, String custCode, String coor, String authorization, final RequestListener listener) {
        uploadCoorSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getDDWRequestService().uploadCoor(submitCode, custCode, coor, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(uploadCoorSubscriber);
    }

    void getPersonNearby(String submitCode, String custCode, String coor, Integer pageIndex, Integer pageSize, String authorization, final RequestListener listener) {
        getPersonBearbySubscriber = new Subscriber<PersonRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(PersonRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getDDWRequestService().getPersonNearby(submitCode, custCode, coor, pageIndex, pageSize, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getPersonBearbySubscriber);
    }

    public void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getShopListSubscriber) {
            getShopListSubscriber.unsubscribe();
        }
        if (null != uploadCoorSubscriber) {
            uploadCoorSubscriber.unsubscribe();
        }
        if (null != getPersonBearbySubscriber) {
            getPersonBearbySubscriber.unsubscribe();
        }
    }
}
