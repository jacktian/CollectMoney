package com.yzdsmart.Collectmoney.money_friendship.friend_list.profile;

import android.content.Context;

import com.tencent.TIMAddFriendRequest;
import com.tencent.TIMCallBack;
import com.tencent.TIMDelFriendType;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public class ProfilePresenter implements ProfileContract.ProfilePresenter {
    private Context context;
    private ProfileContract.ProfileView mView;

    public ProfilePresenter(Context context, ProfileContract.ProfileView mView) {
        this.context = context;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void setRemarkName(String identify, String name, TIMCallBack callBack) {
        TIMFriendshipManager.getInstance().setFriendRemark(identify, name, callBack);
    }

    @Override
    public void addBlackList(List<String> identify, TIMValueCallBack<List<TIMFriendResult>> callBack) {
        TIMFriendshipManager.getInstance().addBlackList(identify, callBack);
    }

    @Override
    public void delFriend(final String id) {
        List<TIMAddFriendRequest> reqList = new ArrayList<>();
        TIMAddFriendRequest req = new TIMAddFriendRequest();
        req.setIdentifier(id);
        reqList.add(req);
        TIMFriendshipManager.getInstance().delFriend(TIMDelFriendType.TIM_FRIEND_DEL_BOTH, reqList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                mView.onAddFriend(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                for (TIMFriendResult item : timFriendResults) {
                    if (item.getIdentifer().equals(id)) {
                        mView.onDelFriend(item.getStatus());
                    }
                }
            }
        });
    }
}
