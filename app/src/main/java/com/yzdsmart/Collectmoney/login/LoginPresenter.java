package com.yzdsmart.Collectmoney.login;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.LoginRequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

/**
 * Created by YZD on 2016/8/26.
 */
public class LoginPresenter implements LoginContract.LoginPresenter {
    private Context context;
    private LoginContract.LoginView mView;
    private LoginModel mModel;

    public LoginPresenter(Context context, LoginContract.LoginView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new LoginModel();
        mView.setPresenter(this);
    }

    @Override
    public void userLogin(String userName, String password, String loginCode) {
        mModel.userLogin(userName, password, loginCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                LoginRequestResponse response = (LoginRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    SharedPreferencesUtils.setString(context, "cust_code", response.getCustCode());
                    SharedPreferencesUtils.setString(context, "im_account", response.getTCInfo().getTCAccount());
                    SharedPreferencesUtils.setString(context, "im_password", response.getTCInfo().getTCPassword());
                    mView.onUserLogin(true, response.getErrorInfo());
                } else if ("FAIL".equals(response.getActionStatus())) {
                    mView.onUserLogin(false, response.getErrorInfo());
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
