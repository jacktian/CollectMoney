package com.yzdsmart.Dingdingwen.publish_tasks;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

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
        void onGetLeftCoins(Integer counts);

        /**
         * 商户创建金币任务
         *
         * @param flag
         * @param msg
         */
        void onPublishTask(boolean flag, String msg);
    }

    interface PublishTasksPresenter extends BasePresenter {
        /**
         * 获取店铺剩余金币数
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getLeftCoins(String action, String submitCode, String bazaCode, String authorization);

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

        void unRegisterSubscribe();
    }
}
