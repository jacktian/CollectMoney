package com.yzdsmart.Collectmoney.search_friend;

import com.tencent.TIMFriendResult;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public interface SearchFriendContract {
    interface AddFriendView extends BaseView<AddFriendPresenter> {
        /**
         * 显示好友信息
         *
         * @param users 好友资料列表
         */
        void showUserInfo(List<TIMUserProfile> users);

        void refreshProfileList();
    }

    interface AddFriendPresenter extends BasePresenter {
        /**
         * 按照ID搜索好友
         *
         * @param identify id
         */
        void searchFriendById(String identify);

        /**
         * 申请加为好友
         *
         * @param identity
         */
        void applyAddFriend(String identity, SearchFriendActivity.OnApplyAddFriendListener listener);

        /**
         * 移除黑名单
         *
         * @param identify 移除黑名单列表
         * @param callBack 回调
         */
        void delBlackList(List<String> identify, TIMValueCallBack<List<TIMFriendResult>> callBack);

        void unRegisterObserver();
    }
}
