package com.yzdsmart.Collectmoney.publish_tasks;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.PublishTaskLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/4.
 */
public interface PublishTasksContract {
    interface PublishTasksView extends BaseView<PublishTasksPresenter> {
        void onGetLeftCoins(Integer counts);

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
        void getLeftCoins(String action, String submitCode, String bazaCode);

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
        void publishTask(String submitCode, String bazaCode, Integer totalGold, Integer totalNum, String beginTime, String endTime);

        void unRegisterSubscribe();
    }
}
