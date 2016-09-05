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
        void onPublishTask(boolean flag, String msg);

        void onPublishTaskLog(List<PublishTaskLog> logList);
    }

    interface PublishTasksPresenter extends BasePresenter {
        /**
         * 商户创建金币任务
         *
         * @param submitCode
         * @param bazaCode
         * @param totalGold
         * @param sMinGold
         * @param sMaxGold
         * @param beginTime
         * @param endTime
         */
        void publishTask(String submitCode, String bazaCode, Integer totalGold, Integer sMinGold, Integer sMaxGold, String beginTime, String endTime);

        /**
         * 指定商铺获得发布任务日志列表
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         */
        void publishTaskLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
