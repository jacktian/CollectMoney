package com.yzdsmart.Dingdingwen.search_friend;

import android.content.Context;

import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.tecent_im.event.FriendshipEvent;

import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by YZD on 2016/9/2.
 */
public class SearchFriendPresenter implements SearchFriendContract.AddFriendPresenter, Observer {
    private Context context;
    private SearchFriendContract.AddFriendView mView;
    private SearchFriendModel mModel;

    public SearchFriendPresenter(Context context, SearchFriendContract.AddFriendView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new SearchFriendModel();
        mView.setPresenter(this);
        FriendshipEvent.getInstance().addObserver(this);
    }

    @Override
    public void searchFriendById(String identify) {
        mModel.searchFriendById(identify, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                TIMUserProfile profile = (TIMUserProfile) result;
                mView.showUserInfo(Collections.singletonList(profile));
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar(err);
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void applyAddFriend(final String identity, final SearchFriendActivity.OnApplyAddFriendListener listener) {
        mModel.applyAddFriend(identity, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<TIMFriendResult> results = (List<TIMFriendResult>) result;
                for (TIMFriendResult item : results) {
                    if (item.getIdentifer().equals(identity)) {
                        listener.callBack(item.getStatus());
                    }
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
    public void delBlackList(List<String> identify, TIMValueCallBack<List<TIMFriendResult>> callBack) {
        TIMFriendshipManager.getInstance().delBlackList(identify, callBack);
    }

    @Override
    public void unRegisterObserver() {
        FriendshipEvent.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof FriendshipEvent) {
            FriendshipEvent.NotifyCmd fcmd = (FriendshipEvent.NotifyCmd) data;
            switch (fcmd.type) {
                case ADD:
                    mView.refreshProfileList();
                    break;
            }
        }
    }
}
