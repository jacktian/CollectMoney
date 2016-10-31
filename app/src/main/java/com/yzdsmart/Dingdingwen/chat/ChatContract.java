package com.yzdsmart.Dingdingwen.chat;

import android.support.annotation.Nullable;

import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/28.
 */
public interface ChatContract {
    interface ChatView extends BaseView<ChatPresenter> {

        /**
         * 显示消息
         */
        void showMessage(TIMMessage message);

        /**
         * 显示消息
         */
        void showMessage(List<TIMMessage> messages);

        /**
         * 清除所有消息(离线恢复),并等待刷新
         */
        void clearAllMessage();

        /**
         * 发送消息成功
         *
         * @param message 返回的消息
         */
        void onSendMessageSuccess(TIMMessage message);

        /**
         * 发送消息失败
         *
         * @param code    返回码
         * @param desc    返回描述
         * @param message 发送的消息
         */
        void onSendMessageFail(int code, String desc, TIMMessage message);

        /**
         * 发送图片消息
         */
        void sendImage();


        /**
         * 发送照片消息
         */
        void sendPhoto();


        /**
         * 发送文字消息
         */
        void sendText();

        /**
         * 发送文件
         */
        void sendFile();


        /**
         * 开始发送语音消息
         */
        void startSendVoice();


        /**
         * 结束发送语音消息
         */
        void endSendVoice();


        /**
         * 发送小视频消息
         *
         * @param fileName 文件名
         */
        void sendVideo(String fileName);

        /**
         * 正在发送
         */
        void sending();

        /**
         * 显示草稿
         */
        void showDraft(TIMMessageDraft draft);
    }

    interface ChatPresenter extends BasePresenter {
        /**
         * 获取消息
         *
         * @param message 最后一条消息
         */
        void getMessage(TIMMessage message);

        /**
         * 设置会话为已读
         */
        void readMessages();

        /**
         * 发送消息
         *
         * @param message 发送的消息
         */
        void sendMessage(final TIMMessage message);

        /**
         * 发送在线消息
         *
         * @param message 发送的消息
         */
        void sendOnlineMessage(TIMMessage message);

        /**
         * 保存草稿
         *
         * @param message 消息数据
         */
        void saveDraft(TIMMessage message);

        /**
         * 根据腾讯云通信账号获取用户cust code
         *
         * @param code
         * @param submitcode
         * @param action
         */
        void getCustCode(String code, String submitcode, String action);

        void unRegisterObserver();
    }
}
