package com.yzdsmart.Collectmoney.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.login.LoginActivity;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
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
}
