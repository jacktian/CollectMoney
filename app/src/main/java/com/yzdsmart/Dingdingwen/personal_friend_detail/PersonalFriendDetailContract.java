package com.yzdsmart.Dingdingwen.personal_friend_detail;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;

import java.util.List;

/**
 * Created by YZD on 2016/8/29.
 */
public interface PersonalFriendDetailContract {
    interface PersonalFriendDetailView extends BaseView<PersonalFriendDetailPresenter> {

        /**
         * 获取用户信息
         *
         * @param response
         */
        void onGetCustInfo(CustInfoRequestResponse response);

        /**
         * 获取用户详细信息
         *
         * @param response
         */
        void onGetCustDetailInfo(CustDetailInfoRequestResponse response);

        /**
         * 获取个人的图片列表
         *
         * @param galleyInfos
         */
        void onGetPersonalGalley(List<GalleyInfo> galleyInfos);

        /**
         * 更新好友关系
         */
        void refreshFriendship();

        /**
         * 设置好友备注
         *
         * @param remark
         */
        void onRemarkFriend(String remark);
    }

    interface PersonalFriendDetailPresenter extends BasePresenter {

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

        /**
         * 获取用户详细信息
         *
         * @param actioncode
         * @param submitCode
         * @param custCode
         * @param selfCustCode
         * @return
         */
        void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode);

        /**
         * 添加好友
         *
         * @param identify
         */
        void addFriend(String identify);

        /**
         * 删除好友
         *
         * @param identify
         */
        void deleteFriend(String identify);

        /**
         * 设置好友备注
         *
         * @param identify
         * @param remark
         */
        void remarkFriend(String identify, String remark);

        void unRegisterObserver();

        void unRegisterSubscribe();
    }
}
