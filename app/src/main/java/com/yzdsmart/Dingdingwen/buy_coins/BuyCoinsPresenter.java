package com.yzdsmart.Dingdingwen.buy_coins;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PayRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopPayLogRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/9/4.
 */
public class BuyCoinsPresenter implements BuyCoinsContract.BuyCoinsPresenter {
    private Context context;
    private BuyCoinsContract.BuyCoinsView mView;
    private BuyCoinsModel mModel;

    public BuyCoinsPresenter(Context context, BuyCoinsContract.BuyCoinsView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new BuyCoinsModel();
        mView.setPresenter(this);
    }

    //    @Override
//    public void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum, String authorization) {
//        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
//        mModel.buyCoins(action, submitCode, bazaCode, goldNum, authorization, new RequestListener() {
//            @Override
//            public void onSuccess(Object result) {
//                ((BaseActivity) context).hideProgressDialog();
//                RequestResponse response = (RequestResponse) result;
//                if ("OK".equals(response.getActionStatus())) {
//                    mView.onBuyCoins(true, context.getResources().getString(R.string.buy_coin_success));
//                } else {
//                    mView.onBuyCoins(false, response.getErrorInfo());
//                }
//            }
//
//            @Override
//            public void onError(String err) {
//                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(err);
//            }
//
//            @Override
//            public void onComplete() {
//            }
//        });
//    }
    @Override
    public void buyCoins(String action, String buyCoinPara, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.buyCoins(action, buyCoinPara, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                PayRequestResponse response = (PayRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onBuyCoins(true, context.getResources().getString(R.string.buy_coin_success), response.getCharge());
                } else {
                    mView.onBuyCoins(false, response.getErrorInfo(), null);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_buy_coin));
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
    public void buyCoinsPay(String payPara, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.buyCoinsPay(payPara, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                PayRequestResponse requestResponse = (PayRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onBuyCoinsPay(requestResponse.getCharge());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_buy_coin_pay));
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
    public void buyCoinsLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.buyCoinsLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                BuyCoinsLogRequestResponse response = (BuyCoinsLogRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onBuyCoinsLog(response.getLists(), response.getLastsequence());
                } else {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_buy_coin_log));
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
    public void getShopPayLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopPayLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopPayLogRequestResponse response = (ShopPayLogRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onShopPayLog(response.getLists(), response.getLastsequence());
                } else {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_buy_coin_pay_log));
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
    public void getNotPayCharge(String submitCode, String chargeId, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getNotPayCharge(submitCode, chargeId, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                PayRequestResponse response = (PayRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onGetNotPayCharge(response.getCharge());
                } else {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_not_pay_charge));
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
