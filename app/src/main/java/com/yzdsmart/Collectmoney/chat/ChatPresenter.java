package com.yzdsmart.Collectmoney.chat;

import android.content.Context;

/**
 * Created by YZD on 2016/8/28.
 */
public class ChatPresenter implements ChatContract.ChatPresenter {
    private Context context;
    private ChatContract.ChatView mView;
    private ChatModel mModel;

    public ChatPresenter(Context context, ChatContract.ChatView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new ChatModel();
        mView.setPresenter(this);
    }
}
