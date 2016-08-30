package com.yzdsmart.Collectmoney.money_friendship;

/**
 * Created by YZD on 2016/8/27.
 */
public class MoneyFriendshipModel {
    //网络请求监听
//    private Subscriber<FriendsRequestResponse> getFriendsListSubscriber;

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
//        if (null != getFriendsListSubscriber) {
//            getFriendsListSubscriber.unsubscribe();
//        }
    }
}
