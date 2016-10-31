package com.yzdsmart.Dingdingwen.main;

import com.tencent.TIMConversation;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/30.
 */
public interface MainContract {
    interface MainView extends BaseView<MainPresenter> {
        /**
         * TLS账号登陆成功
         */
        void chatLoginSuccess();

        /**
         * im sdk登录成功
         */
        void imSDKLoginSuccess();

        /**
         * 腾讯云通信下线
         */
        void onIMOffline();

        /**
         * 初始化界面或刷新界面
         */
        void initConversations(List<TIMConversation> conversationList);

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
    }

    interface MainPresenter extends BasePresenter {
        /**
         * 用户聊天登录
         *
         * @param im_name
         * @param im_pwd
         */
        void chatLogin(String im_name, String im_pwd);

        /**
         * im sdk 登录
         */
        void imSDKLogin();

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
    }
}
