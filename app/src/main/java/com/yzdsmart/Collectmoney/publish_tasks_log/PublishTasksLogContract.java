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

        void onPublishTaskLog(List<PublishTaskLog> logList);
    }

    interface PublishTasksLogPresenter extends BasePresenter {
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
