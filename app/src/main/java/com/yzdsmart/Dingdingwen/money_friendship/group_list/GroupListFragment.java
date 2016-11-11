package com.yzdsmart.Dingdingwen.money_friendship.group_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.TIMGroupCacheInfo;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.BaseFragment;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.money_friendship.group_list.profile.GroupProfileActivity;
import com.yzdsmart.Dingdingwen.tecent_im.adapters.ProfileSummaryAdapter;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupProfile;
import com.yzdsmart.Dingdingwen.tecent_im.bean.ProfileSummary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnEditorAction;
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

    private static final String TAG = "GroupListFragment";

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
        showList.clear();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("identify", showList.get(i).getIdentify());
                ((BaseActivity) getActivity()).openActivity(GroupProfileActivity.class, bundle, 0);
            }
        });
    }

    @Optional
    @OnEditorAction({R.id.search_filter})
    boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
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
