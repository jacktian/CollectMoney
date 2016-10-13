package com.yzdsmart.Collectmoney.register_login.set_info;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.LoginRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

/**
 * Created by YZD on 2016/9/23.
 */

public class SetInfoPresenter implements SetInfoContract.SetInfoPresenter {
    private Context context;
    private SetInfoContract.SetInfoView mView;
    private SetInfoModel mModel;

    public SetInfoPresenter(Context context, SetInfoContract.SetInfoView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new SetInfoModel();
        mView.setPresenter(this);
    }

    @Override
    public void userRegister(String actioncode, String userName, String password, String cSex, Integer cAge, String cNickName, final String regCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.registering));
        mModel.userRegister(actioncode, userName, password, cSex, cAge, cNickName, regCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onUserRegister();
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
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
