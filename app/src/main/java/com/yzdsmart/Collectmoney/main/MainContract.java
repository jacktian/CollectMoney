package com.yzdsmart.Collectmoney.main;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/30.
 */
public interface MainContract {
    interface MainView extends BaseView<MainPresenter> {
        /**
         * TLS账号登陆成功
         */
        void chatLoginSuccess();

        /**
         * im sdk登录成功
         */
        void imSDKLoginSuccess();

        /**
         * 腾讯云通信下线
         */
        void onIMOffline();
    }

    interface MainPresenter extends BasePresenter {
        /**
         * 用户聊天登录
         *
         * @param im_name
         * @param im_pwd
         */
        void chatLogin(String im_name, String im_pwd);

        /**
         * im sdk 登录
         */
        void imSDKLogin();

        void unRegisterObserver();
    }
}
