package com.yzdsmart.Collectmoney.chat;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/28.
 */
public interface ChatContract {
    interface ChatView extends BaseView<ChatPresenter> {
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
    }

    interface ChatPresenter extends BasePresenter {

    }
}
