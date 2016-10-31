package com.yzdsmart.Dingdingwen.register_login.verify_phone;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/26.
 */
public interface VerifyPhoneContract {
    interface VerifyPhoneView extends BaseView<VerifyPhonePresenter> {
        /**
         * 判断手机号码是否已注册
         *
         * @param phoneUsable
         * @param msg
         */
        void onIsUserExist(boolean phoneUsable, String msg);
    }

    interface VerifyPhonePresenter extends BasePresenter {
        /**
         * 手机号是否已注册
         *
         * @param telNum
         */
        void isUserExist(String telNum);

        void unRegisterSubscribe();
    }
}
