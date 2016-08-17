package com.yzdsmart.Collectmoney.register_forget_password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class RegisterForgetPasswordActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout, R.id.forget_pwd_link, R.id.new_user_link})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.user_name_icon)
    ImageView userNameIconIV;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button registerButton;

    private Integer opeType;//0 注册新用户 1 忘记密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        opeType = getIntent().getExtras().getInt("opeType");
        switch (opeType) {
            case 0:
                centerTitleTV.setText(getResources().getString(R.string.register));
                registerButton.setText(getResources().getString(R.string.register));
                break;
            case 1:
                centerTitleTV.setText(getResources().getString(R.string.forget_pwd));
                registerButton.setText(getResources().getString(R.string.confirm));
                break;
        }
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        userNameIconIV.setImageDrawable(getResources().getDrawable(R.mipmap.mobile_icon));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register_forget_password;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }
}
