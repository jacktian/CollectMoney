package com.yzdsmart.Collectmoney.register_forget_password.set_password;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/26.
 */
public interface SetPasswordContract {
    interface SetPasswordView extends BaseView<SetPasswordPresenter> {

    }

    interface SetPasswordPresenter extends BasePresenter {
        /**
         * 用户注册/修改密码
         *
         * @param userName
         * @param password
         * @param regCode
         */
        void setPassword(String userName, String password, String regCode);

        void unRegisterSubscribe();
    }
}
