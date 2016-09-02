package com.yzdsmart.Collectmoney.add_friend;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.http.RequestListener;

/**
 * Created by YZD on 2016/9/2.
 */
public class AddFriendModel {
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
}
