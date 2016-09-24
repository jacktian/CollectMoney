package com.yzdsmart.Collectmoney.buy_coins;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.BuyCoinLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

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
    public void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.buyCoins(action, submitCode, bazaCode, goldNum, new RequestListener() {
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
    public void buyCoinsLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.buyCoinsLog(action, submitCode, bazaCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                BuyCoinLogRequestResponse response = (BuyCoinLogRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    if (null != response.getLists() && response.getLists().size() > 0) {
                        mView.onBuyCoinsLog(response.getLists());
                    }
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
