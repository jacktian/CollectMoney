package com.yzdsmart.Dingdingwen.withdrawals;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ValidateBankCardRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.WithdrawRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by jacks on 2016/9/23.
 */

public class WithDrawPresenter implements WithDrawContract.WithDrawPresenter {
    private Context context;
    private WithDrawContract.WithDrawView mView;
    private WithDrawModel mModel;

    public WithDrawPresenter(Context context, WithDrawContract.WithDrawView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new WithDrawModel();
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
                    mView.onGetCustInfo(requestResponse);
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
    public void getLeftCoins(String action, String submitCode, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getLeftCoins(action, submitCode, bazaCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetCoinRequestResponse requestResponse = (GetCoinRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetLeftCoins(requestResponse.getGoldNum());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_left_coins));
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
    public void shopWithdrawCoins(String action, String submitCode, String bazaCode, Integer goldNum, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.withdrawing));
        mModel.shopWithdrawCoins(action, submitCode, bazaCode, goldNum, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                WithdrawRequestResponse requestResponse = (WithdrawRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onShopWithdrawCoins(requestResponse.getCash());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_withdraw));
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
    public void personalWithdrawCoins(String action, String actiontype, String submitCode, String custCode, Integer goldNum, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.withdrawing));
        mModel.personalWithdrawCoins(action, actiontype, submitCode, custCode, goldNum, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                WithdrawRequestResponse requestResponse = (WithdrawRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onPersonalWithdrawCoins(requestResponse.getCash());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_withdraw));
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
    public void validateBankCard(String submitCode, String bandCardNum, String authorization) {
        mModel.validateBankCard(submitCode, bandCardNum, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ValidateBankCardRequestResponse requestResponse = (ValidateBankCardRequestResponse) result;
            }

            @Override
            public void onError(String err) {

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
