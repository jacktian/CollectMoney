package com.yzdsmart.Dingdingwen.money_friendship.group_list.member;

import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/31.
 */
public interface GroupMemberContract {
    interface GroupMemberView extends BaseView<GroupMemberPresenter> {

    }

    interface GroupMemberPresenter extends BasePresenter {
        /**
         * 邀请入群
         *
         * @param groupId  群组ID
         * @param members  邀请的好友
         * @param callBack 回调
         */
        void inviteGroup(String groupId, List<String> members, TIMValueCallBack<List<TIMGroupMemberResult>> callBack);
    }
}