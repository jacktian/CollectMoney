package com.yzdsmart.Dingdingwen.register_login.verify_code;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getVerifyCode(String telNum, String currDate, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getVerifyCode(telNum, currDate, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onGetVerifyCode(true, null);
                } else if ("FAIL".equals(response.getActionStatus())) {
                    mView.onGetVerifyCode(false, response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_verify_code));
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
    public void validateVerifyCode(String actioncode, String telNum, String verifyCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.validating));
        mModel.validateVerifyCode(actioncode, telNum, verifyCode, authorization, new RequestListener() {
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
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_validate_verify_code));
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
    public void thirdPlatformRegister(String actioncode, String userName, String password, String cSex, Integer cAge, String cNickName, String otherElec, String platformExportData, String regCode, String authorization) {
        mModel.thirdPlatformRegister(actioncode, userName, password, cSex, cAge, cNickName, otherElec, platformExportData, regCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String err) {

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
