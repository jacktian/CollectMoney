package com.yzdsmart.Dingdingwen.coupon_exchange;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.ScanCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopExchangeRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/12/19.
 */

public class CouponExchangePresenter implements CouponExchangeContract.CouponExchangePresenter {
    private Context context;
    private CouponExchangeContract.CouponExchangeView mView;
    private CouponExchangeModel mModel;

    public CouponExchangePresenter(Context context, CouponExchangeContract.CouponExchangeView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new CouponExchangeModel();
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
    public void getShopExchangeList(String action, String submitCode, String bazaCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopExchangeList(action, submitCode, bazaCode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopExchangeRequestResponse requestResponse = (ShopExchangeRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetExchangeList(requestResponse.getLists());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_shop_coupon));
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
    public void getCoinExchangeList(String action, String submitCode, Integer goldType, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCoinExchangeList(action, submitCode, goldType, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopExchangeRequestResponse requestResponse = (ShopExchangeRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetExchangeList(requestResponse.getLists());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_coin_coupon));
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
    public void exchangeCoupon(String submitCode, String exchangeId, final Double goldNum, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.exchangeCoupon(submitCode, exchangeId, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onExchangeCoupon(goldNum);
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_exchange_coupon));
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
