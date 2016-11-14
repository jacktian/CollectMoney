package com.yzdsmart.Dingdingwen.splash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

/**
 * Created by YZD on 2016/8/17.
 */
public class SplashActivity extends AppIntro2 implements SplashContract.SplashView {
    private SplashContract.SplashPresenter mPresenter;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);

        addSlide(new SplashFragment_1());
        addSlide(new SplashFragment_2());
        addSlide(new SplashFragment_3());

        setIndicatorColor(Color.parseColor("#f20044"), Color.parseColor("#cccccc"));

        showSkipButton(false);
        showDoneButton(false);

        new SplashPresenter(this, this);

        if (null == SharedPreferencesUtils.getString(this, "isFirstOpen", "") || SharedPreferencesUtils.getString(this, "isFirstOpen", "").trim().length() <= 0) {
            if (!Utils.isNetUsable(SplashActivity.this)) {
                Toast.makeText(this, getResources().getString(R.string.net_unusable), Toast.LENGTH_SHORT).show();
                return;
            }
            mPresenter.appRegister(Utils.getDeviceId(this), "182c79dbf8871689878b0de620006ea3", "7ed99e89c4831f169627b5d20a0020f7c1a9b026244e6364ac1c12a9fa2314fe");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startMain();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startMain();
    }

    public void startMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPresenter(SplashContract.SplashPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAppRegister(boolean flag) {
        if (flag) {
            SharedPreferencesUtils.setString(this, "isFirstOpen", "true");
        }
    }
}
