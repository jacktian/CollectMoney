package com.yzdsmart.Collectmoney.edit_personal_info;

import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/24.
 */

public class EditPersonalInfoModel {
    //网络请求监听
    private Subscriber<CustInfoRequestResponse> getCustInfoSubscriber;
    private Subscriber<CustDetailInfoRequestResponse> getCustDetailSubscriber;
    private Subscriber<RequestResponse> setCustDetailSubscriber;

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

    void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode, final RequestListener listener) {
        getCustDetailSubscriber = new Subscriber<CustDetailInfoRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(CustDetailInfoRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().getCustDetailInfo(actioncode, submitCode, custCode, selfCustCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustDetailSubscriber);
    }

    void setCustDetailInfo(String submitCode, String custCode, String cName, String cNickName, String cSex, String cBirthday, String cTel, String cIdNo, String cNation, Double cHeight, Double cWeight, String cProfession, String cAddress, String cProv, String cCity, String cDist, String cCountry, String cRemark, final RequestListener listener) {
        setCustDetailSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().setCustDetailInfo(submitCode, custCode, cName, cNickName, cSex, cBirthday, cTel, cIdNo, cNation, cHeight, cWeight, cProfession, cAddress, cProv, cCity, cDist, cCountry, cRemark)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(setCustDetailSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getCustInfoSubscriber) {
            getCustInfoSubscriber.unsubscribe();
        }
        if (null != getCustDetailSubscriber) {
            getCustDetailSubscriber.unsubscribe();
        }
        if (null != setCustDetailSubscriber) {
            setCustDetailSubscriber.unsubscribe();
        }
    }
}
