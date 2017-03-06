package com.yzdsmart.Dingdingwen.payment;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.PayRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ScanCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopDiscountRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/12/18.
 */

public class PaymentPresenter implements PaymentContract.PaymentPresenter {
    private Context context;
    private PaymentContract.PaymentView mView;
    private PaymentModel mModel;

    public PaymentPresenter(Context context, PaymentContract.PaymentView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new PaymentModel();
        mView.setPresenter(this);
    }

    @Override
    public void getPersonalLeftCoins(String action, String actiontype, String submitCode, String custCode, Integer goldType, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getPersonalLeftCoins(action, actiontype, submitCode, custCode, goldType, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ScanCoinRequestResponse requestResponse = (ScanCoinRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetPersonalLeftCoins(requestResponse.getGoldNum());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_left_coins));
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
    public void getShopLeftCoins(String action, String submitCode, String bazaCode, Integer goldType, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopLeftCoins(action, submitCode, bazaCode, goldType, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ScanCoinRequestResponse requestResponse = (ScanCoinRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetShopLeftCoins(requestResponse.getGoldNum());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_left_coins));
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
    public void getShopDiscounts(String submitCode, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopDiscounts(submitCode, bazaCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopDiscountRequestResponse requestResponse = (ShopDiscountRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetShopDiscounts(requestResponse.getLists());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_shop_discount));
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
    public void submitPayment(String action, String paymentPara, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.submitPayment(action, paymentPara, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                PayRequestResponse response = (PayRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onSubmitPayment(true, context.getResources().getString(R.string.payment_submit_payment_success), response.getCharge());
                } else {
                    mView.onSubmitPayment(false, response.getErrorInfo(), null);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_buy_coin));
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
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
