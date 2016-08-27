package com.yzdsmart.Collectmoney.money_friendship;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.bean.FriendsRequestResponse;
import com.yzdsmart.Collectmoney.http.RequestListener;

/**
 * Created by YZD on 2016/8/27.
 */
public class MoneyFriendshipPresenter implements MoneyFriendshipContract.MoneyFriendshipPresenter {
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
    public void getFriendsList(String submitCode, String custCode, Long timeStampNow, Integer startIndex, Integer currentStandardSequence, Integer pageSize) {
        mModel.getFriendsList(submitCode, custCode, timeStampNow, startIndex, currentStandardSequence, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                FriendsRequestResponse response = (FriendsRequestResponse) result;
                if (null != response.getFriends()) {
                    mView.onGetFriendsList(response.getFriends());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
