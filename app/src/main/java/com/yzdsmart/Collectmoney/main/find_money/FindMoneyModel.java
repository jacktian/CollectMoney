package com.yzdsmart.Collectmoney.main.find_money;

import com.yzdsmart.Collectmoney.bean.Shop;
import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/23.
 */
public class FindMoneyModel {
    //获取周边标注信息观察者，与被观察者绑定进行监听
    private Subscriber<List<Shop>> getShopListSubscriber;

    void getShopList(String submitCode, String coor, Integer pageIndex, Integer pageSize, final RequestListener listener) {
        getShopListSubscriber = new Subscriber<List<Shop>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(List<Shop> shops) {
                listener.onSuccess(shops);
            }
        };
        RequestAdapter.getRequestService().getShopList(submitCode, coor, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getShopListSubscriber);
    }

    public void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getShopListSubscriber) {
            getShopListSubscriber.unsubscribe();
        }
    }
}
