package com.yzdsmart.Collectmoney.money_friendship.group_list;

import android.content.Context;
import android.util.Log;

import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupSearchSucc;
import com.tencent.TIMValueCallBack;

/**
 * Created by YZD on 2016/8/30.
 */
public class SearchGroupPresenter implements SearchGroupContract.SearchGroupPresenter {
    private Context context;
    private SearchGroupContract.SearchGroupView mView;

    public SearchGroupPresenter(Context context, SearchGroupContract.SearchGroupView mView) {
        this.context = context;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void searchGroupByName(String key) {
        long flag = 0;
        flag |= TIMGroupManager.TIM_GET_GROUP_BASE_INFO_FLAG_NAME;
        flag |= TIMGroupManager.TIM_GET_GROUP_BASE_INFO_FLAG_OWNER_UIN;

        TIMGroupManager.getInstance().searchGroup(key, flag, null, 0, 30, new TIMValueCallBack<TIMGroupSearchSucc>() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess(TIMGroupSearchSucc timGroupSearchSucc) {
                if (mView == null) return;
                mView.showGroupInfo(timGroupSearchSucc.getInfoList());
            }
        });
    }
}
