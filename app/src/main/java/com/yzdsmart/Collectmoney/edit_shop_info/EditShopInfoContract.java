package com.yzdsmart.Collectmoney.edit_shop_info;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.http.response.ShopInfoByPersRequestResponse;

/**
 * Created by YZD on 2016/9/25.
 */

public interface EditShopInfoContract {
    interface EditShopInfoView extends BaseView<EditShopInfoPresenter> {
        /**
         * 商家商铺详细信息
         *
         * @param shopDetails
         */
        void onGetShopInfo(ShopInfoByPersRequestResponse shopDetails);

        /**
         * 设置商铺详细信息
         *
         * @param editItem
         */
        void onSetShopInfos(Integer editItem);
    }

    interface EditShopInfoPresenter extends BasePresenter {
        /**
         * 商家获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopInfo(String actioncode, String submitCode, String bazaCode);

        /**
         * 设置商铺详细信息
         *
         * @param editItem
         * @param submitCode
         * @param bazaCode
         * @param bazaName
         * @param bazaPers
         * @param bazaTel
         * @param bazaAddr
         * @param remark
         * @param coor
         */
        void setShopInfos(Integer editItem, String submitCode, String bazaCode, String bazaName, String bazaPers, String bazaTel, String bazaAddr, String remark, String coor);

        void unRegisterSubscribe();
    }
}
