package com.yzdsmart.Dingdingwen.publish_tasks;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.CoinType;

import java.util.List;

/**
 * Created by YZD on 2016/9/4.
 */
public interface PublishTasksContract {
    interface PublishTasksView extends BaseView<PublishTasksPresenter> {
        /**
         * 获取店铺剩余金币数
         *
         * @param counts
         */
        void onGetShopLeftCoins(Float counts);

        /**
         * 商户创建金币任务
         *
         * @param flag
         * @param msg
         */
        void onPublishTask(boolean flag, String msg);

        /**
         * 获取金币类型
         *
         * @param coinTypes
         */
        void onGetCoinTypes(List<CoinType> coinTypes);
    }

    interface PublishTasksPresenter extends BasePresenter {
        /**
         * 获取店铺剩余金币数
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getShopLeftCoins(String action, String submitCode, String bazaCode, Integer goldType, String authorization);

        /**
         * 商户创建金币任务
         *
         * @param submitCode
         * @param bazaCode
         * @param totalGold
         * @param totalNum
         * @param beginTime
         * @param endTime
         */
        void publishTask(String submitCode, String bazaCode, Integer totalGold, Integer totalNum, Integer goldType, String beginTime, String endTime, String authorization);

        /**
         * 获取商铺金币类型
         *
         * @param submitCode
         * @param bazaCode
         * @param authorization
         */
        void getShopCoinTypes(String submitCode, String bazaCode, String authorization);

        void unRegisterSubscribe();
    }
}
