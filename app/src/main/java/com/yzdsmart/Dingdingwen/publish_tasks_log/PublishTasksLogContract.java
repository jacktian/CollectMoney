package com.yzdsmart.Dingdingwen.publish_tasks_log;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.PublishTaskLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/18.
 */
public interface PublishTasksLogContract {
    interface PublishTasksLogView extends BaseView<PublishTasksLogPresenter> {

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
         * 指定商铺获得发布任务日志列表
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         */
        void publishTaskLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        void unRegisterSubscribe();
    }
}
