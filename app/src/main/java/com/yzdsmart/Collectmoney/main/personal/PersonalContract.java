package com.yzdsmart.Collectmoney.main.personal;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;

/**
 * Created by YZD on 2016/8/27.
 */
public interface PersonalContract {
    interface PersonalView extends BaseView<PersonalPresenter> {
        /**
         * 获取用户等级和星级
         *
         * @param gra
         * @param sta
         */
        void onGetCustLevel(Integer gra, Integer sta);

        /**
         * 获取用户信息
         *
         * @param name
         * @param headUel
         * @param goldNum
         */
        void onGetCustInfo(String name, String headUel, Integer goldNum);

        /**
         * 商铺详细信息
         *
         * @param shopDetails
         */
        void onGetShopInfo(ShopInfoRequestResponse shopDetails);
    }

    interface PersonalPresenter extends BasePresenter {
        /**
         * 获取用户等级和星级
         *
         * @param custcode
         * @param submitcode
         */
        void getCustLevel(String custcode, String submitcode);

        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode);

        /**
         * 获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopInfo(String actioncode, String submitCode, String bazaCode, String custCode);

        void unRegisterSubscribe();
    }
}
