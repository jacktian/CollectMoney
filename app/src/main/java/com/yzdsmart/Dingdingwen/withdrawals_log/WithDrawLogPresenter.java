package com.yzdsmart.Dingdingwen.withdrawals_log;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.PersonalWithdrawLogRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopWithdrawLogRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getPersonalWithdrawLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getPersonalWithdrawLog(action, submitCode, custCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
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
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_withdraw_log));
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
    public void getShopWithdrawLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopWithdrawLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
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
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_withdraw_log));
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
