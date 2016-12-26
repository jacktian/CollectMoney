package com.yzdsmart.Dingdingwen.coupon_log;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.CouponLog;

import java.util.List;

/**
 * Created by YZD on 2016/12/26.
 */

public interface CouponLogContract {
    interface CouponLogView extends BaseView<CouponLogPresenter> {
        /**
         * 兑换日志
         *
         * @param sequence
         * @param paymentLogs
         */
        void onGetCouponLogs(Integer sequence, List<CouponLog> paymentLogs);
    }

    interface CouponLogPresenter extends BasePresenter {
        /**
         * 个人兑换日志
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         * @param authorization
         */
        void getPersonalCouponLogs(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        /**
         * 商铺兑换商品日志
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         * @param authorization
         */
        void getShopCouponLogs(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        void unRegisterSubscribe();
    }
}
