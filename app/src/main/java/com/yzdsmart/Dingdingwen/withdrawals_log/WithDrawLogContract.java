package com.yzdsmart.Dingdingwen.withdrawals_log;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.PersonalWithdrawLog;
import com.yzdsmart.Dingdingwen.bean.ShopWithdrawLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/24.
 */

public interface WithDrawLogContract {
    interface WithDrawLogView extends BaseView<WithDrawLogPresenter> {
        /**
         * 个人提现日志
         *
         * @param personalWithdrawLogs
         * @param lastsequence
         */
        void onGetPersonalWithdrawLog(List<PersonalWithdrawLog> personalWithdrawLogs, Integer lastsequence);

        /**
         * 商铺提现日志
         *
         * @param shopWithdrawLogs
         * @param lastsequence
         */
        void onGetShopWithdrawLog(List<ShopWithdrawLog> shopWithdrawLogs, Integer lastsequence);
    }

    interface WithDrawLogPresenter extends BasePresenter {
        /**
         * 个人提现日志
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         */
        void getPersonalWithdrawLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        /**
         * 商铺提现日志
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         */
        void getShopWithdrawLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        void unRegisterSubscribe();
    }
}
