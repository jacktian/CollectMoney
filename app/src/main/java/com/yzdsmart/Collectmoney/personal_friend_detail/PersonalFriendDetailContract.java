package com.yzdsmart.Collectmoney.personal_friend_detail;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;

import java.util.List;

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

        void onGetPersonalGalley(List<GalleyInfo> galleyInfos);
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

        /**
         * 获取个人的图片列表
         *
         * @param action
         * @param submitCode
         * @param custCode
         */
        void getPersonalGalley(String action, String submitCode, String custCode);

        void unRegisterSubscribe();
    }
}
