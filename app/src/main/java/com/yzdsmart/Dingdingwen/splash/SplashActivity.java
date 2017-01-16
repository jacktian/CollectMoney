package com.yzdsmart.Dingdingwen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by YZD on 2016/8/17.
 */
public class SplashActivity extends BaseActivity implements SplashContract.SplashView {
    @Nullable
    @BindView(R.id.banner_guide_background)
    BGABanner mBackgroundBanner;
    @Nullable
    @BindView(R.id.banner_guide_foreground)
    BGABanner mForegroundBanner;
    @Nullable
    @BindView(R.id.start_main)
    Button startMainBtn;

    private SplashContract.SplashPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置数据源
        mBackgroundBanner.setData(R.mipmap.splash_1, R.mipmap.splash_2, R.mipmap.splash_3);
        mForegroundBanner.setData(R.mipmap.splash_1, R.mipmap.splash_2, R.mipmap.splash_3);
        mForegroundBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ((mForegroundBanner.getItemCount() - 1) == position) {
                    ButterKnife.apply(startMainBtn, BUTTERKNIFEVISIBLE);
                } else {
                    ButterKnife.apply(startMainBtn, BUTTERKNIFEGONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.start_main, 0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startMain();
            }
        });

        new SplashPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
        if (null != SharedPreferencesUtils.getString(this, "isFirstOpen", "") && SharedPreferencesUtils.getString(this, "isFirstOpen", "").trim().length() > 0) {
            startMain();
            return;
        }
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
        if (!Utils.isNetUsable(SplashActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.appRegister(Utils.getDeviceId(this), "182c79dbf8871689878b0de620006ea3", "7ed99e89c4831f169627b5d20a0020f7c1a9b026244e6364ac1c12a9fa2314fe");
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

//    @Override
//    public void init(@Nullable Bundle savedInstanceState) {
//        super.init(savedInstanceState);

//        addSlide(splash_1);
//        addSlide(splash_2);
//        addSlide(splash_3);
//
//        // OPTIONAL METHODS
//        // Override bar/separator color.
////        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.TRANSPARENT);
//
//        // Hide Skip/Done button.
//        showSkipButton(false);
//        setProgressButtonEnabled(false);
//
//        setIndicatorColor(Color.parseColor("#f20044"), Color.parseColor("#cccccc"));
//
//        new SplashPresenter(this, this);
//
//        if (null == SharedPreferencesUtils.getString(this, "isFirstOpen", "") || SharedPreferencesUtils.getString(this, "isFirstOpen", "").trim().length() <= 0) {
//            if (!Utils.isNetUsable(SplashActivity.this)) {
//                Toast.makeText(this, getResources().getString(R.string.net_unusable), Toast.LENGTH_SHORT).show();
//                return;
//            }
//            mPresenter.appRegister(Utils.getDeviceId(this), "182c79dbf8871689878b0de620006ea3", "7ed99e89c4831f169627b5d20a0020f7c1a9b026244e6364ac1c12a9fa2314fe");
//        }
//    }
}
