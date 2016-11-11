package com.yzdsmart.Dingdingwen.money_friendship.group_list.member;

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
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.tecent_im.adapters.ProfileSummaryAdapter;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupMemberProfile;
import com.yzdsmart.Dingdingwen.tecent_im.bean.ProfileSummary;

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

    private static final String TAG = "GroupMemberActivity";

    private GroupMemberContract.GroupMemberPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText("群成员");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleLeftOpeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });

        new GroupMemberPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
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
