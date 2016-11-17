package com.yzdsmart.Dingdingwen.qr_scan;

import android.content.Context;
import android.net.Uri;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
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
    public void getCoins(String actionCode, String scannerResult, String ip, String authorization) {
        String retaCode = Uri.parse(scannerResult).getQueryParameter("RetaCode");
        String coor = SharedPreferencesUtils.getString(context, "qLocation", "");
        String custCode = SharedPreferencesUtils.getString(context, "cust_code", "");
        mModel.getCoins(actionCode, Utils.md5(retaCode + "yzd" + custCode), custCode, retaCode, coor, ip, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetCoinRequestResponse response = (GetCoinRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onGetCoins(true, null, response.getGoldNum());
                } else {
                    mView.onGetCoins(false, response.getErrorInfo(), null);
                }
            }

            @Override
            public void onError(String err) {
                mView.onGetCoins(false, context.getResources().getString(R.string.get_coins_error), null);
                if (err.contains("HTTP 401 Unauthorized")) {
                    MainActivity.getInstance().refreshAccessToken();
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
