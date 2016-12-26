package com.yzdsmart.Dingdingwen.payment_log;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.PaymentLog;

import java.util.List;

/**
 * Created by YZD on 2016/12/26.
 */

public interface PaymentLogContract {
    interface PaymentLogView extends BaseView<PaymentLogPresenter> {
        /**
         * 消费支付日志
         *
         * @param sequence
         * @param paymentLogs
         */
        void onGetPaymentLogs(Integer sequence, List<PaymentLog> paymentLogs);
    }

    interface PaymentLogPresenter extends BasePresenter {
        /**
         * 个人消费支付日志
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         * @param authorization
         */
        void getPersonalPaymentLogs(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        /**
         * 商铺消费支付日志
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         * @param authorization
         */
        void getShopPaymentLogs(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        void unRegisterSubscribe();
    }
}
