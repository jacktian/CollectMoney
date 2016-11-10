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
         */
        void onGetCoins(boolean flag, String msg, Integer counts);
    }

    interface QRScannerPresenter extends BasePresenter {
        /**
         * 获取金币
         *
         * @param actionCode
         * @param scannerResult 扫描可以获取商铺内码
         * @param ip
         */
        void getCoins(String actionCode, String scannerResult, String ip, String authorization);

        void unRegisterSubscribe();
    }
}
