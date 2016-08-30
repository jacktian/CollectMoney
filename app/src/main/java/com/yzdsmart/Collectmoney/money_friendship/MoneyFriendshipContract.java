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

    }

    interface MoneyFriendshipPresenter extends BasePresenter {
        void unRegisterObserver();

        void unRegisterSubscribe();
    }
}
