package com.yzdsmart.Collectmoney.register_forget_password;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.RequestResponse;
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
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onIsUserExist(true);
                } else if ("FAIL".equals(response.getActionStatus()) && 1004 == response.getErrorCode()) {
                    mView.onIsUserExist(false);
                } else {
                    ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.verify_tel_error));
                }
            }

            @Override
            public void onError(Object err) {
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.verify_tel_error));
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getVerifyCodeUsable(String telNum) {
        mModel.isUserExist(telNum, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onGetVerifyCodeUsable(true);
                } else if ("FAIL".equals(response.getActionStatus()) && 1004 == response.getErrorCode()) {
                    mView.onGetVerifyCodeUsable(false);
                } else {
                    ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.verify_tel_error));
                }
            }

            @Override
            public void onError(Object err) {
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.verify_tel_error));
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getVerifyCode(String telNum, String currDate) {
        mModel.getVerifyCode(telNum, currDate, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("FAIL".equals(response.getActionStatus())) {
                    ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.get_verify_code_error));
                }
            }

            @Override
            public void onError(Object err) {
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.get_verify_code_error));
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
