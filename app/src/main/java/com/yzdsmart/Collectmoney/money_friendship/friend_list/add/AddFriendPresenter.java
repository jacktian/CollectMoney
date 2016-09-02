package com.yzdsmart.Collectmoney.money_friendship.friend_list.add;

import android.content.Context;
import android.util.Log;

import com.tencent.TIMAddFriendRequest;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public class AddFriendPresenter implements AddFriendContract.AddFriendPresenter {
    private Context context;
    private AddFriendContract.AddFriendView mView;

    public AddFriendPresenter(Context context, AddFriendContract.AddFriendView mView) {
        this.context = context;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void addFriend(final String id, String remark, String group, String message) {
        List<TIMAddFriendRequest> reqList = new ArrayList<>();
        TIMAddFriendRequest req = new TIMAddFriendRequest();
        req.setAddWording(message);
        req.setIdentifier(id);
        req.setRemark(remark);
        req.setFriendGroup(group);
        reqList.add(req);
        TIMFriendshipManager.getInstance().addFriend(reqList, new TIMValueCallBack<List<TIMFriendResult>>() {

            @Override
            public void onError(int arg0, String arg1) {
                mView.onAddFriend(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> arg0) {
                for (TIMFriendResult item : arg0) {
                    if (item.getIdentifer().equals(id)) {
                        mView.onAddFriend(item.getStatus());
                    }
                }
            }

        });
    }

    @Override
    public void delBlackList(List<String> identfiy, TIMValueCallBack<List<TIMFriendResult>> callBack) {
        TIMFriendshipManager.getInstance().delBlackList(identfiy, callBack);
    }
}
