package com.yzdsmart.Dingdingwen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.response.GetTokenRequestResponse;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/11/9.
 */

public class RefreshAccessTokenService extends Service {
    private boolean stop = false;
    private RefreshAccessTokenBinder mBinder = new RefreshAccessTokenBinder();
    //获取周边标注信息观察者，与被观察者绑定进行监听
    private Subscriber<GetTokenRequestResponse> refreshAccessTokenSubscriber;
    private Runnable refreshAccessTokenRunnable;
    private Handler mHandler = new Handler();

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class RefreshAccessTokenBinder extends Binder {
        public RefreshAccessTokenService getService() {
            return RefreshAccessTokenService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refreshAccessTokenRunnable = new Runnable() {
            @Override
            public void run() {
                RequestAdapter.getDDWRequestService().refreshAccessToken("refresh_token", SharedPreferencesUtils.getString(RefreshAccessTokenService.this, "ddw_refresh_token", ""))
                        .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                        .observeOn(Schedulers.io())//回调到主线程
                        .subscribe(refreshAccessTokenSubscriber);
            }
        };
        refreshAccessTokenSubscriber = new Subscriber<GetTokenRequestResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GetTokenRequestResponse requestResponse) {

            }
        };
//        while (!stop) {
//            try {
//                mHandler.postDelayed(refreshAccessTokenRunnable, 10000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stop = true;
        //解除引用关系，以避免内存泄露的发生
        if (null != refreshAccessTokenSubscriber) {
            refreshAccessTokenSubscriber.unsubscribe();
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(refreshAccessTokenRunnable);
        super.onDestroy();
    }
}
