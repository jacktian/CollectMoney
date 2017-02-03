package com.yzdsmart.Dingdingwen.register_login.verify_phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.register_login.agreement.AgreementActivity;
import com.yzdsmart.Dingdingwen.register_login.verify_code.VerifyCodeActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyPhoneActivity extends BaseActivity implements VerifyPhoneContract.VerifyPhoneView {
    @Nullable
    @BindViews({R.id.register_login_phone_icon, R.id.user_count_down_layout, R.id.user_pwd_layout, R.id.user_confirm_pwd_layout, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.register_agreement_layout)
    LinearLayout registerAgreementLayout;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.user_name)
    EditText userNameET;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button nextButton;

    private static final String TAG = "VerifyPhoneActivity";

    private Integer opeType;//0 注册 1 忘记密码
    private VerifyPhoneContract.VerifyPhonePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        userNameET.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if (null != savedInstanceState) {
            opeType = savedInstanceState.getInt("opeType");
        } else {
            Bundle bundle = getIntent().getExtras();
            opeType = bundle.getInt("opeType");
        }

        if (null != opeType) {
            switch (opeType) {
                case 0:
                    centerTitleTV.setText(getResources().getString(R.string.register));
                    ButterKnife.apply(registerAgreementLayout, BUTTERKNIFEVISIBLE);
                    break;
                case 1:
                    centerTitleTV.setText(getResources().getString(R.string.forget_pwd));
                    break;
            }
        }

        nextButton.setText(getResources().getString(R.string.next_step));

        new VerifyPhonePresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_verify_phone;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("opeType", opeType);
        super.onSaveInstanceState(outState);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.login_register_confirm_button, R.id.register_agreement})
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
                mPresenter.isUserExist(userNameET.getText().toString(), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case R.id.register_agreement:
                openActivity(AgreementActivity.class);
                break;
        }
    }

    @Optional
    @OnCheckedChanged({R.id.register_agreement_check})
    void onCheckedChanged(CompoundButton button, boolean changed) {
        switch (button.getId()) {
            case R.id.register_agreement_check:
                if (button.isChecked()) {
                    nextButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(false);
                }
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
