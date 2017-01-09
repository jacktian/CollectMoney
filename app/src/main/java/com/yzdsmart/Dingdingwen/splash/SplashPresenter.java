package com.yzdsmart.Dingdingwen.splash;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
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
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.init_app));
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
                ((BaseActivity) context).hideProgressDialog();
                mView.onAppRegister(false);
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
