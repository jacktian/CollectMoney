package com.yzdsmart.Dingdingwen.register_login.login;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/26.
 */
public interface LoginContract {
    interface LoginView extends BaseView<LoginPresenter> {
        /**
         * 用户登录
         *
         * @param flag
         * @param msg
         */
        void onUserLogin(boolean flag, String msg);
    }

    interface LoginPresenter extends BasePresenter {
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
