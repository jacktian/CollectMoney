package com.yzdsmart.Collectmoney.money_friendship.group_list;

import com.tencent.TIMGroupCacheInfo;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/30.
 */
public interface GroupListContract {
    interface GroupListView extends BaseView<GroupListPresenter> {
        void delGroup(String groupId);

        void addGroup(TIMGroupCacheInfo info);

        void updateGroup(TIMGroupCacheInfo info);
    }

    interface GroupListPresenter extends BasePresenter {

        void unRegisterObserver();
    }
}
