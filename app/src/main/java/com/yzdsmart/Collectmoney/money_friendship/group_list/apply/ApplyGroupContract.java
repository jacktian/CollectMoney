package com.yzdsmart.Collectmoney.money_friendship.group_list.apply;

import com.tencent.TIMCallBack;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/31.
 */
public class ApplyGroupContract {
    interface ApplyGroupView extends BaseView<ApplyGroupPresenter> {

    }

    interface ApplyGroupPresenter extends BasePresenter {
        /**
         * 申请加入群
         *
         * @param groupId  群组ID
         * @param reason   申请理由
         * @param callBack 回调
         */
        void applyJoinGroup(String groupId, String reason, TIMCallBack callBack);
    }
}
