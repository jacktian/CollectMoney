package com.yzdsmart.Dingdingwen.settings;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;

/**
 * Created by YZD on 2016/9/3.
 */
public interface SettingsContract {
    interface SettingView extends BaseView<SettingsPresenter> {
        void onGetCustDetailInfo(CustDetailInfoRequestResponse response);
    }

    interface SettingsPresenter extends BasePresenter {
        /**
         * 获取用户详细信息
         *
         * @param actioncode
         * @param submitCode
         * @param custCode
         * @param selfCustCode
         * @return
         */
        void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode, String authorization);

        void unRegisterSubscribe();
    }
}
