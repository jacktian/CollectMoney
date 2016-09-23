package com.yzdsmart.Collectmoney.register_forget_password.verify_phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.register_forget_password.verify_code.VerifyCodeActivity;
import com.yzdsmart.Collectmoney.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyPhoneActivity extends BaseActivity implements VerifyPhoneContract.VerifyPhoneView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout, R.id.user_count_down_layout, R.id.user_pwd_layout, R.id.user_confirm_pwd_layout, R.id.forget_pwd_link, R.id.new_user_link, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.user_name)
    EditText userNameET;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button nextButton;

    private Integer opeType;//0 注册 1 忘记密码
    private VerifyPhoneContract.VerifyPhonePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        toolBar.setBackgroundColor(getResources().getColor(R.color.light_yellow));
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        userNameET.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Bundle bundle = getIntent().getExtras();
        opeType = bundle.getInt("opeType");
        switch (opeType) {
            case 0:
                centerTitleTV.setText(getResources().getString(R.string.register));
                break;
            case 1:
                centerTitleTV.setText(getResources().getString(R.string.forget_pwd));
                break;
        }
        nextButton.setText(getResources().getString(R.string.next_step));

        new VerifyPhonePresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_verify_phone;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.login_register_confirm_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.login_register_confirm_button:
                if (!requiredVerify(userNameET)) {
                    userNameET.setError(getResources().getString(R.string.input_phone_num));
                    return;
                }
                if (!Utils.checkMobile(userNameET.getText().toString())) {
                    userNameET.setError(getResources().getString(R.string.phone_num_error));
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.isUserExist(userNameET.getText().toString());
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void setPresenter(VerifyPhoneContract.VerifyPhonePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onIsUserExist(boolean phoneUsable, String msg) {
        switch (opeType) {
            case 0:
                if (!phoneUsable) {
                    userNameET.setError(msg);
                    return;
                }
                break;
            case 1:
                if (phoneUsable) {
                    userNameET.setError(getResources().getString(R.string.user_not_exist));
                    return;
                }
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("opeType", opeType);
        bundle.putString("userName", userNameET.getText().toString());
        openActivity(VerifyCodeActivity.class, bundle, 0);
    }
}
