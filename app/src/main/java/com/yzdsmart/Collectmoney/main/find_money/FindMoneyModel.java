package com.yzdsmart.Collectmoney.main.find_money;

import com.yzdsmart.Collectmoney.bean.map.POIContent;
import com.yzdsmart.Collectmoney.http.map.BaiduRequestAdapter;
import com.yzdsmart.Collectmoney.listener.HttpRequestListener;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/23.
 */
public class FindMoneyModel {
    //获取周边标注信息观察者，与被观察者绑定进行监听
    private Subscriber<POIContent> findMoneySubscriber;

    public void findMoney(String ak, Integer geo_table_id, String keyWord, String qLocation, Integer radius, Integer pageSize, Integer pageIndex, String m_code, String filter, final HttpRequestListener listener) {
        findMoneySubscriber = new Subscriber<POIContent>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onNext(POIContent poiContent) {
                listener.onSuccess(poiContent);
            }
        };
        BaiduRequestAdapter.getRequestService()
                .findMoney(ak, geo_table_id, keyWord, qLocation, radius, pageSize, pageIndex, m_code, filter)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(findMoneySubscriber);
    }

    public void unRegisterSubscribe() {
        if (null != findMoneySubscriber) {
            //解除引用关系，以避免内存泄露的发生
            findMoneySubscriber.unsubscribe();
        }
    }
}
