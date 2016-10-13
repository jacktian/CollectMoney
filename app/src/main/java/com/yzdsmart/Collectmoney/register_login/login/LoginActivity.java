package com.yzdsmart.Collectmoney.register_login.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.register_login.verify_phone.VerifyPhoneActivity;
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
    @BindViews({R.id.register_login_area_code, R.id.user_count_down_layout, R.id.user_confirm_pwd_layout, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindViews({R.id.forget_new_user_layout})
    List<View> showViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.user_name)
    EditText userNameET;
    @Nullable
    @BindView(R.id.user_pwd)
    EditText userPasswordET;

    //腾讯
    public static final String mTecentAppid = "1105651703";
    public static Tencent mTencent;
    //腾讯

    //微博
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String APP_KEY = "1371063102";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * 授权认证所需要的信息
     */
    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * SSO 授权认证实例
     */
    private SsoHandler mSsoHandler;

    /**
     * 登陆认证对应的listener
     */
    private AuthListener mLoginListener = new AuthListener();
    //微博

    //微信
    private static final String APP_ID = "wx1f0757f65d97d285";
    //IWXAPI是第三方app和微信通信的openapi接口
    private IWXAPI iwxapi;

    //注册到微信
    private void regToWx() {
        //通过WXAPIFactory工厂获取IWXAPI的实例
        iwxapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }
    //微信

    private LoginContract.LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
        centerTitleTV.setText(getResources().getString(R.string.login));
        userPasswordET.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //腾讯
        if (null == mTencent) {
            mTencent = Tencent.createInstance(mTecentAppid, App.getAppInstance());
        }

        //微博
        // 创建授权认证信息
        mAuthInfo = new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE);

        new LoginPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.forget_pwd_link, R.id.new_user_link, R.id.login_register_confirm_button, R.id.platform_wechat, R.id.platform_qq, R.id.platform_webo})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
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
            case R.id.platform_wechat:
                regToWx();
                break;
            case R.id.platform_qq:
                if (!mTencent.isSessionValid()) {
                    mTencent.login(this, "all", tecentLoginListener);
                }
                break;
            case R.id.platform_webo:
                if (null == mSsoHandler && mAuthInfo != null) {
                    mSsoHandler = new SsoHandler(this, mAuthInfo);
                }
                if (mSsoHandler != null) {
                    mSsoHandler.authorize(mLoginListener);
                } else {
                    showSnackbar("Please setWeiboAuthInfo(...) for first");
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

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            showSnackbar(e.getMessage());
        }

        @Override
        public void onCancel() {
            showSnackbar(getResources().getString(R.string.weibosdk_demo_toast_auth_canceled));
        }
    }
}
