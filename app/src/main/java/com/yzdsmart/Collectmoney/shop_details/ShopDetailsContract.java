package com.yzdsmart.Collectmoney.shop_details;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.ShopDetails;

/**
 * Created by YZD on 2016/8/26.
 */
public interface ShopDetailsContract {
    interface ShopDetailsView extends BaseView<ShopDetailsPresenter> {
        /**
         * 商铺详细信息
         *
         * @param shopDetails
         */
        void onGetShopDetails(ShopDetails shopDetails);

        /**
         * @param flag
         * @param action
         */
        void onChangeShopFollow(Boolean flag, String action, String msg);
    }

    interface ShopDetailsPresenter extends BasePresenter {
        /**
         * 获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopDetails(String actioncode, String submitCode, String bazaCode);

        /**
         * 设置对店铺关注
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param bazaCode
         */
        void changeShopFollow(String action, String submitCode, String custCode, String bazaCode);

        void unRegisterSubscribe();
    }
}
