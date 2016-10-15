package com.yzdsmart.Collectmoney.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.find_money.FindMoneyFragment;
import com.yzdsmart.Collectmoney.main.recommend.RecommendFragment;
import com.yzdsmart.Collectmoney.money_friendship.MoneyFriendshipActivity;
import com.yzdsmart.Collectmoney.register_login.login.LoginActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.UserInfo;
import com.yzdsmart.Collectmoney.tecent_im.service.TLSService;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.utils.Utils;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity implements CustomNestRadioGroup.OnCheckedChangeListener, MainContract.MainView {
    @Nullable
    @BindView(R.id.main_bottom_tab)
    CustomNestRadioGroup mainBottomTab;

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    private MainContract.MainPresenter mPresenter;

    private TLSService tlsService;

    private static final Integer REQUEST_LOGIN_CODE = 1000;

    private static MainActivity mainActivity;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        fm = getFragmentManager();

        initView();

        tlsService = TLSService.getInstance();

        new MainPresenter(this, this, tlsService);

        mainBottomTab.setOnCheckedChangeListener(this);

        imLogin();

        registerMessageReceiver();  // used for receive msg
        initJPush();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new FindMoneyFragment();
        ft.add(R.id.layout_frame, mCurrentFragment, "find");
        ft.commit();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
//        if (!(mCurrentFragment instanceof FindMoneyFragment)) {
//            backToFindMoney();
//        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        mPresenter.unRegisterObserver();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            imLogin();
        }
    }

    @Optional
    @OnClick({R.id.loc_scan_coins})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.loc_scan_coins:
                if (!(mCurrentFragment instanceof FindMoneyFragment)) {
                    backToFindMoney();
                }
                Fragment fragment = fm.findFragmentByTag("find");
                if (null != fragment) {
                    ((FindMoneyFragment) fragment).locScanCoins();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
        Fragment fragment;
        switch (group.getCheckedRadioButtonId()) {
            case R.id.recommend_radio:
                fragment = fm.findFragmentByTag("recommend");
                if (null == fragment) {
                    fragment = new RecommendFragment();
                }
                addOrShowFragment(fragment, "recommend");
                break;
            case R.id.money_friend_radio:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
                    group.clearCheck();
                    return;
                }
                openActivity(MoneyFriendshipActivity.class);
                fragment = fm.findFragmentByTag("find");
                if (null == fragment) {
                    fragment = new FindMoneyFragment();
                }
                addOrShowFragment(fragment, "find");
                group.clearCheck();
                break;
        }
    }

    /**
     * 添加或者显示碎片
     *
     * @param fragment
     * @param tag
     */
    public void addOrShowFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        if (!fragment.isAdded()) {
            ft.hide(mCurrentFragment).add(R.id.layout_frame, fragment, tag);
        } else {
            ft.hide(mCurrentFragment).show(fragment);
        }
        ft.commitAllowingStateLoss();
        mCurrentFragment = fragment;
    }

    /**
     * 返回到找钱页
     */
    public void backToFindMoney() {
        mainBottomTab.clearCheck();
        Fragment fragment = fm.findFragmentByTag("find");
        if (null == fragment) {
            fragment = new FindMoneyFragment();
        }
        addOrShowFragment(fragment, "find");
    }

    public void getShopListNearByMarket(String name, String location) {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            ((FindMoneyFragment) fragment).getShopListNearByMarket(name, location);
        }
    }

    public void planRoute(String coor) {
        Fragment fragment = fm.findFragmentByTag("find");
        if (null != fragment) {
            ((FindMoneyFragment) fragment).planRoute(coor);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (!(mCurrentFragment instanceof FindMoneyFragment)) {
                    backToFindMoney();
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void chatLoginSuccess() {
        mPresenter.imSDKLogin();
    }

    @Override
    public void imSDKLoginSuccess() {
        //退到后台发送通知
        //初始化程序后台后消息推送
//        PushUtil.getInstance();
    }

    @Override
    public void onIMOffline() {
        App.getAppInstance().exitApp();
        openActivity(MainActivity.class);
    }

    @Override
    public void setPresenter(MainContract.MainPresenter presenter) {
        mPresenter = presenter;
    }

    private void imLogin() {
        if (SharedPreferencesUtils.getString(this, "im_account", "").length() > 0 && SharedPreferencesUtils.getString(this, "im_password", "").length() > 0) {
            String im_name = SharedPreferencesUtils.getString(this, "im_account", "");
            String im_pwd = SharedPreferencesUtils.getString(this, "im_password", "");
            mPresenter.chatLogin(im_name, im_pwd);
        }
    }

    public void chatLogin() {
        imLogin();
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJPush() {
        if (null != SharedPreferencesUtils.getString(this, "baza_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "baza_code", ""))) {
            setAlias(SharedPreferencesUtils.getString(this, "baza_code", "").replaceAll("-", ""));
        } else if (null != SharedPreferencesUtils.getString(this, "cust_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "cust_code", ""))) {
            setAlias(SharedPreferencesUtils.getString(this, "cust_code", "").replaceAll("-", ""));
        } else {
            return;
        }
        JPushInterface.init(App.getAppInstance());
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!Utils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                showSnackbar(showMsg.toString());
            }
        }
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
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
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
            }
        }
    };
}
