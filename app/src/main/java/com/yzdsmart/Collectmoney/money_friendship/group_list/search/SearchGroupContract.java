package com.yzdsmart.Collectmoney.money_friendship.group_list.search;

import com.tencent.TIMGroupDetailInfo;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/30.
 */
public interface SearchGroupContract {
    interface SearchGroupView extends BaseView<SearchGroupPresenter> {

        /**
         * 显示群资料
         *
         * @param groupInfos 群资料信息列表
         */
        void showGroupInfo(List<TIMGroupDetailInfo> groupInfos);
    }

    interface SearchGroupPresenter extends BasePresenter {
        /**
         * 按照群名称搜索群
         *
         * @param key 关键字
         */
        void searchGroupByName(String key);
    }
}
