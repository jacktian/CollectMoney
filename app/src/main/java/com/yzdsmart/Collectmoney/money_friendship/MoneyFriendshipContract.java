package com.yzdsmart.Collectmoney.money_friendship;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.Friendship;

import java.util.List;

/**
 * Created by YZD on 2016/8/27.
 */
public interface MoneyFriendshipContract {
    interface MoneyFriendshipView extends BaseView<MoneyFriendshipPresenter> {
        /**
         * 获取好友列表
         *
         * @param friends
         */
        void onGetFriendsList(List<Friendship> friends);
    }

    interface MoneyFriendshipPresenter extends BasePresenter {
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
