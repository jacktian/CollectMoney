package com.yzdsmart.Collectmoney.money_friendship.friend_list.add;

import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public interface AddFriendContract {
    interface AddFriendView extends BaseView<AddFriendPresenter> {
        /**
         * 添加好友结果回调
         *
         * @param status 返回状态
         */
        void onAddFriend(TIMFriendStatus status);
    }

    interface AddFriendPresenter extends BasePresenter {
        /**
         * 添加好友
         *
         * @param id      添加对象Identify
         * @param remark  备注名
         * @param group   分组
         * @param message 附加消息
         */
        void addFriend(final String id, String remark, String group, String message);

        /**
         * 移除黑名单
         *
         * @param identfiy 移除黑名单列表
         * @param callBack 回调
         */
        void delBlackList(List<String> identfiy, TIMValueCallBack<List<TIMFriendResult>> callBack);
    }
}
