package com.yzdsmart.Collectmoney.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.find_money.FindMoneyFragment;
import com.yzdsmart.Collectmoney.main.recommend.RecommendFragment;
import com.yzdsmart.Collectmoney.money_friendship.MoneyFriendshipActivity;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

import butterknife.BindView;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity implements CustomNestRadioGroup.OnCheckedChangeListener {
    //    @Nullable
//    @BindViews({R.id.left_title, R.id.center_title})
//    List<View> hideViews;
    @Nullable
    @BindView(R.id.main_bottom_tab)
    CustomNestRadioGroup mainBottomTab;

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        fm = getFragmentManager();

        initView();

        mainBottomTab.setOnCheckedChangeListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

//    @Optional
//    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.title_left_operation_layout:
//                if (mCurrentFragment instanceof FindMoneyFragment) {
//                    Fragment fragment = fm.findFragmentByTag("personal");
//                    if (null == fragment) {
//                        fragment = new PersonalFragment();
//                    }
//                    addOrShowFragment(fragment, "personal");
//                } else if (mCurrentFragment instanceof RecommendFragment) {
//
//                }
//                break;
//            case R.id.title_right_operation_layout:
//                if (mCurrentFragment instanceof FindMoneyFragment) {
//                    openActivity(LoginActivity.class);
//                } else if (mCurrentFragment instanceof RecommendFragment) {
//
//                }
//                break;
//        }
//    }

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new FindMoneyFragment();
        ft.add(R.id.layout_frame, mCurrentFragment, "find");
        ft.commit();
    }

    @Override
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
        Fragment fragment;
        switch (checkedId) {
            case R.id.recommend_radio:
                fragment = fm.findFragmentByTag("recommend");
                if (null == fragment) {
                    fragment = new RecommendFragment();
                }
                addOrShowFragment(fragment, "recommend");
                break;
            case R.id.money_friend_radio:
                openActivity(MoneyFriendshipActivity.class);
                group.clearCheck();
                backToFindMoney();
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

    /**
     * 返回到找钱页
     */
    public void backToFindMoney() {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null == fragment) {
            fragment = new FindMoneyFragment();
        }
        addOrShowFragment(fragment, "find");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (!(mCurrentFragment instanceof FindMoneyFragment)) {
                    backToFindMoney();
                    mainBottomTab.clearCheck();
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }
}
