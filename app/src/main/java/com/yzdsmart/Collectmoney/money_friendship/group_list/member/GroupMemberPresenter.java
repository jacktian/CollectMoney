package com.yzdsmart.Collectmoney.money_friendship.group_list.member;

import android.content.Context;

import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMValueCallBack;

import java.util.List;

/**
 * Created by YZD on 2016/8/31.
 */
public class GroupMemberPresenter implements GroupMemberContract.GroupMemberPresenter {
    private Context context;
    private GroupMemberContract.GroupMemberView mView;

    public GroupMemberPresenter(Context context, GroupMemberContract.GroupMemberView mView) {
        this.context = context;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void inviteGroup(String groupId, List<String> members, TIMValueCallBack<List<TIMGroupMemberResult>> callBack) {
        TIMGroupManager.getInstance().inviteGroupMember(groupId, members, callBack);
    }
}
