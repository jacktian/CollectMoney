package com.yzdsmart.Dingdingwen.shop_scanned_log;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.ScannedLog;

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
         * @param lastsequence
         */
        void onGetScannedLog(List<ScannedLog> scannedLogs, Integer lastsequence);
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
         * @param lastsequence
         */
        void getScannedLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence);

        void unRegisterSubscribe();
    }
}
