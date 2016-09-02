package com.yzdsmart.Collectmoney.tecent_im.bean;

import android.content.Context;
import android.os.Bundle;

import com.tencent.TIMConversationType;
import com.tencent.TIMUserProfile;
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.R;

/**
 * 好友资料
 */
public class FriendProfile implements ProfileSummary {

    private TIMUserProfile profile;
    private boolean isSelected;

    public FriendProfile(TIMUserProfile profile) {
        this.profile = profile;
    }

    /**
     * 获取头像资源
     */
    @Override
    public int getAvatarRes() {
        return R.mipmap.tecent_head_other;
    }

    /**
     * 获取头像地址
     */
    @Override
    public String getAvatarUrl() {
        return null;
    }

    /**
     * 获取名字
     */
    @Override
    public String getName() {
        if (!profile.getRemark().equals("")) {
            return profile.getRemark();
        } else if (!profile.getNickName().equals("")) {
            return profile.getNickName();
        }
        return profile.getIdentifier();
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * 显示详情
     *
     * @param context 上下文
     */
    @Override
    public void onClick(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("identify", profile.getIdentifier());
        bundle.putSerializable("type", TIMConversationType.C2C);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 获取用户ID
     */
    @Override
    public String getIdentify() {
        return profile.getIdentifier();
    }

    /**
     * 获取用户备注名
     */
    public String getRemark() {
        return profile.getRemark();
    }

    /**
     * 获取好友分组
     */
    public String getGroupName() {
        if (profile.getFriendGroups().size() == 0) {
            return App.getAppInstance().getString(R.string.default_group_name);
        } else {
            return profile.getFriendGroups().get(0);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "profile:" + profile +
                ", isSelected:" + isSelected +
                '}';
    }
}
