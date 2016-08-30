package com.yzdsmart.Collectmoney.money_friendship.friend_list;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.Friendship;

import java.util.List;

/**
 * Created by YZD on 2016/8/30.
 */
public interface FriendListContract {
    interface FriendListView extends BaseView<FriendListPresenter>{
        /**
         * 获取好友列表
         *
         * @param friends
         */
        void onGetFriendsList(List<Friendship> friends);
    }
    interface FriendListPresenter extends BasePresenter{
        /**
         * 获取好友列表
         *
         * @param submitCode
         * @param custCode
         * @param timeStampNow
         * @param startIndex
         * @param currentStandardSequence
         * @param pageSize
         */
        void getFriendsList(String submitCode, String custCode, Long timeStampNow, Integer startIndex, Integer currentStandardSequence, Integer pageSize);

        void unRegisterSubscribe();
    }
}
