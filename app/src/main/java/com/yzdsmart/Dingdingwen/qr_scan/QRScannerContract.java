package com.yzdsmart.Dingdingwen.qr_scan;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

/**
 * Created by YZD on 2016/9/3.
 */
public interface QRScannerContract {
    interface QRScannerView extends BaseView<QRScannerPresenter> {
        /**
         * @param flag
         * @param msg    获取金币返回信息
         * @param counts 获得金币数
         * @param type   0 获取金币  1 签到
         */
        void onScanQRCode(boolean flag, String msg, Double counts, String coinLogo, Integer type);
    }

    interface QRScannerPresenter extends BasePresenter {

        /**
         * 获取金币
         *
         * @param actionCode
         * @param retaCode      扫描可以获取商铺内码
         * @param custCode
         * @param coor
         * @param ip
         * @param type          0 获取金币  1 签到
         * @param authorization
         */
        void scanQRCode(String actionCode, String retaCode, String custCode, String coor, String ip, Integer type, String authorization);

        void unRegisterSubscribe();
    }
}
