package com.yzdsmart.Dingdingwen.money_friendship.friend_list.profile;

import com.tencent.TIMCallBack;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/9/2.
 */
public interface ProfileContract {
    interface ProfileView extends BaseView<ProfilePresenter> {
        /**
         * 添加好友结果回调
         *
         * @param status 返回状态
         */
        void onAddFriend(TIMFriendStatus status);

        /**
         * 删除好友结果回调
         *
         * @param status 返回状态
         */
        void onDelFriend(TIMFriendStatus status);

        /**
         * 修改好友分组回调
         *
         * @param status    返回状态
         * @param groupName 分组名
         */
        void onChangeGroup(TIMFriendStatus status, String groupName);
    }

    interface ProfilePresenter extends BasePresenter {
        /**
         * 设置好友备注
         *
         * @param identify 好友identify
         * @param name     备注名
         * @param callBack 回调
         */
        void setRemarkName(String identify, String name, TIMCallBack callBack);

        /**
         * 加入黑名单
         *
         * @param identify 加黑名单列表
         * @param callBack 回调
         */
        void addBlackList(List<String> identify, TIMValueCallBack<List<TIMFriendResult>> callBack);

        /**
         * 删除好友
         *
         * @param id 删除对象Identify
         */
        void delFriend(String id);
    }
}
