package com.yzdsmart.Collectmoney.money_friendship;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.FriendsRequestResponse;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by YZD on 2016/8/27.
 */
public class MoneyFriendshipPresenter implements MoneyFriendshipContract.MoneyFriendshipPresenter, Observer {
    private Context context;
    private MoneyFriendshipContract.MoneyFriendshipView mView;
    private MoneyFriendshipModel mModel;

    public MoneyFriendshipPresenter(Context context, MoneyFriendshipContract.MoneyFriendshipView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new MoneyFriendshipModel();
        mView.setPresenter(this);
    }

    @Override
    public void unRegisterObserver() {

    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
