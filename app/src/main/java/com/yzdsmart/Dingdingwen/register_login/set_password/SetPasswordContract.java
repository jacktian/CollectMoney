package com.yzdsmart.Dingdingwen.register_login.set_password;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/26.
 */
public interface SetPasswordContract {
    interface SetPasswordView extends BaseView<SetPasswordPresenter> {
        /**
         * 用户注册/修改密码
         *
         * @param flag
         * @param msg
         */
        void onSetPassword(boolean flag, String msg);

        /**
         * 用户登录
         *
         * @param flag
         * @param msg
         */
        void onUserLogin(boolean flag, String msg);
    }

    interface SetPasswordPresenter extends BasePresenter {
        /**
         * 用户注册/修改密码
         *
         * @param userName
         * @param password
         * @param regCode
         */
        void setPassword(String actioncode, String userName, String password, String regCode);

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
