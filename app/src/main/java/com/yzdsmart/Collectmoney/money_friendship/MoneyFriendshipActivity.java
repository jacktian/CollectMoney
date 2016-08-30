package com.yzdsmart.Collectmoney.money_friendship;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.friend_future.FriendFutureActivity;
import com.yzdsmart.Collectmoney.money_friendship.conversation.ConversationFragment;
import com.yzdsmart.Collectmoney.money_friendship.friend_list.FriendListFragment;
import com.yzdsmart.Collectmoney.money_friendship.group_list.GroupListFragment;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/18.
 */
public class MoneyFriendshipActivity extends BaseActivity implements MoneyFriendshipContract.MoneyFriendshipView, CustomNestRadioGroup.OnCheckedChangeListener {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_right_operation_layout)
    FrameLayout titleRightOpeLayout;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.friend_profile_list)
    UltimateRecyclerView friendListRV;
    @Nullable
    @BindView(R.id.im_bottom_tab)
    CustomNestRadioGroup imBottomTab;

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    private MoneyFriendshipContract.MoneyFriendshipPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getFragmentManager();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));

        imBottomTab.setOnCheckedChangeListener(this);

        new MoneyFriendshipPresenter(this, this);

        initView();
    }

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new ConversationFragment();
        ft.add(R.id.layout_frame, mCurrentFragment, "conversation");
        ft.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_money_friendship;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.title_right_operation_layout:
                openActivity(FriendFutureActivity.class);
                break;
        }
    }

    @Override
    public void setPresenter(MoneyFriendshipContract.MoneyFriendshipPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unRegisterObserver();
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
        Fragment fragment;
        ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEVISIBLE);
        switch (group.getCheckedRadioButtonId()) {
            case R.id.conversation_radio:
                ButterKnife.apply(titleRightOpeLayout, BUTTERKNIFEGONE);
                fragment = fm.findFragmentByTag("conversation");
                if (null == fragment) {
                    fragment = new ConversationFragment();
                }
                addOrShowFragment(fragment, "conversation");
                break;
            case R.id.friend_list_radio:
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));
                fragment = fm.findFragmentByTag("friend_list");
                if (null == fragment) {
                    fragment = new FriendListFragment();
                }
                addOrShowFragment(fragment, "friend_list");
                break;
            case R.id.group_list_radio:
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.plus_icon));
                fragment = fm.findFragmentByTag("group_list");
                if (null == fragment) {
                    fragment = new GroupListFragment();
                }
                addOrShowFragment(fragment, "group_list");
                break;
        }
    }

    /**
     * 添加或者显示碎片
     *
     * @param fragment
     * @param tag
     */
    public void addOrShowFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        if (!fragment.isAdded()) {
            ft.hide(mCurrentFragment).add(R.id.layout_frame, fragment, tag);
        } else {
            ft.hide(mCurrentFragment).show(fragment);
        }
        ft.commit();
        mCurrentFragment = fragment;
    }
}
