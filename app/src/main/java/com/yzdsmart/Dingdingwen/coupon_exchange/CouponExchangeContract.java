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
         * 获取个人金币总数
         *
         * @param goldNum
         */
        void onGetPersonalLeftCoins(Double goldNum);

        /**
         * 获取店铺剩余金币数
         *
         * @param goldNum
         */
        void onGetShopLeftCoins(Double goldNum);

        /**
         * 指定商铺兑换列表
         *
         * @param couponBeans
         */
        void onGetExchangeList(List<CouponBean> couponBeans);

        /**
         * 兑换商品
         */
        void onExchangeCoupon(Double goldNum);
    }

    interface CouponExchangePresenter extends BasePresenter {
        /**
         * 获取个人金币总数
         *
         * @param action
         * @param actiontype
         * @param submitCode
         * @param custCode
         * @param goldType
         * @param authorization
         */
        void getPersonalLeftCoins(String action, String actiontype, String submitCode, String custCode, Integer goldType, String authorization);

        /**
         * 获取店铺剩余金币数
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getShopLeftCoins(String action, String submitCode, String bazaCode, Integer goldType, String authorization);

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
        void exchangeCoupon(String submitCode, String exchangeId, Double goldNum, String custCode, String authorization);

        void unRegisterSubscribe();
    }
}
