package com.yzdsmart.Collectmoney.main.personal;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustLevelRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/27.
 */
public class PersonalModel {
    //网络请求监听
    private Subscriber<CustLevelRequestResponse> getCustLevelSubscriber;
    private Subscriber<CustInfoRequestResponse> getCustInfoSubscriber;
    private Subscriber<ShopInfoRequestResponse> getShopInfoSubscriber;
    private Subscriber<GetGalleyRequestResponse> getShopGalleySubscriber;

    /**
     * 获取用户等级和星级
     *
     * @param custcode
     * @param submitcode
     */
    void getCustLevel(String custcode, String submitcode, final RequestListener listener) {
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
        RequestAdapter.getRequestService().getCustLevel(custcode, submitcode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustLevelSubscriber);
    }

    void getCustInfo(String submitcode, String custCode, final RequestListener listener) {
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
        RequestAdapter.getRequestService().getCustInfo(submitcode, custCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustInfoSubscriber);
    }

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
    }
}
