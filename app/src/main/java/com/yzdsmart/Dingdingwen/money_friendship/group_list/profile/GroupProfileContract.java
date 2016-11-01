package com.yzdsmart.Dingdingwen.money_friendship.group_list.profile;

import com.tencent.TIMCallBack;
import com.tencent.TIMGroupDetailInfo;
import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/31.
 */
public interface GroupProfileContract {
    interface GroupProfileView extends BaseView<GroupProfilePresenter> {
        /**
         * 显示群资料
         *
         * @param groupInfos 群资料信息列表
         */
        void showGroupInfo(List<TIMGroupDetailInfo> groupInfos);
    }

    interface GroupProfilePresenter extends BasePresenter {
        void getGroupDetailInfo();

        /**
         * 解散群
         *
         * @param groupId  群组ID
         * @param callBack 回调
         */
        void dismissGroup(String groupId, TIMCallBack callBack);

        /**
         * 退出群
         *
         * @param groupId  群组ID
         * @param callBack 回调
         */
        void quitGroup(String groupId, TIMCallBack callBack);
    }
}
