package com.yzdsmart.Collectmoney.money_friendship;

import com.tencent.TIMConversation;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.Friendship;

import java.util.List;

/**
 * Created by YZD on 2016/8/27.
 */
public interface MoneyFriendshipContract {
    interface MoneyFriendshipView extends BaseView<MoneyFriendshipPresenter> {
        /**
         * 初始化界面或刷新界面
         */
        void initView(List<TIMConversation> conversationList);

        /**
         * 更新最新消息显示
         *
         * @param message 最后一条消息
         */
        void updateConversation(TIMMessage message);

        /**
         * 获取群管理最后一条系统消息的回调
         *
         * @param message     最后一条消息
         * @param unreadCount 未读数
         */
        void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount);

        /**
         * 删除会话
         */
        void removeConversation(String identify);

        /**
         * 更新群信息
         */
        void updateGroupInfo(TIMGroupCacheInfo info);

        /**
         * 刷新界面
         */
        void refreshConversation();
    }

    interface MoneyFriendshipPresenter extends BasePresenter {
        /**
         * 获取会话
         */
        void getConversation();

        /**
         * 获取群管理最有一条消息,和未读消息数
         * 包括：加群等已决和未决的消息
         */
        void getGroupManageLastMessage();

        void unRegisterObserver();

        void unRegisterSubscribe();
    }
}
