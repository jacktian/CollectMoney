package com.yzdsmart.Collectmoney.search_friend;

import com.tencent.TIMAddFriendRequest;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.http.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public class SearchFriendModel {
    /**
     * 按照ID搜索好友
     *
     * @param identify id
     */
    void searchFriendById(String identify, final RequestListener listener) {
        TIMFriendshipManager.getInstance().searchFriend(identify, new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }

            @Override
            public void onSuccess(TIMUserProfile profile) {
                listener.onSuccess(profile);
            }
        });
    }

    /**
     * 申请添加好友
     *
     * @param identity
     * @param listener
     */
    void applyAddFriend(String identity, final RequestListener listener) {
        List<TIMAddFriendRequest> users = new ArrayList<TIMAddFriendRequest>();
        TIMAddFriendRequest req = new TIMAddFriendRequest();
        req.setIdentifier(identity);
        users.add(req);
        TIMFriendshipManager.getInstance().addFriend(users, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> results) {
                listener.onSuccess(results);
            }
        });
    }
}
