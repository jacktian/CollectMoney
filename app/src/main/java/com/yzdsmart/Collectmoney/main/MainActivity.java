package com.yzdsmart.Collectmoney.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.login.LoginActivity;
import com.yzdsmart.Collectmoney.main.find_money.FindMoneyFragment;
import com.yzdsmart.Collectmoney.main.money_friendship.MoneyFriendshipFragment;
import com.yzdsmart.Collectmoney.main.recommend.RecommendFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.main_bottom_tab)
    RadioGroup mainBottomTab;

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        fm = getFragmentManager();

        initView();
    }

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new FindMoneyFragment();
        ft.add(R.id.layout_frame, mCurrentFragment, "find");
        ft.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                break;
            case R.id.title_right_operation_layout:
                openActivity(LoginActivity.class);
                break;
        }
    }

    /**
     * 切换底部tab
     *
     * @param button
     * @param changed
     */
    @Optional
    @OnCheckedChanged({R.id.recommend_radio, R.id.money_friend_radio})
    void switchTab(CompoundButton button, boolean changed) {
        if (mainBottomTab.getCheckedRadioButtonId() != button.getId()) {
            Fragment fragment;
            switch (button.getId()) {
                case R.id.recommend_radio:
                    fragment = fm.findFragmentByTag("recommend");
                    if (null == fragment) {
                        fragment = new RecommendFragment();
                    }
                    addOrShowFragment(fragment, "recommend");
                    break;
                case R.id.money_friend_radio:
                    fragment = fm.findFragmentByTag("friend");
                    if (null == fragment) {
                        fragment = new MoneyFriendshipFragment();
                    }
                    addOrShowFragment(fragment, "friend");
                    break;
            }
        }
    }

    /**
     * 添加或者显示碎片
     *
     * @param fragment
     * @param tag
     */
    private void addOrShowFragment(Fragment fragment, String tag) {
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
