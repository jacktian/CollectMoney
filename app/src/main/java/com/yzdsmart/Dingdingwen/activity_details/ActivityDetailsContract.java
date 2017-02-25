package com.yzdsmart.Dingdingwen.activity_details;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.SignDataRequestResponse;

/**
 * Created by YZD on 2017/1/20.
 */

public interface ActivityDetailsContract {
    interface ActivityDetailsView extends BaseView<ActivityDetailsPresenter> {
        /**
         * 定向活动签到数据列表
         *
         * @param flag
         * @param errorInfo
         * @param data
         */
        void onGetSignActivityList(Boolean flag, String errorInfo, SignDataRequestResponse.DataBean data);
    }

    interface ActivityDetailsPresenter extends BasePresenter {
        /**
         * 定向活动签到数据列表
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param authorization
         */
        void getSignActivityList(String action, String submitCode, String custCode, String activityCode, String authorization);

        void unRegisterSubscribe();
    }
}
