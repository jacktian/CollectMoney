package com.yzdsmart.Collectmoney.money_friendship.group_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.tencent.TIMGroupCacheInfo;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.adapters.ProfileSummaryAdapter;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupInfo;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupProfile;
import com.yzdsmart.Collectmoney.tecent_im.bean.ProfileSummary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jacks on 2016/8/29.
 */
public class GroupListFragment extends BaseFragment implements GroupListContract.GroupListView {
    @Nullable
    @BindView(R.id.list)
    ListView listView;

    private String type;
    private List<ProfileSummary> list;
    private ProfileSummaryAdapter adapter;
    private final int CREATE_GROUP_CODE = 100;

    private GroupListContract.GroupListPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GroupListPresenter(getActivity(), this);

        type = "Public";
        list = GroupInfo.getInstance().getGroupListByType(type);
        adapter = new ProfileSummaryAdapter(getActivity(), R.layout.tecent_item_profile_summary, list);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_group_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        mPresenter.unRegisterObserver();
        super.onDestroy();
    }

    @Override
    public void setPresenter(GroupListContract.GroupListPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void delGroup(String groupId) {
        Iterator<ProfileSummary> it = list.iterator();
        while (it.hasNext()) {
            ProfileSummary item = it.next();
            if (item.getIdentify().equals(groupId)) {
                it.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void addGroup(TIMGroupCacheInfo info) {
        if (info != null && info.getGroupInfo().getGroupType().equals(type)) {
            GroupProfile profile = new GroupProfile(info);
            list.add(profile);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateGroup(TIMGroupCacheInfo info) {
        delGroup(info.getGroupInfo().getGroupId());
        addGroup(info);
    }
}
