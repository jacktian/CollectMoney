package com.yzdsmart.Collectmoney.main.personal;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;
import com.yzdsmart.Collectmoney.http.response.ShopInfoByPersRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;

import java.util.List;

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
         * 商家商铺详细信息
         *
         * @param shopDetails
         */
        void onGetShopInfo(ShopInfoByPersRequestResponse shopDetails);

        /**
         * 获取商铺图片列表
         *
         * @param galleyInfos
         */
        void onGetShopGalley(List<GalleyInfo> galleyInfos);
    }

    interface PersonalPresenter extends BasePresenter {
        /**
         * 获取用户等级和星级
         *
         * @param code
         * @param submitcode
         * @param action
         */
        void getCustLevel(String code, String submitcode, String action);

        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode);

        /**
         * 商家获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopInfo(String actioncode, String submitCode, String bazaCode);

        /**
         * 获取商铺图片列表
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getShopGalley(String action, String submitCode, String bazaCode);

        void unRegisterSubscribe();
    }
}
