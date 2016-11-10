package com.yzdsmart.Dingdingwen.buy_coins;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsPayRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;

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

    @Override
    public void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.buyCoins(action, submitCode, bazaCode, goldNum, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onBuyCoins(true, context.getResources().getString(R.string.buy_coin_success));
                } else {
                    mView.onBuyCoins(false, response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void buyCoinsPay(String payPara,String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.buyCoinsPay(payPara,authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                BuyCoinsPayRequestResponse requestResponse = (BuyCoinsPayRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onBuyCoinsPay(requestResponse.getCharge());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
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
        mModel.unRegisterSubscribe();
    }
}
