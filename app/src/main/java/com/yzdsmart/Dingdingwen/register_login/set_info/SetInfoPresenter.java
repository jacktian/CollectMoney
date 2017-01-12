package com.yzdsmart.Dingdingwen.register_login.set_info;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.LoginRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;

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
    public void userRegister(String actioncode, String userName, String password, String cSex, Integer cAge, String cNickName, final String regCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.registering));
        mModel.userRegister(actioncode, userName, password, cSex, cAge, cNickName, regCode, authorization, new RequestListener() {
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
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_user_register));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {

            }
        });
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
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
