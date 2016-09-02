package com.yzdsmart.Collectmoney.friend_future;

import com.tencent.TIMFriendFutureItem;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public interface FriendFutureContract {
    interface FriendFutureView extends BaseView<FriendFuturePresenter> {
        void onGetFutureFriends(List<TIMFriendFutureItem> futureItems, long p, long d, long r);

        void refreshFriendFuture();
    }

    interface FriendFuturePresenter extends BasePresenter {
        void getFutureFriends(int pageSize, long pendSeq, long decideSeq, long recommendSeq);

        void unRegisterObserver();
    }
}
