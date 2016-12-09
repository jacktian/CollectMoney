package com.yzdsmart.Dingdingwen.register_login.verify_code;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

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
        void getVerifyCode(String telNum, String currDate, String authorization);

        /**
         * 验证短信验证码
         *
         * @param telNum
         * @param verifyCode
         */
        void validateVerifyCode(String actioncode, String telNum, String verifyCode, String authorization);

        void thirdPlatformRegister(String actioncode, String userName, String password, String cSex, Integer cAge, String cNickName, String otherElec, String platformExportData,String regCode, String authorization);

        void unRegisterSubscribe();
    }
}
