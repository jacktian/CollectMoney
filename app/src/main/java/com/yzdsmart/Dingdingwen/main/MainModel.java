package com.yzdsmart.Dingdingwen.main;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.BackgroundBagRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetTokenRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PersonRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopListRequestResponse;
import com.yzdsmart.Dingdingwen.tecent_im.service.TLSService;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tencent.tls.platform.TLSPwdLoginListener;

/**
 * Created by YZD on 2016/8/30.
 */
public class MainModel {
    //获取周边标注信息观察者，与被观察者绑定进行监听
    private Subscriber<RequestResponse> appRegisterSubscriber;
    private Subscriber<GetTokenRequestResponse> getRefreshTokenSubscriber;
    private Subscriber<GetTokenRequestResponse> refreshAccessTokenSubscriber;
    private Subscriber<BackgroundBagRequestResponse> personalBackgroundBagSubscriber;
    private Subscriber<BackgroundBagRequestResponse> shopBackgroundBagSubscriber;
    private Subscriber<List<ShopListRequestResponse>> getShopListSubscriber;
    private Subscriber<RequestResponse> uploadCoorSubscriber;
    private Subscriber<PersonRequestResponse> getPersonBearbySubscriber;
    private TLSService tlsService;

    public MainModel(TLSService tlsService) {
        this.tlsService = tlsService;
    }

    /**
     * TLS账号登陆
     *
     * @param userName
     * @param password
     * @param listener
     */
    void chatLogin(String userName, String password, TLSPwdLoginListener listener) {
        tlsService.TLSPwdLogin(userName, password, listener);
    }

    void appRegister(String appCode, String appId, String appSecret, final RequestListener listener) {
        appRegisterSubscriber = new Subscriber<RequestResponse>() {
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
        RequestAdapter.getDDWRequestService().appRegister(appCode, appId, appSecret)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(appRegisterSubscriber);
    }

    void getRefreshToken(String grantType, String userName, String password, final RequestListener listener) {
        getRefreshTokenSubscriber = new Subscriber<GetTokenRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GetTokenRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getRefreshToken(grantType, userName, password)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getRefreshTokenSubscriber);
    }

    void refreshAccessToken(String grantType, String refreshToken, final RequestListener listener) {
        refreshAccessTokenSubscriber = new Subscriber<GetTokenRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GetTokenRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().refreshAccessToken(grantType, refreshToken)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(refreshAccessTokenSubscriber);
    }

    void personalBackgroundBag(String action, String actiontype, String submitCode, String custCode, String authorization, final RequestListener listener) {
        personalBackgroundBagSubscriber = new Subscriber<BackgroundBagRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(BackgroundBagRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().personalBackgroundBag(action, actiontype, submitCode, custCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(personalBackgroundBagSubscriber);
    }

    void shopBackgroundBag(String action, String submitCode, String bazaCode, String authorization, final RequestListener listener) {
        shopBackgroundBagSubscriber = new Subscriber<BackgroundBagRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(BackgroundBagRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().shopBackgroundBag(action, submitCode, bazaCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(shopBackgroundBagSubscriber);
    }

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
        if (null != appRegisterSubscriber) {
            appRegisterSubscriber.unsubscribe();
        }
        if (null != getRefreshTokenSubscriber) {
            getRefreshTokenSubscriber.unsubscribe();
        }
        if (null != refreshAccessTokenSubscriber) {
            refreshAccessTokenSubscriber.unsubscribe();
        }
        if (null != personalBackgroundBagSubscriber) {
            personalBackgroundBagSubscriber.unsubscribe();
        }
        if (null != shopBackgroundBagSubscriber) {
            shopBackgroundBagSubscriber.unsubscribe();
        }
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
