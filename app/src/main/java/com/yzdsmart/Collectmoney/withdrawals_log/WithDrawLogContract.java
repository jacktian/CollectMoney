package com.yzdsmart.Collectmoney.withdrawals_log;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.PersonalWithdrawLog;
import com.yzdsmart.Collectmoney.bean.ShopWithdrawLog;

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
         */
        void onGetPersonalWithdrawLog(List<PersonalWithdrawLog> personalWithdrawLogs);

        /**
         * 商铺提现日志
         *
         * @param shopWithdrawLogs
         */
        void onGetShopWithdrawLog(List<ShopWithdrawLog> shopWithdrawLogs);
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
         */
        void getPersonalWithdrawLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize);

        /**
         * 商铺提现日志
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         */
        void getShopWithdrawLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
