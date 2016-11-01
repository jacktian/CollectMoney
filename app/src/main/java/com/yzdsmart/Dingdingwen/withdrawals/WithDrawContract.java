package com.yzdsmart.Dingdingwen.withdrawals;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;

/**
 * Created by jacks on 2016/9/23.
 */

public interface WithDrawContract {
    interface WithDrawView extends BaseView<WithDrawPresenter> {
        /**
         * 获取用户信息
         *
         * @param response
         */
        void onGetCustInfo(CustInfoRequestResponse response);

        /**
         * 获取店铺剩余金币数
         *
         * @param counts
         */
        void onGetLeftCoins(Integer counts);

        /**
         * 商铺提现
         *
         * @param withdrawRMB
         */
        void onShopWithdrawCoins(String withdrawRMB);

        /**
         * 个人提现
         *
         * @param withdrawRMB
         */
        void onPersonalWithdrawCoins(String withdrawRMB);
    }

    interface WithDrawPresenter extends BasePresenter {
        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode);

        /**
         * 获取店铺剩余金币数
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getLeftCoins(String action, String submitCode, String bazaCode);

        /**
         * 商铺提现
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param goldNum
         */
        void shopWithdrawCoins(String action, String submitCode, String bazaCode, Integer goldNum);

        /**
         * 个人提现
         *
         * @param action
         * @param actiontype
         * @param submitCode
         * @param custCode
         * @param goldNum
         */
        void personalWithdrawCoins(String action, String actiontype, String submitCode, String custCode, Integer goldNum);

        void unRegisterSubscribe();
    }
}
