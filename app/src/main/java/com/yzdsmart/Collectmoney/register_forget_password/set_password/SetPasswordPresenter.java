package com.yzdsmart.Collectmoney.register_forget_password.set_password;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.bean.RequestResponse;
import com.yzdsmart.Collectmoney.http.RequestListener;

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
    public void setPassword(String actioncode,String userName, String password, String regCode) {
        mModel.setPassword(actioncode,userName, password, regCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onSetPassword(true, response.getErrorInfo());
                } else if ("FAIL".equals(response.getActionStatus())) {
                    mView.onSetPassword(false, response.getErrorInfo());
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
