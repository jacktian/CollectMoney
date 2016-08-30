package com.yzdsmart.Collectmoney.chat;

import android.content.Context;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.tecent_im.event.MessageEvent;
import com.yzdsmart.Collectmoney.tecent_im.event.RefreshEvent;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by YZD on 2016/8/28.
 */
public class ChatPresenter implements ChatContract.ChatPresenter, Observer {
    private Context context;
    private ChatContract.ChatView mView;
    private ChatModel mModel;
    private TIMConversation conversation;
    private boolean isGettingMessage = false;
    private final int LAST_MESSAGE_NUM = 20;

    public ChatPresenter(Context context, ChatContract.ChatView mView, String identify, TIMConversationType type) {
        this.context = context;
        this.mView = mView;
        mModel = new ChatModel();
        mView.setPresenter(this);
        conversation = TIMManager.getInstance().getConversation(type, identify);

        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        RefreshEvent.getInstance().addObserver(this);
        getMessage(null);
        if (conversation.hasDraft()) {
            mView.showDraft(conversation.getDraft());
        }
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            TIMMessage msg = (TIMMessage) data;
            if (msg == null || msg.getConversation().getPeer().equals(conversation.getPeer()) && msg.getConversation().getType() == conversation.getType()) {
                mView.showMessage(msg);
                //当前聊天界面已读上报，用于多终端登录时未读消息数同步
                readMessages();
            }
        } else if (observable instanceof RefreshEvent) {
            mView.clearAllMessage();
            getMessage(null);
        }
    }

    @Override
    public void getMessage(TIMMessage message) {
        if (!isGettingMessage) {
            isGettingMessage = true;
            conversation.getMessage(LAST_MESSAGE_NUM, message, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    isGettingMessage = false;
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    isGettingMessage = false;
                    mView.showMessage(timMessages);
                }
            });
        }
    }

    @Override
    public void readMessages() {
        conversation.setReadMessage();
    }

    /**
     * 发送消息
     *
     * @param message 发送的消息
     */
    @Override
    public void sendMessage(final TIMMessage message) {
        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                mView.onSendMessageFail(code, desc, message);
            }

            @Override
            public void onSuccess(TIMMessage msg) {
                //发送消息成功,消息状态已在sdk中修改，此时只需更新界面
                MessageEvent.getInstance().onNewMessage(null);

            }
        });
        //message对象为发送中状态
        MessageEvent.getInstance().onNewMessage(message);
    }

    /**
     * 发送在线消息
     *
     * @param message 发送的消息
     */
    @Override
    public void sendOnlineMessage(final TIMMessage message) {
        conversation.sendOnlineMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                mView.onSendMessageFail(i, s, message);
            }

            @Override
            public void onSuccess(TIMMessage message) {

            }
        });
    }

    @Override
    public void saveDraft(TIMMessage message) {
        conversation.setDraft(null);
        if (message != null && message.getElementCount() > 0) {
            TIMMessageDraft draft = new TIMMessageDraft();
            for (int i = 0; i < message.getElementCount(); ++i) {
                draft.addElem(message.getElement(i));
            }
            conversation.setDraft(draft);
        }
    }

    @Override
    public void unRegisterObserver() {
        //注销消息监听
        MessageEvent.getInstance().deleteObserver(this);
        RefreshEvent.getInstance().deleteObserver(this);
    }
}
