package com.yzdsmart.Dingdingwen.tecent_im.bean;

import android.content.Context;
import android.os.Bundle;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.chat.ChatActivity;

/**
 * 好友或群聊的会话
 */
public class NormalConversation extends Conversation {

    private TIMConversation conversation;

    //最后一条消息
    private Message lastMessage;

    public NormalConversation() {
    }

    public NormalConversation(TIMConversation conversation) {
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();
        if (type == TIMConversationType.Group) {
            name = GroupInfo.getInstance().getGroupName(identify);
            if (name.equals("")) name = identify;
        } else {
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            name = profile == null ? identify : profile.getName();
        }
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getAvatar() {
        switch (type) {
            case C2C:
                return R.mipmap.tecent_head_other;
            case Group:
                return R.mipmap.tecent_head_group;
        }
        return 0;
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("identify", identify);
        bundle.putSerializable("type", type);
        ((BaseActivity) context).openActivity(ChatActivity.class, bundle, 0);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary() {
        if (lastMessage == null) return "";
        return lastMessage.getSummary();
    }

    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum() {
        if (conversation == null) return 0;
        return conversation.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage() {
        if (conversation != null) {
            conversation.setReadMessage();
        }
    }

    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType() {
        return conversation.getType();
    }
}
