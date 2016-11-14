package com.yzdsmart.Dingdingwen.splash;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

/**
 * Created by YZD on 2016/11/14.
 */

public interface SplashContract {
    interface SplashView extends BaseView<SplashPresenter> {
        void onAppRegister(boolean flag);
    }

    interface SplashPresenter extends BasePresenter {
        /**
         * 注册应用
         *
         * @param appCode
         * @param appId
         * @param appSecret
         */
        void appRegister(String appCode, String appId, String appSecret);

        void unRegisterSubscribe();
    }
}
