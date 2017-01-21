package com.yzdsmart.Dingdingwen.time_keeper;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.SignDataRequestResponse;

/**
 * Created by YZD on 2017/1/20.
 */

public interface TimeKeeperContract {
    interface TimeKeeperView extends BaseView<TimeKeeperPresenter> {
        /**
         * 定向活动签到数据列表
         *
         * @param flag
         * @param errorInfo
         * @param data
         */
        void onGetSignActivityList(Boolean flag, String errorInfo, SignDataRequestResponse.DataBean data);
    }

    interface TimeKeeperPresenter extends BasePresenter {
        /**
         * 定向活动签到数据列表
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param authorization
         */
        void getSignActivityList(String action, String submitCode, String custCode, String authorization);

        void unRegisterSubscribe();
    }
}
