package com.yzdsmart.Collectmoney.register_login_password.verify_code;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/26.
 */
public interface VerifyCodeContract {
    interface VerifyCodeView extends BaseView<VerifyCodePresenter> {
        /**
         * 获取短信验证码
         *
         * @param flag
         * @param msg
         */
        void onGetVerifyCode(boolean flag, String msg);

        /**
         * 验证短信验证码
         *
         * @param flag
         * @param msg
         */
        void onValidateVerifyCode(boolean flag, String msg);
    }

    interface VerifyCodePresenter extends BasePresenter {
        /**
         * 获取短信验证码
         *
         * @param telNum
         * @param currDate
         */
        void getVerifyCode(String telNum, String currDate);

        /**
         * 验证短信验证码
         *
         * @param telNum
         * @param verifyCode
         */
        void validateVerifyCode(String actioncode, String telNum, String verifyCode);

        void unRegisterSubscribe();
    }
}
