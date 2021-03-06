package com.yzdsmart.Collectmoney.withdrawals_log;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.PersonalWithdrawLogRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopWithdrawLogRequestResponse;

/**
 * Created by YZD on 2016/9/24.
 */

public class WithDrawLogPresenter implements WithDrawLogContract.WithDrawLogPresenter {
    private Context context;
    private WithDrawLogContract.WithDrawLogView mView;
    private WithDrawLogModel mModel;

    public WithDrawLogPresenter(Context context, WithDrawLogContract.WithDrawLogView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new WithDrawLogModel();
        mView.setPresenter(this);
    }

    @Override
    public void getPersonalWithdrawLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getPersonalWithdrawLog(action, submitCode, custCode, pageIndex, pageSize, lastsequence, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                PersonalWithdrawLogRequestResponse requestResponse = (PersonalWithdrawLogRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetPersonalWithdrawLog(requestResponse.getLists(), requestResponse.getLastsequence());
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
    public void getShopWithdrawLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopWithdrawLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopWithdrawLogRequestResponse requestResponse = (ShopWithdrawLogRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetShopWithdrawLog(requestResponse.getLists(), requestResponse.getLastsequence());
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
