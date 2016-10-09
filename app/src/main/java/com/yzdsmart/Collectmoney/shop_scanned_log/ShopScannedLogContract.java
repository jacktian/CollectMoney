package com.yzdsmart.Collectmoney.shop_scanned_log;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.ScannedLog;

import java.util.List;

/**
 * Created by YZD on 2016/10/9.
 */

public class ShopScannedLogContract {
    interface ShopScannedLogView extends BaseView<ShopScannedLogPresenter> {
        /**
         * 获取指定商铺被扫码日志
         *
         * @param scannedLogs
         */
        void onGetScannedLog(List<ScannedLog> scannedLogs);
    }

    interface ShopScannedLogPresenter extends BasePresenter {
        /**
         * 获取指定商铺被扫码日志
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         */
        void getScannedLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
