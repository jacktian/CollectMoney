package com.yzdsmart.Dingdingwen.register_login.login;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.LoginRequestResponse;

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

        /**
         * 第三方登录
         *
         * @param requestResponse
         */
        void onThirdPlatformLoginSuccess(LoginRequestResponse requestResponse);

        /**
         * 第三方登录没有绑定手机号
         */
        void onThirdPlatformNotBindPhone();
    }

    interface LoginPresenter extends BasePresenter {
        /**
         * 用户登录
         *
         * @param userName
         * @param password
         * @param loginCode
         */
        void userLogin(String userName, String password, String loginCode, String authorization);

        /**
         * 第三方登录
         *
         * @param userName
         * @param otherElec
         * @param loginCode
         * @param authorization
         */
        void thirdPlatformLogin(String userName, String otherElec, String loginCode, String authorization);

        void unRegisterSubscribe();
    }
}
