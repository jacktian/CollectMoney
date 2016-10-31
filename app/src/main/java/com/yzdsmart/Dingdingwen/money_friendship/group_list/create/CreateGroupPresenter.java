package com.yzdsmart.Dingdingwen.money_friendship.group_list.create;

import android.content.Context;

import com.tencent.TIMGroupAddOpt;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/8/31.
 */
public class CreateGroupPresenter implements CreateGroupContract.CreateGroupPresenter {
    private Context context;
    private CreateGroupContract.CreateGroupView mView;

    public CreateGroupPresenter(Context context, CreateGroupContract.CreateGroupView mView) {
        this.context = context;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void createGroup(String name, String type, List<String> members, TIMValueCallBack<String> callBack) {
        List<TIMGroupMemberInfo> memberinfos = new ArrayList<>();
        for (String member : members) {
            TIMGroupMemberInfo newMember = new TIMGroupMemberInfo();
            newMember.setUser(member);
            memberinfos.add(newMember);
        }
        TIMGroupManager.CreateGroupParam groupGroupParam = TIMGroupManager.getInstance().new CreateGroupParam();
        groupGroupParam.setGroupName(name);
        groupGroupParam.setAddOption(TIMGroupAddOpt.TIM_GROUP_ADD_ANY);
        groupGroupParam.setMembers(memberinfos);
        groupGroupParam.setGroupType(type);
        TIMGroupManager.getInstance().createGroup(groupGroupParam, callBack);
    }
}
