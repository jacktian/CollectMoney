package com.yzdsmart.Collectmoney.register_forget_password.verify_code;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyCodePresenter implements VerifyCodeContract.VerifyCodePresenter {
    private Context context;
    private VerifyCodeContract.VerifyCodeView mView;
    private VerifyCodeModel mModel;

    public VerifyCodePresenter(Context context, VerifyCodeContract.VerifyCodeView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new VerifyCodeModel();
        mView.setPresenter(this);
    }

    @Override
    public void getVerifyCode(String telNum, String currDate) {
        mModel.getVerifyCode(telNum, currDate, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("FAIL".equals(response.getActionStatus())) {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void validateVerifyCode(String actioncode, String telNum, String verifyCode) {
        mModel.validateVerifyCode(actioncode, telNum, verifyCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onValidateVerifyCode(true, response.getErrorInfo());
                } else if ("FAIL".equals(response.getActionStatus())) {
                    mView.onValidateVerifyCode(false, response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar(err);
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
