package com.yzdsmart.Dingdingwen.bind_bank_card;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

/**
 * Created by YZD on 2016/12/14.
 */

public interface BindBankCardContract {
    interface BindBankCardView extends BaseView<BindBankCardPresenter> {
        void onValidateBankCard(String bankCode);

        void onBindBankCard();
    }

    interface BindBankCardPresenter extends BasePresenter {
        /**
         * 校验银行卡
         *
         * @param submitCode
         * @param bankCardNum
         * @param authorization
         */
        void validateBankCard(String submitCode, String bankCardNum, String authorization);

        /**
         * 绑定银行卡
         *
         * @param submitCode
         * @param custCode
         * @param bankCode
         * @param bankCardNum
         * @param custName
         * @param authorization
         */
        void bindBankCard(String submitCode, String custCode, String bankCode, String bankCardNum, String custName, String authorization);

        void unRegisterSubscribe();
    }
}
