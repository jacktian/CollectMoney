package com.yzdsmart.Dingdingwen.money_friendship.group_list.apply;

import android.content.Context;

import com.tencent.TIMCallBack;
import com.tencent.TIMGroupManager;

/**
 * Created by YZD on 2016/8/31.
 */
public class ApplyGroupPresenter implements ApplyGroupContract.ApplyGroupPresenter {
    private Context context;
    private ApplyGroupContract.ApplyGroupView mView;

    public ApplyGroupPresenter(Context context, ApplyGroupContract.ApplyGroupView mView) {
        this.context = context;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void applyJoinGroup(String groupId, String reason, TIMCallBack callBack) {
        TIMGroupManager.getInstance().applyJoinGroup(groupId, reason, callBack);
    }
}
