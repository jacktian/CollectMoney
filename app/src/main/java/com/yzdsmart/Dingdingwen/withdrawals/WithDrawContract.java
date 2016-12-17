package com.yzdsmart.Dingdingwen.withdrawals;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;

import java.util.List;

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
         * 获取个人金币总数
         *
         * @param counts
         */
        void onGetPersonalLeftCoins(Float counts);

        /**
         * 获取店铺剩余金币数
         *
         * @param counts
         */
        void onGetShopLeftCoins(Float counts);

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

        /**
         * 获取绑定银行卡数据
         *
         * @param bankCards
         */
        void onGetBankCardList(List<BankCard> bankCards);

        /**
         * 获取金币类型
         *
         * @param coinTypes
         */
        void onGetCoinTypes(List<CoinType> coinTypes);
    }

    interface WithDrawPresenter extends BasePresenter {
        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode, String authorization);

        /**
         * 获取个人金币总数
         *
         * @param action
         * @param actiontype
         * @param submitCode
         * @param custCode
         * @param goldType
         * @param authorization
         */
        void getPersonalLeftCoins(String action, String actiontype, String submitCode, String custCode, Integer goldType, String authorization);

        /**
         * 获取店铺剩余金币数
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getShopLeftCoins(String action, String submitCode, String bazaCode, Integer goldType, String authorization);

        /**
         * 商铺提现
         *
         * @param action
         */
        void shopWithdrawCoins(String action, String shopWithdrawPara, String authorization);

        /**
         * 个人提现
         *
         * @param action
         * @param actiontype
         */
        void personalWithdrawCoins(String action, String actiontype, String personalWithdrawPara, String authorization);

        /**
         * 获取绑定银行卡数据
         *
         * @param submitCode
         * @param custCode
         * @param authorization
         */
        void getBankCardList(String submitCode, String custCode, String authorization);

        /**
         * 获取商铺金币类型
         *
         * @param submitCode
         * @param bazaCode
         * @param authorization
         */
        void getShopCoinTypes(String submitCode, String bazaCode, String authorization);

        /**
         * 获取个人金币类型
         *
         * @param submitCode
         * @param custCode
         * @param authorization
         */
        void getPersonalCoinTypes(String submitCode, String custCode, String authorization);

        void unRegisterSubscribe();
    }
}
