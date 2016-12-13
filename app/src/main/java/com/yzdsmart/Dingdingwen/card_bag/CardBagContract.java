package com.yzdsmart.Dingdingwen.card_bag;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.BankCard;

import java.util.List;

/**
 * Created by YZD on 2016/12/13.
 */

public interface CardBagContract {
    interface CardBagView extends BaseView<CardBagPresenter> {
        /**
         * 获取绑定银行卡数据
         *
         * @param bankCards
         */
        void onGetBankCardList(List<BankCard> bankCards);
    }

    interface CardBagPresenter extends BasePresenter {
        /**
         * 获取绑定银行卡数据
         *
         * @param submitCode
         * @param custCode
         * @param authorization
         */
        void getBankCardList(String submitCode, String custCode, String authorization);

        void unRegisterSubscribe();
    }
}
