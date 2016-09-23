package com.yzdsmart.Collectmoney.withdrawals;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetCoinRequestResponse;

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
    public void getCustInfo(String submitcode, String custCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCustInfo(submitcode, custCode, new RequestListener() {
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
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void getLeftCoins(String action, String submitCode, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getLeftCoins(action, submitCode, bazaCode, new RequestListener() {
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
