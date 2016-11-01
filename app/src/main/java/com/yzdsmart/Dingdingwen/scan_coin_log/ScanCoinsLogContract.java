package com.yzdsmart.Dingdingwen.scan_coin_log;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.GetCoinsLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/5.
 */
public interface ScanCoinsLogContract {
    interface ScanCoinsLogView extends BaseView<ScanCoinsLogPresenter> {
        /**
         * 用户获取金币日志列表
         *
         * @param logList
         * @param lastsequence
         */
        void onGetCoinsLog(List<GetCoinsLog> logList, Integer lastsequence);
    }

    interface ScanCoinsLogPresenter extends BasePresenter {
        /**
         * 用户获取金币日志列表
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         */
        void getCoinsLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence);

        void unRegisterSubscribe();
    }
}
