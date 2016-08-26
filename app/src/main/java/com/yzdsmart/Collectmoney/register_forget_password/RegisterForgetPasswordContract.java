package com.yzdsmart.Collectmoney.register_forget_password;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/25.
 */
public interface RegisterForgetPasswordContract {
    interface RegisterForgetPasswordView extends BaseView<RegisterForgetPasswordPresenter> {
        /**
         * 判断手机号码是否已注册
         *
         * @param phoneUsable
         */
        void onIsUserExist(boolean phoneUsable);

        /**
         * 判断手机号码是否已注册
         *
         * @param phoneUsable
         */
        void onGetVerifyCodeUsable(boolean phoneUsable);
    }

    interface RegisterForgetPasswordPresenter extends BasePresenter {
        /**
         * 手机号是否已注册
         *
         * @param telNum
         */
        void isUserExist(String telNum);

        /**
         * 手机号是否已注册
         * 焦点在用户名输入框，点击获取验证码(注册页)
         *
         * @param telNum
         */
        void getVerifyCodeUsable(String telNum);

        /**
         * 获取短信验证码
         *
         * @param telNum
         * @param currDate
         */
        void getVerifyCode(String telNum, String currDate);

        void unRegisterSubscribe();
    }
}
