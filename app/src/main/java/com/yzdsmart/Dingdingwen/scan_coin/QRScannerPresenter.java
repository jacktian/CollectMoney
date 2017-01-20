package com.yzdsmart.Dingdingwen.scan_coin;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.ScanCoinRequestResponse;
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
    public void scanQRCode(String actionCode, String retaCode, String custCode, String coor, String ip, final Integer type, String authorization) {
        switch (type) {
            case 0:
                ((BaseActivity) context).showProgressDialog(R.drawable.loading, "正在扫币......");
                break;
            case 1:
                ((BaseActivity) context).showProgressDialog(R.drawable.loading, "正在签到......");
                break;
        }
        mModel.scanQRCode(actionCode, Utils.md5(retaCode + "yzd" + custCode), custCode, retaCode, coor, ip, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ScanCoinRequestResponse response = (ScanCoinRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    switch (type) {
                        case 0:
                            mView.onScanQRCode(true, null, response.getGoldNum(), response.getGoldLogoUrl(), type);
                            break;
                        case 1:
                            mView.onScanQRCode(true, null, null, response.getInfo(), type);
                            break;
                    }
                } else {
                    mView.onScanQRCode(false, response.getErrorInfo(), null, null, type);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                    return;
                }
                switch (type) {
                    case 0:
                        mView.onScanQRCode(false, context.getResources().getString(R.string.get_coins_error), null, null, type);
                        break;
                    case 1:
                        mView.onScanQRCode(false, context.getResources().getString(R.string.sign_error), null, null, type);
                        break;
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
