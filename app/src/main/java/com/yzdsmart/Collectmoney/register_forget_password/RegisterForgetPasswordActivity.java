package com.yzdsmart.Collectmoney.register_forget_password;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class RegisterForgetPasswordActivity extends BaseActivity implements RegisterForgetPasswordContract.RegisterForgetPasswordView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout, R.id.forget_pwd_link, R.id.new_user_link})
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
    @BindView(R.id.user_name_icon)
    ImageView userNameIconIV;
    @Nullable
    @BindView(R.id.user_name)
    EditText userNameET;
    @Nullable
    @BindView(R.id.get_verify_button)
    TextView getVerifyTV;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button registerButton;

    private Integer opeType;//0 注册新用户 1 忘记密码

    private RegisterForgetPasswordContract.RegisterForgetPasswordPresenter mPresenter;

    private Boolean isTelUsable = false;//手机号码是否可用(已注册)

    private Handler mHandler = new Handler();
    private Runnable getVerifyCodeRunnable;
    private Integer countDownTime = 10;//获取短信验证码倒计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

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

        new RegisterForgetPasswordPresenter(this, this);

        getVerifyCodeRunnable = new Runnable() {
            @Override
            public void run() {
                if (countDownTime > 1) {
                    getVerifyTV.setText((--countDownTime) + " 秒");
                    mHandler.postDelayed(this, 1000);
                } else {
                    getVerifyTV.setText(getResources().getString(R.string.get_verify_code));
                    getVerifyTV.setEnabled(true);
                    mHandler.removeCallbacks(this);
                    countDownTime = 10;
                }
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register_forget_password;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(getVerifyCodeRunnable);
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.get_verify_button, R.id.login_register_confirm_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.get_verify_button:
                Utils.hideSoftInput(this);
                String userName = userNameET.getText().toString();
                if (!requiredVerify(userNameET)) {
                    userNameET.setError(getResources().getString(R.string.input_phone_num));
                    return;
                }
                if (!Utils.checkMobile(userName)) {
                    userNameET.setError(getResources().getString(R.string.phone_num_error));
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                if (userNameET.isFocused()) {
                    mPresenter.getVerifyCodeUsable(userName);
                    return;
                }
                if (!isTelUsable) return;
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                getVerifyTV.setEnabled(false);
                mHandler.post(getVerifyCodeRunnable);
                mPresenter.getVerifyCode(userName, DateTime.now().toString());
                break;
            case R.id.login_register_confirm_button:
                break;
        }
    }

    @Optional
    @OnFocusChange({R.id.user_name})
    void onFocusChange(View view, boolean focus) {
        if (!focus) {
            switch (view.getId()) {
                case R.id.user_name:
                    Utils.hideSoftInput(this);
                    String userName = userNameET.getText().toString();
                    if (!requiredVerify(userNameET)) {
                        userNameET.setError(getResources().getString(R.string.input_phone_num));
                        return;
                    }
                    if (!Utils.checkMobile(userName)) {
                        userNameET.setError(getResources().getString(R.string.phone_num_error));
                        return;
                    }
                    if (!Utils.isNetUsable(this)) {
                        showSnackbar(getResources().getString(R.string.net_unusable));
                        return;
                    }
                    mPresenter.isUserExist(userName);
                    break;
            }
        }
    }

    @Override
    public void setPresenter(RegisterForgetPasswordContract.RegisterForgetPasswordPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onIsUserExist(boolean phoneUsable) {
        isTelUsable = phoneUsable;
        if (!phoneUsable)
            userNameET.setError(getResources().getString(R.string.user_exist));
    }

    @Override
    public void onGetVerifyCodeUsable(boolean phoneUsable) {
        isTelUsable = phoneUsable;
        if (!phoneUsable) {
            userNameET.setError(getResources().getString(R.string.user_exist));
            return;
        }
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        getVerifyTV.setEnabled(false);
        mHandler.post(getVerifyCodeRunnable);
        mPresenter.getVerifyCode(userNameET.getText().toString(), DateTime.now().toString());
    }

    private void verifyUserName(String userName) {
        if (!requiredVerify(userNameET)) {
            userNameET.setError(getResources().getString(R.string.input_phone_num));
            return;
        }
        if (!Utils.checkMobile(userName)) {
            userNameET.setError(getResources().getString(R.string.phone_num_error));
            return;
        }
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
    }
}
