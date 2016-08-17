package com.yzdsmart.Collectmoney.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.login.LoginActivity;
import com.yzdsmart.Collectmoney.main.find_money.FindMoneyFragment;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity implements CustomNestRadioGroup.OnCheckedChangeListener {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.main_bottom_tab)
    CustomNestRadioGroup mainBottomTab;

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        fm = getFragmentManager();

        initView();

        mainBottomTab.setOnCheckedChangeListener(this);
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

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new FindMoneyFragment();
        ft.add(R.id.layout_frame, mCurrentFragment, "find");
        ft.commit();
    }

    @Override
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {

    }
}
