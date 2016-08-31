package com.yzdsmart.Collectmoney.money_friendship.group_list.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.adapters.ProfileSummaryAdapter;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupMemberProfile;
import com.yzdsmart.Collectmoney.tecent_im.bean.ProfileSummary;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class GroupMemberActivity extends BaseActivity implements GroupMemberContract.GroupMemberView, TIMValueCallBack<List<TIMGroupMemberInfo>> {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_left_operation_layout)
    FrameLayout titleLeftOpeLayout;

    ProfileSummaryAdapter adapter;
    List<ProfileSummary> list = new ArrayList<>();
    ListView listView;
    String groupId, type;

    private GroupMemberContract.GroupMemberPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText("群成员");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        titleLeftOpeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });

        new GroupMemberPresenter(this, this);

        listView = (ListView) findViewById(R.id.list);
        adapter = new ProfileSummaryAdapter(this, R.layout.tecent_item_profile_summary, list);
        listView.setAdapter(adapter);
        TIMGroupManager.getInstance().getGroupMembers(groupId, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_group_member;
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
        list.clear();
        if (timGroupMemberInfos == null) return;
        for (TIMGroupMemberInfo item : timGroupMemberInfos) {
            list.add(new GroupMemberProfile(item));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void setPresenter(GroupMemberContract.GroupMemberPresenter presenter) {
        mPresenter = presenter;
    }
}
