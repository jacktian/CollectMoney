package com.yzdsmart.Collectmoney.money_friendship.group_list.create;

import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/31.
 */
public interface CreateGroupContract {
    interface CreateGroupView extends BaseView<CreateGroupPresenter> {

    }

    interface CreateGroupPresenter extends BasePresenter {
        /**
         * 创建群
         *
         * @param name     群名称
         * @param type     群类型
         * @param members  群成员
         * @param callBack 回调
         */
        void createGroup(String name, String type, List<String> members, TIMValueCallBack<String> callBack);
    }
}
