package com.yzdsmart.Dingdingwen.register_business;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RegisterBusinessRequestResponse;

/**
 * Created by YZD on 2016/9/4.
 */
public class RegisterBusinessPresenter implements RegisterBusinessContract.RegisterBusinessPresenter {
    private Context context;
    private RegisterBusinessContract.RegisterBusinessView mView;
    private RegisterBusinessModel mModel;

    public RegisterBusinessPresenter(Context context, RegisterBusinessContract.RegisterBusinessView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new RegisterBusinessModel();
        mView.setPresenter(this);
    }

    @Override
    public void registerBusiness(String submitCode, String custCode, String bazaName, String bazaPers, String bazaTel, String bazaAddr, final String remark, String coor) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.registering_business));
        mModel.registerBusiness(submitCode, custCode, bazaName, bazaPers, bazaTel, bazaAddr, remark, coor, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RegisterBusinessRequestResponse response = (RegisterBusinessRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onRegisterBusiness(true, context.getResources().getString(R.string.register_business_success));
                } else {
                    mView.onRegisterBusiness(false, response.getErrorInfo());
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

    }
}
