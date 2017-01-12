package com.yzdsmart.Dingdingwen.register_login.login;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.LoginRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;

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
    public void userLogin(String userName, String password, String loginCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loginning));
        mModel.userLogin(userName, password, loginCode, authorization, new RequestListener() {
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
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_user_login));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void thirdPlatformLogin(String userName, String otherElec, String loginCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loginning));
        mModel.thirdPlatformLogin(userName, otherElec, loginCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                LoginRequestResponse requestResponse = (LoginRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    SharedPreferencesUtils.setString(context, "baza_code", requestResponse.getBazaCode());
                    SharedPreferencesUtils.setString(context, "cust_code", requestResponse.getCustCode());
                    SharedPreferencesUtils.setString(context, "im_account", requestResponse.getTCInfo().getTCAccount());
                    SharedPreferencesUtils.setString(context, "im_password", requestResponse.getTCInfo().getTCPassword());
                    mView.onThirdPlatformLoginSuccess(requestResponse);
                } else if ("FAIL".equals(requestResponse.getActionStatus())) {
                    if ("1199".equals("" + requestResponse.getErrorCode())) {
                        mView.onThirdPlatformNotBindPhone();
                    } else {
                        ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                    }
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_user_login));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
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
