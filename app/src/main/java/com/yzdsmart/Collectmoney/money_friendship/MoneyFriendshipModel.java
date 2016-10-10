package com.yzdsmart.Collectmoney.money_friendship;

import com.tencent.TIMFriendFutureMeta;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGetFriendFutureListSucc;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.http.RequestListener;

/**
 * Created by YZD on 2016/8/27.
 */
public class MoneyFriendshipModel {

    void getUnReadFriendFuture(final RequestListener listener) {
        TIMFriendFutureMeta meta = new TIMFriendFutureMeta();
        long reqFlag = 0, futureFlags = 0;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE;
//        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_DECIDE_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE;
//        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_RECOMMEND_TYPE;
        TIMFriendshipManager.getInstance().getFutureFriends(reqFlag, futureFlags, null, meta, new TIMValueCallBack<TIMGetFriendFutureListSucc>() {

            @Override
            public void onError(int arg0, String arg1) {
                listener.onError(arg1);
            }

            @Override
            public void onSuccess(TIMGetFriendFutureListSucc arg0) {
                listener.onSuccess(arg0);
            }
        });
    }

    void unRegisterSubscribe() {
    }
}
