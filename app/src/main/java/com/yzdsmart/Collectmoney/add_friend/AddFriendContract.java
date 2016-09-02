package com.yzdsmart.Collectmoney.add_friend;

import com.tencent.TIMUserProfile;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public interface AddFriendContract {
    interface AddFriendView extends BaseView<AddFriendPresenter> {
        /**
         * 显示好友信息
         *
         * @param users 好友资料列表
         */
        void showUserInfo(List<TIMUserProfile> users);
    }

    interface AddFriendPresenter extends BasePresenter {
        /**
         * 按照ID搜索好友
         *
         * @param identify id
         */
        void searchFriendById(String identify);
    }
}
