package com.yzdsmart.Dingdingwen.splash;

import android.content.Context;

import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;

/**
 * Created by YZD on 2016/11/14.
 */

public class SplashPresenter implements SplashContract.SplashPresenter {
    private Context context;
    private SplashContract.SplashView mView;
    private SplashModel mModel;

    public SplashPresenter(Context context, SplashContract.SplashView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new SplashModel();
        mView.setPresenter(this);
    }

    @Override
    public void appRegister(String appCode, String appId, String appSecret) {
        mModel.appRegister(appCode, appId, appSecret, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onAppRegister(true);
                } else {
                    mView.onAppRegister(false);
                }
            }

            @Override
            public void onError(String err) {
                mView.onAppRegister(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
