package com.yzdsmart.Collectmoney.money_friendship;

import android.content.Context;
import android.util.Log;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupPendencyGetParam;
import com.tencent.TIMGroupPendencyListGetSucc;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.FriendsRequestResponse;
import com.yzdsmart.Collectmoney.tecent_im.event.FriendshipEvent;
import com.yzdsmart.Collectmoney.tecent_im.event.GroupEvent;
import com.yzdsmart.Collectmoney.tecent_im.event.MessageEvent;
import com.yzdsmart.Collectmoney.tecent_im.event.RefreshEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by YZD on 2016/8/27.
 */
public class MoneyFriendshipPresenter implements MoneyFriendshipContract.MoneyFriendshipPresenter, Observer {
    private Context context;
    private MoneyFriendshipContract.MoneyFriendshipView mView;
    private MoneyFriendshipModel mModel;

    public MoneyFriendshipPresenter(Context context, MoneyFriendshipContract.MoneyFriendshipView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new MoneyFriendshipModel();
        mView.setPresenter(this);

        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
        //注册群关系监听
        GroupEvent.getInstance().addObserver(this);
    }

    @Override
    public void getConversation() {
        List<TIMConversation> list = new ArrayList<TIMConversation>();
        //获取会话个数
        long cnt = TIMManager.getInstance().getConversationCount();
        //遍历会话列表
        for (long i = 0; i < cnt; ++i) {
            //根据索引获取会话
            final TIMConversation conversation = TIMManager.getInstance().getConversationByIndex(i);
            if (conversation.getType() == TIMConversationType.System) continue;
            list.add(conversation);
            conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    ((BaseActivity) context).showSnackbar("获取消息失败:" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        mView.updateConversation(timMessages.get(0));
                    }
                }
            });
        }
        mView.initView(list);
    }

    /**
     * 获取群管理最有一条消息,和未读消息数
     * 包括：加群等已决和未决的消息
     */
    @Override
    public void getGroupManageLastMessage() {
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setNumPerPage(1);
        param.setTimestamp(0);
        TIMGroupManager.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int i, String s) {
                System.out.println("onError code" + i + " msg " + s);
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                if (mView != null && timGroupPendencyListGetSucc.getPendencies().size() > 0) {
                    mView.onGetGroupManageLastMessage(timGroupPendencyListGetSucc.getPendencies().get(0),
                            timGroupPendencyListGetSucc.getPendencyMeta().getUnReadCount());
                }
            }
        });
    }

    @Override
    public void unRegisterObserver() {
        //解除消息监听
        MessageEvent.getInstance().deleteObserver(this);
        //解除刷新监听
        RefreshEvent.getInstance().deleteObserver(this);
        //解除好友关系链监听
        FriendshipEvent.getInstance().deleteObserver(this);
        //解除群关系监听
        GroupEvent.getInstance().deleteObserver(this);
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            TIMMessage msg = (TIMMessage) data;
            mView.updateConversation(msg);
        } else if (observable instanceof FriendshipEvent) {
            FriendshipEvent.NotifyCmd fcmd = (FriendshipEvent.NotifyCmd) data;
            switch (fcmd.type) {
                case REFRESH:
                case DEL:
                    mView.refreshConversation();
                    break;
            }
        } else if (observable instanceof GroupEvent) {
            GroupEvent.NotifyCmd gcmd = (GroupEvent.NotifyCmd) data;
            switch (gcmd.type) {
                case UPDATE:
                case ADD:
                    mView.updateGroupInfo((TIMGroupCacheInfo) gcmd.data);
                    break;
                case DEL:
                    mView.removeConversation((String) gcmd.data);
                    break;
            }
        } else if (observable instanceof RefreshEvent) {
            mView.refreshConversation();
        }
    }
}
