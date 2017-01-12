package com.yzdsmart.Dingdingwen.register_login.verify_phone;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void isUserExist(String telNum, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.validating));
        mModel.isUserExist(telNum, authorization, new RequestListener() {
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
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_user_exist));
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
