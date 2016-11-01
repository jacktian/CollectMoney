package com.yzdsmart.Dingdingwen.register_business;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

/**
 * Created by YZD on 2016/9/4.
 */
public interface RegisterBusinessContract {
    interface RegisterBusinessView extends BaseView<RegisterBusinessPresenter> {
        void onRegisterBusiness(boolean flag, String msg);
    }

    interface RegisterBusinessPresenter extends BasePresenter {
        /**
         * 个人升级为商铺注册信息
         *
         * @param submitCode
         * @param custCode
         * @param bazaName
         * @param bazaPers
         * @param bazaTel
         * @param bazaAddr
         * @param remark
         * @param coor
         */
        void registerBusiness(String submitCode, String custCode, String bazaName, String bazaPers, String bazaTel, String bazaAddr, String remark, String coor);

        void unRegisterSubscribe();
    }
}
