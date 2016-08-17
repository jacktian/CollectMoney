package com.yzdsmart.Collectmoney.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.register_forget_password.RegisterForgetPasswordActivity;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class LoginActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.toolbar_layout, R.id.user_count_down_layout, R.id.user_confirm_pwd_layout})
    List<View> hideViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Optional
    @OnClick({R.id.forget_pwd_link, R.id.new_user_link})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.forget_pwd_link:
                bundle = new Bundle();
                bundle.putInt("opeType", 1);
                openActivity(RegisterForgetPasswordActivity.class, bundle, 0);
                break;
            case R.id.new_user_link:
                bundle = new Bundle();
                bundle.putInt("opeType", 0);
                openActivity(RegisterForgetPasswordActivity.class, bundle, 0);
                break;
        }
    }
}
