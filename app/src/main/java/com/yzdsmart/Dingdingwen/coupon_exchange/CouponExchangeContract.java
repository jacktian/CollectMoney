package com.yzdsmart.Dingdingwen.coupon_exchange;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.CouponBean;

import java.util.List;

/**
 * Created by YZD on 2016/12/19.
 */

public interface CouponExchangeContract {
    interface CouponExchangeView extends BaseView<CouponExchangePresenter> {
        /**
         * 获取用户信息
         *
         * @param coinCounts
         */
        void onGetCustInfo(Float coinCounts);

        /**
         * 商家获取商铺详情
         *
         * @param coinCounts
         */
        void onGetShopInfo(Float coinCounts);

        /**
         * 指定商铺兑换列表
         *
         * @param couponBeans
         */
        void onGetExchangeList(List<CouponBean> couponBeans);

        /**
         * 兑换商品
         */
        void onExchangeCoupon();
    }

    interface CouponExchangePresenter extends BasePresenter {
        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode, String authorization);

        /**
         * 商家获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopInfo(String actioncode, String submitCode, String bazaCode, String authorization);

        /**
         * 指定商铺兑换列表
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param custCode
         * @param authorization
         */
        void getShopExchangeList(String action, String submitCode, String bazaCode, String custCode, String authorization);

        /**
         * 获取指定商铺兑换列表
         *
         * @param action
         * @param submitCode
         * @param goldType
         * @param custCode
         * @param authorization
         */
        void getCoinExchangeList(String action, String submitCode, Integer goldType, String custCode, String authorization);

        /**
         * 兑换商品
         *
         * @param submitCode
         * @param exchangeId
         * @param custCode
         * @param authorization
         */
        void exchangeCoupon(String submitCode, Integer exchangeId, String custCode, String authorization);

        void unRegisterSubscribe();
    }
}
