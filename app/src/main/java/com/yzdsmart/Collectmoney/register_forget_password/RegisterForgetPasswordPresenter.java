package com.yzdsmart.Collectmoney.register_forget_password;

import android.content.Context;

import com.yzdsmart.Collectmoney.http.RequestListener;

/**
 * Created by YZD on 2016/8/25.
 */
public class RegisterForgetPasswordPresenter implements RegisterForgetPasswordContract.RegisterForgetPasswordPresenter {
    private Context context;
    private RegisterForgetPasswordContract.RegisterForgetPasswordView mView;
    private RegisterForgetPasswordModel mModel;

    public RegisterForgetPasswordPresenter(Context context, RegisterForgetPasswordContract.RegisterForgetPasswordView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new RegisterForgetPasswordModel();
        mView.setPresenter(this);
    }

    @Override
    public void isUserExist(String telNum) {
        mModel.isUserExist(telNum, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(Object err) {

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
