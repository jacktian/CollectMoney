package com.yzdsmart.Dingdingwen.shop_scanned_log;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.ScannedLogRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getScannedLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getScannedLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ScannedLogRequestResponse requestResponse = (ScannedLogRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetScannedLog(requestResponse.getLists(), requestResponse.getLastsequence());
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
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
