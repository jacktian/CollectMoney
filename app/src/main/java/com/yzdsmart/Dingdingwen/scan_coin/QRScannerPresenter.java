package com.yzdsmart.Dingdingwen.scan_coin;

import android.content.Context;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.Utils;

/**
 * Created by YZD on 2016/9/3.
 */
public class QRScannerPresenter implements QRScannerContract.QRScannerPresenter {
    private Context context;
    private QRScannerContract.QRScannerView mView;
    private QRScannerModel mModel;

    public QRScannerPresenter(Context context, QRScannerContract.QRScannerView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new QRScannerModel();
        mView.setPresenter(this);
    }

    @Override
    public void getCoins(String actionCode, String retaCode, String custCode, String coor, String ip, String authorization) {
        mModel.getCoins(actionCode, Utils.md5(retaCode + "yzd" + custCode), custCode, retaCode, coor, ip, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetCoinRequestResponse response = (GetCoinRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onGetCoins(true, null, response.getGoldNum(), response.getGoldLogoUrl());
                } else {
                    mView.onGetCoins(false, response.getErrorInfo(), null, null);
                }
            }

            @Override
            public void onError(String err) {
                mView.onGetCoins(false, context.getResources().getString(R.string.get_coins_error), null, null);
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
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
