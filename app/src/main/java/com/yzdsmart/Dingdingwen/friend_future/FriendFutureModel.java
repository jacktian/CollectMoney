package com.yzdsmart.Dingdingwen.friend_future;

import com.tencent.TIMFriendFutureMeta;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGetFriendFutureListSucc;
import com.tencent.TIMPageDirectionType;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.http.RequestListener;

/**
 * Created by YZD on 2016/9/2.
 */
public class FriendFutureModel {
    void getFutureFriends(int pageSize, long pendSeq, long decideSeq, long recommendSeq, final RequestListener listener) {
        TIMFriendFutureMeta meta = new TIMFriendFutureMeta();
        meta.setReqNum(pageSize);
        //设置用于分页拉取的seq
        meta.setPendencySeq(pendSeq);
        meta.setDecideSeq(decideSeq);
        meta.setRecommendSeq(recommendSeq);
        meta.setDirectionType(TIMPageDirectionType.TIM_PAGE_DIRECTION_DOWN_TYPE);
        long reqFlag = 0, futureFlags = 0;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_DECIDE_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_RECOMMEND_TYPE;
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
}
