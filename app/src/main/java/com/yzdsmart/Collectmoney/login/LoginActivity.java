package com.yzdsmart.Collectmoney.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.register_forget_password.verify_phone.VerifyPhoneActivity;
import com.yzdsmart.Collectmoney.utils.Utils;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class LoginActivity extends BaseActivity implements LoginContract.LoginView {
    @Nullable
    @BindViews({R.id.user_count_down_layout, R.id.user_confirm_pwd_layout, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.user_name)
    EditText userNameET;
    @Nullable
    @BindView(R.id.user_pwd)
    EditText userPasswordET;

    public static final String mTecentAppid = "1105651703";
    public static Tencent mTencent;

    private LoginContract.LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        userPasswordET.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if (null == mTencent) {
            mTencent = Tencent.createInstance(mTecentAppid, App.getAppInstance());
        }

        new LoginPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Optional
    @OnClick({R.id.forget_pwd_link, R.id.new_user_link, R.id.login_register_confirm_button, R.id.platform_qq})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.forget_pwd_link:
                bundle = new Bundle();
                bundle.putInt("opeType", 1);
                openActivity(VerifyPhoneActivity.class, bundle, 0);
                break;
            case R.id.new_user_link:
                bundle = new Bundle();
                bundle.putInt("opeType", 0);
                openActivity(VerifyPhoneActivity.class, bundle, 0);
                break;
            case R.id.login_register_confirm_button:
                if (!requiredVerify(userNameET)) {
                    userNameET.setError(getResources().getString(R.string.input_phone_num));
                    return;
                }
                if (!requiredVerify(userPasswordET)) {
                    userPasswordET.setError(getResources().getString(R.string.input_pwd));
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.userLogin(userNameET.getText().toString(), userPasswordET.getText().toString(), "");
                break;
            case R.id.platform_qq:
                if (!mTencent.isSessionValid()) {
                    mTencent.login(this, "all", tecentLoginListener);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, tecentLoginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(LoginContract.LoginPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUserLogin(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        setResult(RESULT_OK);
        closeActivity();
    }

    IUiListener tecentLoginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            showSnackbar("AuthorSwitch_SDK:" + values);
//            initOpenidAndToken(values);
//            updateUserInfo();
//            updateLoginButton();
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                showSnackbar("返回为空,登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                showSnackbar("返回为空,登录失败");
                return;
            }
            showSnackbar("登录成功");
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            showSnackbar("onError: " + e.errorDetail);
        }

        @Override
        public void onCancel() {
            showSnackbar("onCancel");
        }
    }
}
