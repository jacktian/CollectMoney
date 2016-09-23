package com.yzdsmart.Collectmoney.register_forget_password.set_info;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/9/23.
 */

public interface SetInfoContract {
    interface SetInfoView extends BaseView<SetInfoPresenter> {
        void onUserRegister();
    }

    interface SetInfoPresenter extends BasePresenter {
        /**
         * 用户手机注册信息
         *
         * @param actioncode
         * @param userName
         * @param password
         * @param cSex
         * @param cAge
         * @param cNickName
         * @param regCode
         */
        void userRegister(String actioncode, String userName, String password, String cSex, Integer cAge, String cNickName, String regCode);

        void unRegisterSubscribe();
    }
}
