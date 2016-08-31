package com.yzdsmart.Collectmoney.money_friendship.group_list.profile;

import android.content.Context;

import com.tencent.TIMCallBack;
import com.tencent.TIMGroupDetailInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMValueCallBack;

import java.util.List;

/**
 * Created by YZD on 2016/8/31.
 */
public class GroupProfilePresenter implements GroupProfileContract.GroupProfilePresenter, TIMValueCallBack<List<TIMGroupDetailInfo>> {
    private Context context;
    private GroupProfileContract.GroupProfileView mView;
    private boolean isInGroup;
    private List<String> groupIds;

    public GroupProfilePresenter(Context context, GroupProfileContract.GroupProfileView mView, boolean isInGroup, List<String> groupIds) {
        this.context = context;
        this.mView = mView;
        this.isInGroup = isInGroup;
        this.groupIds = groupIds;
        mView.setPresenter(this);
    }

    @Override
    public void getGroupDetailInfo() {
        if (isInGroup) {
            TIMGroupManager.getInstance().getGroupDetailInfo(groupIds, this);
        } else {
            TIMGroupManager.getInstance().getGroupPublicInfo(groupIds, this);
        }
    }

    @Override
    public void dismissGroup(String groupId, TIMCallBack callBack) {
        TIMGroupManager.getInstance().deleteGroup(groupId, callBack);
    }

    @Override
    public void quitGroup(String groupId, TIMCallBack callBack) {
        TIMGroupManager.getInstance().quitGroup(groupId, callBack);
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
        mView.showGroupInfo(timGroupDetailInfos);
    }
}
