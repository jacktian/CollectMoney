package com.yzdsmart.Dingdingwen.register_login.set_password;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.register_login.set_info.SetInfoActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/26.
 */
public class SetPasswordActivity extends BaseActivity implements SetPasswordContract.SetPasswordView {
    @Nullable
    @BindViews({R.id.register_login_name_layout, R.id.user_count_down_layout, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    //    @Nullable
//    @BindView(R.id.user_name)
//    EditText userNameET;
    @Nullable
    @BindView(R.id.user_pwd)
    EditText userPwdET;
    @Nullable
    @BindView(R.id.user_confirm_pwd)
    EditText userConfirmPwdET;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button confirmButton;

    private static final String TAG = "SetPasswordActivity";

    private Integer opeType;//0 注册 1 忘记密码
    private String userName;

    private SetPasswordContract.SetPasswordPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable setPwdSuccessRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            opeType = savedInstanceState.getInt("opeType");
            userName = savedInstanceState.getString("userName");
        } else {
            Bundle bundle = getIntent().getExtras();
            opeType = bundle.getInt("opeType");
            userName = bundle.getString("userName");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
//        userNameET.setEnabled(false);
//        userNameET.setText(userName);

        //密码最多16位
//        userPwdET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
//        userConfirmPwdET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

        switch (opeType) {
            case 0:
                centerTitleTV.setText(getResources().getString(R.string.register));
                confirmButton.setText(getResources().getString(R.string.confirm));
                break;
            case 1:
                centerTitleTV.setText(getResources().getString(R.string.forget_pwd));
                confirmButton.setText(getResources().getString(R.string.confirm));
                break;
        }

        new SetPasswordPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        setPwdSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                if (!Utils.isNetUsable(SetPasswordActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.userLogin(userName, userPwdET.getText().toString(), "", SharedPreferencesUtils.getString(SetPasswordActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(SetPasswordActivity.this, "ddw_access_token", ""));
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_set_password;
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
        outState.putString("userName", userName);
        super.onSaveInstanceState(outState);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.login_register_confirm_button})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.login_register_confirm_button:
                if (!requiredVerify(userPwdET)) {
                    userPwdET.setError(getResources().getString(R.string.input_pwd));
                    return;
                }
                if (!requiredVerify(userConfirmPwdET)) {
                    userConfirmPwdET.setError(getResources().getString(R.string.input_pwd));
                    return;
                }
                if (!userPwdET.getText().toString().equals(userConfirmPwdET.getText().toString())) {
                    userConfirmPwdET.setError(getResources().getString(R.string.confirm_pwd_different));
                    return;
                }
                if (userPwdET.getText().toString().trim().length() < 8) {
                    userPwdET.setError(getResources().getString(R.string.min_pwd_length));
                    return;
                }
                boolean allNumber = true;
                for (int acc_query = 0; acc_query < userPwdET.getText().toString().trim().length(); ++acc_query) {
                    char req_con = userPwdET.getText().toString().trim().charAt(acc_query);
                    if (req_con != 46 && req_con != 95 && !Character.isLetterOrDigit(req_con)) {
                        return;
                    }
                    if (!Character.isDigit(req_con)) {
                        allNumber = false;
                    }
                }
                if (allNumber) {
                    userPwdET.setError(getResources().getString(R.string.number_pwd));
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                switch (opeType) {
                    case 0:
                        bundle = new Bundle();
                        bundle.putString("userName", userName);
                        bundle.putString("password", userPwdET.getText().toString());
                        openActivity(SetInfoActivity.class, bundle, 0);
                        break;
                    case 1:
                        mPresenter.setPassword(Constants.FIND_PWD_ACTION_CODE, userName, userPwdET.getText().toString(), Utils.md5(Constants.FIND_PWD_ACTION_CODE + "yzd" + userName), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                        break;
                }
                break;
        }
    }

    @Optional
    @OnTextChanged(R.id.user_confirm_pwd)
    void onTextChanged() {
        if (!userPwdET.getText().toString().equals(userConfirmPwdET.getText().toString())) {
            userConfirmPwdET.setError(getResources().getString(R.string.confirm_pwd_different));
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(setPwdSuccessRunnable);
        super.onDestroy();
    }

    @Override
    public void setPresenter(SetPasswordContract.SetPasswordPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSetPassword(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        showProgressDialog(R.drawable.success, getResources().getString(R.string.set_success));
        mHandler.postDelayed(setPwdSuccessRunnable, 500);
    }

    @Override
    public void onUserLogin(boolean flag, String msg) {
        if (null != MainActivity.getInstance()) {
            MainActivity.getInstance().imLogin();
        }
        openActivityClear(MainActivity.class, null, 0);
    }
}
