package com.yzdsmart.Dingdingwen.payment_log;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.PaymentLogRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/12/26.
 */

public class PaymentLogPresenter implements PaymentLogContract.PaymentLogPresenter {
    private Context context;
    private PaymentLogContract.PaymentLogView mView;
    private PaymentLogModel mModel;

    public PaymentLogPresenter(Context context, PaymentLogContract.PaymentLogView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new PaymentLogModel();
        mView.setPresenter(this);
    }

    @Override
    public void getPersonalPaymentLogs(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getPersonalPaymentLogs(action, submitCode, custCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                PaymentLogRequestResponse requestResponse = (PaymentLogRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetPaymentLogs(requestResponse.getLastsequence(), requestResponse.getLists());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_payment_log));
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
    public void getShopPaymentLogs(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopPaymentLogs(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                PaymentLogRequestResponse requestResponse = (PaymentLogRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetPaymentLogs(requestResponse.getLastsequence(), requestResponse.getLists());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_payment_log));
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
