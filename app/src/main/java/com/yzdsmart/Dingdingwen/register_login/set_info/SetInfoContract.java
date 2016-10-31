package com.yzdsmart.Dingdingwen.register_login.set_info;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/9/23.
 */

public interface SetInfoContract {
    interface SetInfoView extends BaseView<SetInfoPresenter> {
        /**
         * 用户注册
         */
        void onUserRegister();

        /**
         * 用户登录
         *
         * @param flag
         * @param msg
         */
        void onUserLogin(boolean flag, String msg);
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

        /**
         * 用户登录
         *
         * @param userName
         * @param password
         * @param loginCode
         */
        void userLogin(String userName, String password, String loginCode);

        void unRegisterSubscribe();
    }
}
