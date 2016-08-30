package com.yzdsmart.Collectmoney.money_friendship.group_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
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
import butterknife.OnFocusChange;
import butterknife.Optional;

/**
 * Created by jacks on 2016/8/29.
 */
public class GroupListFragment extends BaseFragment implements GroupListContract.GroupListView {
    @Nullable
    @BindView(R.id.list)
    ListView listView;
    @Nullable
    @BindView(R.id.search_filter)
    EditText searchFilterET;

    private String type;
    private List<ProfileSummary> list;
    private List<ProfileSummary> showList;
    private ProfileSummaryAdapter adapter;

    private GroupListContract.GroupListPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GroupListPresenter(getActivity(), this);

        showList = new ArrayList<ProfileSummary>();

        type = "Public";
        list = GroupInfo.getInstance().getGroupListByType(type);
        showList.addAll(list);
        adapter = new ProfileSummaryAdapter(getActivity(), R.layout.tecent_item_profile_summary, showList);
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

    @Optional
    @OnFocusChange({R.id.search_filter})
    void onFocusChange(View view, boolean focus) {
        if (!focus) {
            switch (view.getId()) {
                case R.id.search_filter:
                    if ("".equals(searchFilterET.getText().toString())) {
                        showList.clear();
                        showList.addAll(list);
                    } else {
                        showList.clear();
                        for (ProfileSummary summary : list) {
                            if (summary.getName().contains(searchFilterET.getText().toString())) {
                                showList.add(summary);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
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
        showList.clear();
        Iterator<ProfileSummary> it = list.iterator();
        while (it.hasNext()) {
            ProfileSummary item = it.next();
            if (item.getIdentify().equals(groupId)) {
                it.remove();
                showList.addAll(list);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void addGroup(TIMGroupCacheInfo info) {
        if (info != null && info.getGroupInfo().getGroupType().equals(type)) {
            showList.clear();
            GroupProfile profile = new GroupProfile(info);
            list.add(profile);
            showList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateGroup(TIMGroupCacheInfo info) {
        delGroup(info.getGroupInfo().getGroupId());
        addGroup(info);
    }
}
