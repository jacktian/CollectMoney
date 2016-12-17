package com.yzdsmart.Dingdingwen.buy_coins;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.BuyCoinsLog;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.bean.ShopPayLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/4.
 */
public interface BuyCoinsContract {
    interface BuyCoinsView extends BaseView<BuyCoinsPresenter> {
        /**
         * 商户充值金币
         *
         * @param flag
         * @param msg
         */
        void onBuyCoins(boolean flag, String msg, Object charge);

        void onBuyCoinsPay(Object charge);

        /**
         * 指定商铺购买金币日志列表
         *
         * @param logList
         * @param lastsequence
         */
        void onBuyCoinsLog(List<BuyCoinsLog> logList, Integer lastsequence);

        /**
         * 指定商铺购买金币日志列表
         *
         * @param logList
         * @param lastsequence
         */
        void onShopPayLog(List<ShopPayLog> logList, Integer lastsequence);

        void onGetNotPayCharge(Object charge);


        /**
         * 获取金币类型
         *
         * @param coinTypes
         */
        void onGetCoinTypes(List<CoinType> coinTypes);
    }

    interface BuyCoinsPresenter extends BasePresenter {
        /**
         * 商户充值金币
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param goldNum
         */
//        void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum, String authorization);
        void buyCoins(String action, String buyCoinPara, String authorization);

        void buyCoinsPay(String payPara, String authorization);

        /**
         * 指定商铺购买金币日志列表
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         */
        void buyCoinsLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        /**
         * 获取指定商铺支付日志
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         * @param authorization
         */
        void getShopPayLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        /**
         * 获取支付单据
         *
         * @param submitCode
         * @param chargeId
         * @param authorization
         */
        void getNotPayCharge(String submitCode, String chargeId, String authorization);

        /**
         * 获取商铺金币类型
         *
         * @param submitCode
         * @param bazaCode
         * @param authorization
         */
        void getShopCoinTypes(String submitCode, String bazaCode, String authorization);

        void unRegisterSubscribe();
    }
}
