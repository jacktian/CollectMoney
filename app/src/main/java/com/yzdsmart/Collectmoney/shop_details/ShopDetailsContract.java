package com.yzdsmart.Collectmoney.shop_details;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/26.
 */
public interface ShopDetailsContract {
    interface ShopDetailsView extends BaseView<ShopDetailsPresenter> {
        void onGetShopDetails();
    }

    interface ShopDetailsPresenter extends BasePresenter {
        /**
         * 获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopDetails(String submitCode, String bazaCode);

        void unRegisterSubscribe();
    }
}
