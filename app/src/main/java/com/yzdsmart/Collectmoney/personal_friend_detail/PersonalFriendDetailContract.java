package com.yzdsmart.Collectmoney.personal_friend_detail;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;

/**
 * Created by YZD on 2016/8/29.
 */
public interface PersonalFriendDetailContract {
    interface PersonalFriendDetailView extends BaseView<PersonalFriendDetailPresenter> {
        /**
         * 获取用户等级和星级
         *
         * @param gra
         * @param sta
         */
        void onGetCustLevel(Integer gra, Integer sta);

        /**
         * 获取用户信息
         *
         * @param response
         */
        void onGetCustInfo(CustInfoRequestResponse response);
    }

    interface PersonalFriendDetailPresenter extends BasePresenter {
        /**
         * 获取用户等级和星级
         *
         * @param custcode
         * @param submitcode
         */
        void getCustLevel(String custcode, String submitcode);

        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode);

        void unRegisterSubscribe();
    }
}
