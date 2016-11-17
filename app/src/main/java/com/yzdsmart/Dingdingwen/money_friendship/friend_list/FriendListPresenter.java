package com.yzdsmart.Dingdingwen.money_friendship.friend_list;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.FriendsRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/8/30.
 */
public class FriendListPresenter implements FriendListContract.FriendListPresenter {
    private Context context;
    private FriendListContract.FriendListView mView;
    private FriendListModel mModel;

    public FriendListPresenter(Context context, FriendListContract.FriendListView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new FriendListModel();
        mView.setPresenter(this);
    }

    @Override
    public void getFriendsList(String submitCode, String custCode, Long timeStampNow, Integer startIndex, Integer currentStandardSequence, Integer pageSize, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getFriendsList(submitCode, custCode, timeStampNow, startIndex, currentStandardSequence, pageSize, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                FriendsRequestResponse response = (FriendsRequestResponse) result;
                if (response.getFriendNum() > 0 && null != response.getFriends()) {
                    mView.onGetFriendsList(response.getTimeStampNow(), response.getStartIndex(), response.getCurrentStandardSequence(), response.getFriends());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
                if (err.contains("HTTP 401 Unauthorized")) {
                    MainActivity.getInstance().refreshAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
