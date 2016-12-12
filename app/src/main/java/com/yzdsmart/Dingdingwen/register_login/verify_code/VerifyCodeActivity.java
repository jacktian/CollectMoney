package com.yzdsmart.Dingdingwen.register_login.verify_code;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.register_login.set_password.SetPasswordActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by YZD on 2016/8/26.
 */
public class VerifyCodeActivity extends BaseActivity implements VerifyCodeContract.VerifyCodeView {
    @Nullable
    @BindViews({R.id.register_login_phone_icon, R.id.user_pwd_layout, R.id.user_confirm_pwd_layout, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
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

    private static final String TAG = "VerifyCodeActivity";

    private Integer opeType;//0 注册 1 忘记密码 2 第三方注册
    private String userName;
    private VerifyCodeContract.VerifyCodePresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable getVerifyCodeRunnable;
    private Integer countDownTime = 60;//获取短信验证码倒计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        verifyCodeET.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Bundle bundle = getIntent().getExtras();
        opeType = bundle.getInt("opeType");
        userName = bundle.getString("userName");
        if (null != userName) {
            userNameET.setText(userName);
        }
        switch (opeType) {
            case 0:
                centerTitleTV.setText(getResources().getString(R.string.register));
                userNameET.setEnabled(false);
                nextButton.setText(getResources().getString(R.string.next_step));
                break;
            case 1:
                centerTitleTV.setText(getResources().getString(R.string.forget_pwd));
                userNameET.setEnabled(false);
                nextButton.setText(getResources().getString(R.string.next_step));
                break;
            case 2:
                centerTitleTV.setText(getResources().getString(R.string.bind_phone));
                nextButton.setText(getResources().getString(R.string.confirm));
                break;
        }

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

        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_verify_code;
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

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.get_verify_button, R.id.login_register_confirm_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                if (null != SharedPreferencesUtils.getString(VerifyCodeActivity.this, "platform", "") && SharedPreferencesUtils.getString(VerifyCodeActivity.this, "platform", "").length() > 0) {
                    cancelThirdPlatformLogin();
                }
                closeActivity();
                break;
            case R.id.get_verify_button:
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                getVerifyTV.setEnabled(false);
                mHandler.post(getVerifyCodeRunnable);
                mPresenter.getVerifyCode(userNameET.getText().toString(), DateTime.now().toString(), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
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
                mPresenter.validateVerifyCode("000000", userNameET.getText().toString(), verifyCodeET.getText().toString(), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (null != SharedPreferencesUtils.getString(VerifyCodeActivity.this, "platform", "") && SharedPreferencesUtils.getString(VerifyCodeActivity.this, "platform", "").length() > 0) {
                    cancelThirdPlatformLogin();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
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
        if (null != SharedPreferencesUtils.getString(this, "platform", "") && SharedPreferencesUtils.getString(this, "platform", "").length() > 0) {
            mPresenter.thirdPlatformRegister(Constants.THIRD_PLATFORM_ACTION_CODE, userNameET.getText().toString(), userNameET.getText().toString(), SharedPreferencesUtils.getString(this, "userGender", ""), 18, SharedPreferencesUtils.getString(this, "userNickName", ""), SharedPreferencesUtils.getString(this, "platform", ""), SharedPreferencesUtils.getString(this, "exportData", ""), Utils.md5(Constants.THIRD_PLATFORM_ACTION_CODE + "yzd" + userNameET.getText().toString()), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("opeType", opeType);
        bundle.putString("userName", userNameET.getText().toString());
        openActivity(SetPasswordActivity.class, bundle, 0);
    }

    @Override
    public void onThirdPlatformRegister() {
        openActivityClear(MainActivity.class, null, 0);
    }

    private void cancelThirdPlatformLogin() {
        String platformName = SharedPreferencesUtils.getString(VerifyCodeActivity.this, "platform", "");
        if (null != platformName && platformName.length() > 0) {
            Platform platform = null;
            if ("qq".equals(platformName)) {
                platform = ShareSDK.getPlatform(VerifyCodeActivity.this, QQ.NAME);
            } else if ("wb".equals(platformName)) {
                platform = ShareSDK.getPlatform(VerifyCodeActivity.this, SinaWeibo.NAME);
            } else if ("wx".equals(platformName)) {
                platform = ShareSDK.getPlatform(VerifyCodeActivity.this, Wechat.NAME);
            }
            if (platform.isAuthValid()) {
                platform.removeAccount(true);
            }
            SharedPreferencesUtils.remove(VerifyCodeActivity.this, "platform");
            SharedPreferencesUtils.remove(VerifyCodeActivity.this, "exportData");
            SharedPreferencesUtils.remove(VerifyCodeActivity.this, "userGender");
            SharedPreferencesUtils.remove(VerifyCodeActivity.this, "userNickName");
        }
    }
}
