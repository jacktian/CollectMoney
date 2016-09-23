package com.yzdsmart.Collectmoney.withdrawals;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;

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

        void onGetLeftCoins(Integer counts);
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

        void unRegisterSubscribe();
    }
}
