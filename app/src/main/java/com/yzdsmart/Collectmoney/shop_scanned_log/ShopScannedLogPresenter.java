package com.yzdsmart.Collectmoney.shop_scanned_log;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.ScannedLogRequestResponse;

/**
 * Created by YZD on 2016/10/9.
 */

public class ShopScannedLogPresenter implements ShopScannedLogContract.ShopScannedLogPresenter {
    private Context context;
    private ShopScannedLogContract.ShopScannedLogView mView;
    private ShopScannedLogModel mModel;

    public ShopScannedLogPresenter(Context context, ShopScannedLogContract.ShopScannedLogView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new ShopScannedLogModel();
        mView.setPresenter(this);
    }

    @Override
    public void getScannedLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getScannedLog(action, submitCode, bazaCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ScannedLogRequestResponse requestResponse = (ScannedLogRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetScannedLog(requestResponse.getLists());
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
