package com.yzdsmart.Collectmoney.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.find_money.FindMoneyFragment;
import com.yzdsmart.Collectmoney.main.recommend.RecommendFragment;
import com.yzdsmart.Collectmoney.money_friendship.MoneyFriendshipActivity;
import com.yzdsmart.Collectmoney.register_login_password.login.LoginActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.UserInfo;
import com.yzdsmart.Collectmoney.tecent_im.service.TLSService;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        fm = getFragmentManager();

        initView();

        mainBottomTab.setOnCheckedChangeListener(this);

        tlsService = TLSService.getInstance();

        new MainPresenter(this, this, tlsService);

        imLogin();
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
        super.onResume();
//        if (!(mCurrentFragment instanceof FindMoneyFragment)) {
//            backToFindMoney();
//        }
    }

    @Override
    protected void onDestroy() {
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
}
