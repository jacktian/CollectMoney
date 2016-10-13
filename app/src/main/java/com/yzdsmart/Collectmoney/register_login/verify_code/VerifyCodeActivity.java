package com.yzdsmart.Collectmoney.register_login.verify_code;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.register_login.set_password.SetPasswordActivity;
import com.yzdsmart.Collectmoney.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyCodeActivity extends BaseActivity implements VerifyCodeContract.VerifyCodeView {
    @Nullable
    @BindViews({R.id.app_logo, R.id.register_login_phone_icon, R.id.user_pwd_layout, R.id.user_confirm_pwd_layout, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.user_name)
    EditText userNameET;
    @Nullable
    @BindView(R.id.user_verify_code)
    EditText verifyCodeET;
    @Nullable
    @BindView(R.id.get_verify_button)
    TextView getVerifyTV;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button nextButton;

    private Integer opeType;//0 注册 1 忘记密码
    private String userName;
    private VerifyCodeContract.VerifyCodePresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable getVerifyCodeRunnable;
    private Integer countDownTime = 60;//获取短信验证码倒计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        userNameET.setEnabled(false);
        verifyCodeET.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Bundle bundle = getIntent().getExtras();
        opeType = bundle.getInt("opeType");
        userName = bundle.getString("userName");
        userNameET.setText(userName);
        switch (opeType) {
            case 0:
                centerTitleTV.setText(getResources().getString(R.string.register));
                break;
            case 1:
                centerTitleTV.setText(getResources().getString(R.string.forget_pwd));
                break;
        }
        nextButton.setText(getResources().getString(R.string.next_step));

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
                    countDownTime = 60;
                }
            }
        };

        new VerifyCodePresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_verify_code;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.get_verify_button, R.id.login_register_confirm_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.get_verify_button:
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                getVerifyTV.setEnabled(false);
                mHandler.post(getVerifyCodeRunnable);
                mPresenter.getVerifyCode(userName, DateTime.now().toString());
                break;
            case R.id.login_register_confirm_button:
                if (!requiredVerify(verifyCodeET)) {
                    verifyCodeET.setError(getResources().getString(R.string.input_verify_code));
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.validateVerifyCode("000000", userName, verifyCodeET.getText().toString());
                break;
        }
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

    @Override
    public void setPresenter(VerifyCodeContract.VerifyCodePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetVerifyCode(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            getVerifyTV.setText(getResources().getString(R.string.get_verify_code));
            getVerifyTV.setEnabled(true);
            mHandler.removeCallbacks(getVerifyCodeRunnable);
            countDownTime = 60;
            return;
        }
        showSnackbar("获取验证码成功");
    }

    @Override
    public void onValidateVerifyCode(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("opeType", opeType);
        bundle.putString("userName", userNameET.getText().toString());
        openActivity(SetPasswordActivity.class, bundle, 0);
    }
}
