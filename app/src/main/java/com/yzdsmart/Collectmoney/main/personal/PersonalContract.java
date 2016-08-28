package com.yzdsmart.Collectmoney.main.personal;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/27.
 */
public interface PersonalContract {
    interface PersonalView extends BaseView<PersonalPresenter> {
        /**
         * 获取用户等级和星级
         *
         * @param gra
         * @param sta
         */
        void onGetCustLevel(Integer gra, Integer sta);
    }

    interface PersonalPresenter extends BasePresenter {
        /**
         * 获取用户等级和星级
         *
         * @param custcode
         * @param submitcode
         */
        void getCustLevel(String custcode, String submitcode);

        void unRegisterSubscribe();
    }
}
