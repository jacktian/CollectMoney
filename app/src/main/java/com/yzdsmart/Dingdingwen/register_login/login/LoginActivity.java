package com.yzdsmart.Dingdingwen.register_login.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.LoginRequestResponse;
import com.yzdsmart.Dingdingwen.register_login.verify_code.VerifyCodeActivity;
import com.yzdsmart.Dingdingwen.register_login.verify_phone.VerifyPhoneActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by YZD on 2016/8/17.
 */
public class LoginActivity extends BaseActivity implements LoginContract.LoginView {
    @Nullable
    @BindViews({R.id.register_login_area_code, R.id.common_user_info_layout_padding, R.id.user_count_down_layout, R.id.user_confirm_pwd_layout, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindViews({R.id.app_logo_layout, R.id.forget_new_user_layout})
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

    private static final String TAG = "LoginActivity";

    private LoginContract.LoginPresenter mPresenter;

    private String thirdPlatformExportData;
    private String userGender;
    private String userNickName;
    private String platFormName;
    private Bundle bundle;
    private Handler thirdPlatformLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            thirdPlatformLogin(bundle.getString("userID"), bundle.getString("platFormName"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
        centerTitleTV.setText(getResources().getString(R.string.login));
        userPasswordET.setImeOptions(EditorInfo.IME_ACTION_DONE);

        bundle = new Bundle();

        new LoginPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        ShareSDK.initSDK(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
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
    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.forget_pwd_link, R.id.new_user_link, R.id.login_register_confirm_button, R.id.platform_wechat, R.id.platform_qq, R.id.platform_webo})
    void onClick(View view) {
        final Bundle bundle;
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
                mPresenter.userLogin(userNameET.getText().toString(), userPasswordET.getText().toString(), "", SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case R.id.platform_wechat:
                platFormName = "wx";
                Platform platformWeChat = ShareSDK.getPlatform(this, Wechat.NAME);
                platformWeChat.SSOSetting(false);  //设置false表示使用SSO授权方式
                platformWeChat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
                        thirdPlatformExportData = platform.getDb().exportData();
                        JSONObject exportData = JSON.parseObject(platform.getDb().exportData());
                        userGender = exportData.getString("gender").equals("1") ? "女" : "男";
                        userNickName = exportData.getString("nickname");
                        SharedPreferencesUtils.setString(LoginActivity.this, "exportData", thirdPlatformExportData);
                        SharedPreferencesUtils.setString(LoginActivity.this, "platform", platFormName);
                        SharedPreferencesUtils.setString(LoginActivity.this, "userGender", userGender);
                        SharedPreferencesUtils.setString(LoginActivity.this, "userNickName", userNickName);
                        Bundle loginBundle = new Bundle();
                        loginBundle.putString("userID", exportData.getString("userID"));
                        loginBundle.putString("platFormName", platFormName);
                        Message loginMsg = new Message();
                        loginMsg.setData(loginBundle);
                        thirdPlatformLoginHandler.sendMessage(loginMsg);
                        //遍历Map
//                        Iterator ite = res.entrySet().iterator();
//                        while (ite.hasNext()) {
//                            Map.Entry entry = (Map.Entry) ite.next();
//                            Object key = entry.getKey();
//                            Object value = entry.getValue();
//                            System.out.println(key + "------:------" + value);
//                        }
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        showSnackbar("取消登录");
                    }
                }); // 设置分享事件回调
                platformWeChat.showUser(null);//授权并获取用户信息
                break;
            case R.id.platform_qq:
                platFormName = "qq";
                Platform platformQQ = ShareSDK.getPlatform(this, QQ.NAME);
                platformQQ.SSOSetting(false);  //设置false表示使用SSO授权方式
                platformQQ.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
                        thirdPlatformExportData = platform.getDb().exportData();
                        JSONObject exportData = JSON.parseObject(platform.getDb().exportData());
                        userGender = exportData.getString("gender").equals("1") ? "女" : "男";
                        userNickName = exportData.getString("nickname");
                        SharedPreferencesUtils.setString(LoginActivity.this, "exportData", thirdPlatformExportData);
                        SharedPreferencesUtils.setString(LoginActivity.this, "platform", platFormName);
                        SharedPreferencesUtils.setString(LoginActivity.this, "userGender", userGender);
                        SharedPreferencesUtils.setString(LoginActivity.this, "userNickName", userNickName);
//                        thirdPlatformLogin(exportData.getString("userID"), platFormName);
                        Bundle loginBundle = new Bundle();
                        loginBundle.putString("userID", exportData.getString("userID"));
                        loginBundle.putString("platFormName", platFormName);
                        Message loginMsg = new Message();
                        loginMsg.setData(loginBundle);
                        thirdPlatformLoginHandler.sendMessage(loginMsg);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        showSnackbar("取消登录");
                    }
                }); // 设置分享事件回调
                platformQQ.showUser(null);//授权并获取用户信息
                break;
            case R.id.platform_webo:
                platFormName = "wb";
                Platform platformWeiBo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                platformWeiBo.SSOSetting(false);  //设置false表示使用SSO授权方式
                platformWeiBo.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
                        thirdPlatformExportData = platform.getDb().exportData();
                        JSONObject exportData = JSON.parseObject(platform.getDb().exportData());
                        userGender = exportData.getString("gender").equals("1") ? "女" : "男";
                        userNickName = exportData.getString("nickname");
                        SharedPreferencesUtils.setString(LoginActivity.this, "exportData", thirdPlatformExportData);
                        SharedPreferencesUtils.setString(LoginActivity.this, "platform", platFormName);
                        SharedPreferencesUtils.setString(LoginActivity.this, "userGender", userGender);
                        SharedPreferencesUtils.setString(LoginActivity.this, "userNickName", userNickName);
//                        thirdPlatformLogin(exportData.getString("userID"), platFormName);
                        Bundle loginBundle = new Bundle();
                        loginBundle.putString("userID", exportData.getString("userID"));
                        loginBundle.putString("platFormName", platFormName);
                        Message loginMsg = new Message();
                        loginMsg.setData(loginBundle);
                        thirdPlatformLoginHandler.sendMessage(loginMsg);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        showSnackbar("取消登录");
                    }
                }); // 设置分享事件回调
                platformWeiBo.showUser(null);//授权并获取用户信息
                break;
        }
    }

    private void thirdPlatformLogin(String userID, String platform) {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.thirdPlatformLogin(userID, platform, "", SharedPreferencesUtils.getString(LoginActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(LoginActivity.this, "ddw_access_token", ""));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
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
        JPushInterface.init(App.getAppInstance());
        JPushInterface.resumePush(App.getAppInstance());
        if (null != SharedPreferencesUtils.getString(this, "baza_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "baza_code", ""))) {
            setAlias(SharedPreferencesUtils.getString(this, "baza_code", "").replaceAll("-", ""));
        } else if (null != SharedPreferencesUtils.getString(this, "cust_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "cust_code", ""))) {
            setAlias(SharedPreferencesUtils.getString(this, "cust_code", "").replaceAll("-", ""));
        } else {
            return;
        }
        setResult(RESULT_OK);
        closeActivity();
    }

    @Override
    public void onThirdPlatformLoginSuccess(LoginRequestResponse requestResponse) {
        JPushInterface.init(App.getAppInstance());
        JPushInterface.resumePush(App.getAppInstance());
        if (null != SharedPreferencesUtils.getString(this, "baza_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "baza_code", ""))) {
            setAlias(SharedPreferencesUtils.getString(this, "baza_code", "").replaceAll("-", ""));
        } else if (null != SharedPreferencesUtils.getString(this, "cust_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "cust_code", ""))) {
            setAlias(SharedPreferencesUtils.getString(this, "cust_code", "").replaceAll("-", ""));
        } else {
            return;
        }
        setResult(RESULT_OK);
        closeActivity();
    }

    @Override
    public void onThirdPlatformNotBindPhone() {
        showSnackbar("没有绑定手机号");
        bundle.putInt("opeType", 2);
        openActivity(VerifyCodeActivity.class, bundle, 0);
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
            switch (code) {
                case 0:
//                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    SharedPreferencesUtils.setString(LoginActivity.this, "push_alias", alias);
                    break;
                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
//                    logs = "Failed with errorCode = " + code;
            }
//            showSnackbar(logs);
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAlias(getApplicationContext(),
                            (String) msg.obj,
                            mAliasCallback);
                    break;
                default:
            }
        }
    };
}
