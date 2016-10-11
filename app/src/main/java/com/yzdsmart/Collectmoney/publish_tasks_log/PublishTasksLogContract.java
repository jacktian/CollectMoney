package com.yzdsmart.Collectmoney.publish_tasks_log;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.PublishTaskLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/18.
 */
public interface PublishTasksLogContract {
    interface PublishTasksLogView extends BaseView<PublishTasksLogPresenter> {

        /**
         * 获取店铺任务剩余金币数
         *
         * @param counts
         */
        void onGetLeftCoins(Integer counts);

        /**
         * 指定商铺获得发布任务日志列表
         *
         * @param logList
         * @param lastsequence
         */
        void onPublishTaskLog(List<PublishTaskLog> logList, Integer lastsequence);

    }

    interface PublishTasksLogPresenter extends BasePresenter {
        /**
         * 获取店铺任务剩余金币数
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getLeftCoins(String action, String submitCode, String bazaCode);

        /**
         * 指定商铺获得发布任务日志列表
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         */
        void publishTaskLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence);

        void unRegisterSubscribe();
    }
}
