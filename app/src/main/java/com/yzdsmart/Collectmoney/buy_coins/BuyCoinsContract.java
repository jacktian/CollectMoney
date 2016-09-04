package com.yzdsmart.Collectmoney.buy_coins;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/9/4.
 */
public interface BuyCoinsContract {
    interface BuyCoinsView extends BaseView<BuyCoinsPresenter> {
        void onBuyCoins(boolean flag, String msg);
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

        void unRegisterSubscribe();
    }
}
