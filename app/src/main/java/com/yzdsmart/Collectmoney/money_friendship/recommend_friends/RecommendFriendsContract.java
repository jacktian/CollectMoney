package com.yzdsmart.Collectmoney.money_friendship.recommend_friends;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.Friendship;

import java.util.List;

/**
 * Created by YZD on 2016/9/25.
 */

public interface RecommendFriendsContract {
    interface RecommendFriendsView extends BaseView<RecommendFriendsPresenter> {
        /**
         * 获取系统推荐好友
         *
         * @param friendships
         */
        void onGetRecommendFriends(List<Friendship> friendships);
    }

    interface RecommendFriendsPresenter extends BasePresenter {
        /**
         * 获取系统推荐好友
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param recomNum
         */
        void getRecommendFriends(String action, String submitCode, String custCode, Integer recomNum);

        void unRegisterSubscribe();
    }
}
