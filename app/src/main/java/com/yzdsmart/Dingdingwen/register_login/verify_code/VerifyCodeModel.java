package com.yzdsmart.Dingdingwen.register_login.verify_code;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.LoginRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyCodeModel {
    //网络请求监听
    private Subscriber<RequestResponse> getVerifyCodeSubscriber;
    private Subscriber<RequestResponse> verifyVerifyCodeSubscriber;
    private Subscriber<LoginRequestResponse> thirdPlatformRegisterSubscriber;

    /**
     * 获取短信验证码
     *
     * @param telNum
     * @param listener
     */
    void getVerifyCode(String telNum, String currDate, String authorization, final RequestListener listener) {
        getVerifyCodeSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().getVerifyCode(telNum, currDate, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getVerifyCodeSubscriber);
    }

    void validateVerifyCode(String actioncode, String telNum, String verifyCode, String authorization, final RequestListener listener) {
        verifyVerifyCodeSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getRequestService().validateVerifyCode(actioncode, telNum, verifyCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(verifyVerifyCodeSubscriber);
    }


    void thirdPlatformRegister(String actioncode, String userName, String password, String cSex, Integer cAge, String cNickName, String otherElec, String platformExportData, String regCode, String authorization, final RequestListener listener) {
        thirdPlatformRegisterSubscriber = new Subscriber<LoginRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(LoginRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        String str = "{ \"UserName\": \"" + userName + "\",\"Password\": \"" + password + "\",\"CSex\": \"" + cSex + "\",\"CAge\": " + cAge + ",\"CNickName\":\"" + cNickName + "\",\"OtherElec\":\"" + otherElec + "\" ,\"ElecInfo\":" + platformExportData + ",\"RegCode\": \"" + regCode + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), str);
        RequestAdapter.getRequestService().thirdPlatformRegister(actioncode, "application/json", "application/json", body, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(thirdPlatformRegisterSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getVerifyCodeSubscriber) {
            getVerifyCodeSubscriber.unsubscribe();
        }
        if (null != verifyVerifyCodeSubscriber) {
            verifyVerifyCodeSubscriber.unsubscribe();
        }
        if (null != thirdPlatformRegisterSubscriber) {
            thirdPlatformRegisterSubscriber.unsubscribe();
        }
    }
}
