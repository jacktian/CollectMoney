package com.yzdsmart.Collectmoney.login;

import android.content.Context;

import com.tencent.TIMLogLevel;
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.LoginRequestResponse;
import com.yzdsmart.Collectmoney.tecent_im.business.InitBusiness;
import com.yzdsmart.Collectmoney.tecent_im.service.TlsBusiness;
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
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loginning));
        mModel.userLogin(userName, password, loginCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                LoginRequestResponse response = (LoginRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    SharedPreferencesUtils.setString(context, "baza_code", response.getBazaCode());
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
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
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
