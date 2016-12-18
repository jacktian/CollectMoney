package com.yzdsmart.Dingdingwen.payment;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.ShopDiscount;

import java.util.List;

/**
 * Created by YZD on 2016/12/18.
 */

public interface PaymentContract {
    interface PaymentView extends BaseView<PaymentPresenter> {
        /**
         * 获取金币数
         *
         * @param goldNum
         */
        void onGetCustInfo(Float goldNum);

        /**
         * 获取商铺打折列表
         *
         * @param shopDiscounts
         */
        void onGetShopDiscounts(List<ShopDiscount> shopDiscounts);

        void onSubmitPayment(boolean flag, String msg, Object charge);
    }

    interface PaymentPresenter extends BasePresenter {
        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         * @param authorization
         */
        void getCustInfo(String submitcode, String custCode, String authorization);

        /**
         * 获取商铺打折列表
         *
         * @param submitCode
         * @param bazaCode
         * @param authorization
         */
        void getShopDiscounts(String submitCode, String bazaCode, String authorization);

        /**
         * 个人去商铺消费支付
         *
         * @param action
         * @param paymentPara
         * @param authorization
         */
        void submitPayment(String action, String paymentPara, String authorization);

        void unRegisterSubscribe();
    }
}
