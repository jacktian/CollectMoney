package com.yzdsmart.Collectmoney.edit_personal_info;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;

/**
 * Created by YZD on 2016/9/24.
 */

public interface EditPersonalInfoContract {
    interface EditPersonalInfoView extends BaseView<EditPersonalInfoPresenter> {
        void onGetCustDetailInfo(CustDetailInfoRequestResponse response);
    }

    interface EditPersonalInfoPresenter extends BasePresenter {
        /**
         * 获取用户详细信息
         *
         * @param actioncode
         * @param submitCode
         * @param custCode
         * @return
         */
        void getCustDetailInfo(String actioncode, String submitCode, String custCode);

        void unRegisterSubscribe();
    }
}
