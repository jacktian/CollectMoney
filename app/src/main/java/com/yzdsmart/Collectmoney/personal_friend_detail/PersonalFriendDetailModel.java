package com.yzdsmart.Collectmoney.personal_friend_detail;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustLevelRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetGalleyRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/29.
 */
public class PersonalFriendDetailModel {
    //网络请求监听
    private Subscriber<CustLevelRequestResponse> getCustLevelSubscriber;
    private Subscriber<CustInfoRequestResponse> getCustInfoSubscriber;
    private Subscriber<GetGalleyRequestResponse> getGalleySubscriber;

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

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getCustLevelSubscriber) {
            getCustLevelSubscriber.unsubscribe();
        }
        if (null != getCustInfoSubscriber) {
            getCustInfoSubscriber.unsubscribe();
        }
        if (null != getGalleySubscriber) {
            getGalleySubscriber.unsubscribe();
        }
    }
}
