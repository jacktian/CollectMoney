package com.yzdsmart.Dingdingwen.money_friendship.group_list.choose;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.adapters.ExpandGroupListAdapter;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendProfile;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendshipInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ChooseFriendActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo})
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
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation_layout)
    FrameLayout titleRightOpeLayout;

    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private List<FriendProfile> selectList = new ArrayList<>();
    private List<String> mAlreadySelect = new ArrayList<>();
    private List<FriendProfile> alreadySelectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> selected = getIntent().getStringArrayListExtra("selected");
        if (selected != null) {
            mAlreadySelect.addAll(selected);
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText("创建群");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.confirm_icon));
        titleLeftOpeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });
        titleRightOpeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList.size() == 0) {
                    Toast.makeText(ChooseFriendActivity.this, getString(R.string.choose_need_one), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra("select", getSelectIds());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();
        for (String id : mAlreadySelect) {
            for (String key : friends.keySet()) {
                for (FriendProfile profile : friends.get(key)) {
                    if (id.equals(profile.getIdentify())) {
                        profile.setIsSelected(true);
                        alreadySelectList.add(profile);
                    }
                }
            }
        }
        mGroupListView = (ExpandableListView) findViewById(R.id.groupList);
        mGroupListAdapter = new ExpandGroupListAdapter(this, FriendshipInfo.getInstance().getGroups(), friends, true);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                FriendProfile profile = friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition);
                if (alreadySelectList.contains(profile)) return false;
                onSelect(profile);
                mGroupListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mGroupListAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_choose_friend;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (FriendProfile item : selectList) {
            item.setIsSelected(false);
        }
        for (FriendProfile item : alreadySelectList) {
            item.setIsSelected(false);
        }
    }

    private void onSelect(FriendProfile profile) {
        if (!profile.isSelected()) {
            selectList.add(profile);
        } else {
            selectList.remove(profile);
        }
        profile.setIsSelected(!profile.isSelected());
    }

    private ArrayList<String> getSelectIds() {
        ArrayList<String> result = new ArrayList<>();
        for (FriendProfile item : selectList) {
            result.add(item.getIdentify());
        }
        return result;
    }
}
