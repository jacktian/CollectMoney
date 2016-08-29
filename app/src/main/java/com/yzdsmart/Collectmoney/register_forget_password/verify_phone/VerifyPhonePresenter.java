package com.yzdsmart.Collectmoney.register_forget_password.verify_phone;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyPhonePresenter implements VerifyPhoneContract.VerifyPhonePresenter {
    private Context context;
    private VerifyPhoneContract.VerifyPhoneView mView;
    private VerifyPhoneModel mModel;

    public VerifyPhonePresenter(Context context, VerifyPhoneContract.VerifyPhoneView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new VerifyPhoneModel();
        mView.setPresenter(this);
    }

    @Override
    public void isUserExist(String telNum) {
        mModel.isUserExist(telNum, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onIsUserExist(true, response.getErrorInfo());
                } else if ("FAIL".equals(response.getActionStatus()) && 1004 == response.getErrorCode()) {
                    mView.onIsUserExist(false, response.getErrorInfo());
                } else {
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
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
