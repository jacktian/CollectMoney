package com.yzdsmart.Dingdingwen.coupon_exchange;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopExchangeRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoByPersRequestResponse;
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
    public void getCustInfo(String submitcode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCustInfo(submitcode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustInfoRequestResponse requestResponse = (CustInfoRequestResponse) result;
                if (null != requestResponse) {
                    mView.onGetCustInfo(requestResponse.getGoldNum());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_cust_info));
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
    public void getShopInfo(String actioncode, String submitCode, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopInfo(actioncode, submitCode, bazaCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopInfoByPersRequestResponse requestResponse = (ShopInfoByPersRequestResponse) result;
                if (null != requestResponse) {
                    mView.onGetShopInfo(requestResponse.getTotalGlodNum());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_shop_info));
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
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_shop_coupon));
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
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_coin_coupon));
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
    public void exchangeCoupon(String submitCode, Integer exchangeId, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.exchangeCoupon(submitCode, exchangeId, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onExchangeCoupon();
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_exchange_coupon));
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
