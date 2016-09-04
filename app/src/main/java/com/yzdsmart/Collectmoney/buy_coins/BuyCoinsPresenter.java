package com.yzdsmart.Collectmoney.buy_coins;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
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
        mModel.buyCoins(action, submitCode, bazaCode, goldNum, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onBuyCoins(true, null);
                } else {
                    mView.onBuyCoins(false, response.getErrorInfo());
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
