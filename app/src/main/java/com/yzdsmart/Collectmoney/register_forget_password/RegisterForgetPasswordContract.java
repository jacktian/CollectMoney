package com.yzdsmart.Collectmoney.register_forget_password;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/25.
 */
public interface RegisterForgetPasswordContract {
    interface RegisterForgetPasswordView extends BaseView<RegisterForgetPasswordPresenter> {
        void onIsUserExist();
    }

    interface RegisterForgetPasswordPresenter extends BasePresenter {
        /**
         * 手机号是否已注册
         *
         * @param telNum
         */
        void isUserExist(Integer telNum);

        void unRegisterSubscribe();
    }
}
