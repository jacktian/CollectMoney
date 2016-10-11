package com.yzdsmart.Collectmoney.buy_coins;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.BuyCoinsLog;

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
        void onBuyCoins(boolean flag, String msg);

        /**
         * 指定商铺购买金币日志列表
         *
         * @param logList
         * @param lastsequence
         */
        void onBuyCoinsLog(List<BuyCoinsLog> logList, Integer lastsequence);
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
        void buyCoins(String action, String submitCode, String bazaCode, Integer goldNum);

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
        void buyCoinsLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence);

        void unRegisterSubscribe();
    }
}
