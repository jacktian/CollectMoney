package com.yzdsmart.Collectmoney.shop_details;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.ShopScanner;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;

import java.util.List;

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
        void onGetShopInfo(ShopInfoRequestResponse shopDetails);

        /**
         * @param flag
         * @param action
         */
        void onSetFollow(Boolean flag, String action, String msg);

        /**
         * 获取指定店铺的获取金币的用户信息
         *
         * @param shopScanners
         */
        void onGetShopFollowers(List<ShopScanner> shopScanners);
    }

    interface ShopDetailsPresenter extends BasePresenter {
        /**
         * 获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopInfo(String actioncode, String submitCode, String bazaCode, String custCode);

        /**
         * 设置对店铺关注
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param bazaCode
         */
        void setFollow(String action, String submitCode, String custCode, String bazaCode);

        /**
         * 获取指定店铺的获取金币的用户信息
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param custCode
         * @param pageIndex
         * @param pageSize
         */
        void getShopFollowers(String action, String submitCode, String bazaCode, String custCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
