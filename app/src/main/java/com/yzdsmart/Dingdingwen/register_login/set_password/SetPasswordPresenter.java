package com.yzdsmart.Dingdingwen.register_login.set_password;

import android.content.Context;
import android.preference.PreferenceManager;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.LoginRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

/**
 * Created by YZD on 2016/8/26.
 */
public class SetPasswordPresenter implements SetPasswordContract.SetPasswordPresenter {
    private Context context;
    private SetPasswordContract.SetPasswordView mView;
    private SetPasswordModel mModel;

    public SetPasswordPresenter(Context context, SetPasswordContract.SetPasswordView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new SetPasswordModel();
        mView.setPresenter(this);
    }

    @Override
    public void setPassword(String actioncode, String userName, String password, String regCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.setting));
        mModel.setPassword(actioncode, userName, password, regCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onSetPassword(true, response.getErrorInfo());
                } else if ("FAIL".equals(response.getActionStatus())) {
                    mView.onSetPassword(false, response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void userLogin(String userName, String password, String loginCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loginning));
        mModel.userLogin(userName, password, loginCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                LoginRequestResponse response = (LoginRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    SharedPreferencesUtils.clear(context, PreferenceManager.getDefaultSharedPreferences(context));
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
