package com.yzdsmart.Collectmoney.splash;

import android.os.Bundle;
import android.os.Handler;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;

/**
 * Created by YZD on 2016/8/17.
 */
public class SplashActivity extends BaseActivity {
    private Handler splashHandler;
    private Runnable splashRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashHandler = new Handler();
        splashRunnable = new Runnable() {
            @Override
            public void run() {
                openActivity(MainActivity.class);
                closeActivity();
            }
        };
        splashHandler.postDelayed(splashRunnable, 1000);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        splashHandler.removeCallbacks(splashRunnable);
        super.onDestroy();
    }

}
