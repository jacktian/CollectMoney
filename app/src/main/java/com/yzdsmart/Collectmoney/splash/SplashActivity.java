package com.yzdsmart.Collectmoney.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.yzdsmart.Collectmoney.main.MainActivity;

/**
 * Created by YZD on 2016/8/17.
 */
public class SplashActivity extends AppIntro2 {
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);

        addSlide(new SplashFragment_1());
        addSlide(new SplashFragment_2());
        addSlide(new SplashFragment_3());

        showSkipButton(false);
        showDoneButton(false);
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
}
