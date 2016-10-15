package com.yzdsmart.Collectmoney.splash;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class SplashActivity extends BaseActivity {
    @Nullable
    @BindView(R.id.splash_banner)
    ConvenientBanner splashBanner;
    @Nullable
    @BindView(R.id.start_app)
    Button startApp;
//    private Handler splashHandler;
//    private Runnable splashRunnable;

    private ArrayList<Integer> localImages;//默认banner图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localImages = new ArrayList<Integer>();
        localImages.add(R.mipmap.splash_1);
        localImages.add(R.mipmap.splash_2);
        localImages.add(R.mipmap.splash_3);

        splashBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        splashBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(position + "-----------" + (localImages.size() - 1));
                if (position == (localImages.size() - 1)) {
                    ButterKnife.apply(startApp, BUTTERKNIFEVISIBLE);
                } else {
                    ButterKnife.apply(startApp, BUTTERKNIFEGONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        splashHandler = new Handler();
//        splashRunnable = new Runnable() {
//            @Override
//            public void run() {
//                openActivity(MainActivity.class);
//                closeActivity();
//            }
//        };
//        splashHandler.postDelayed(splashRunnable, 1000);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Optional
    @OnClick({R.id.start_app})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_app:
                openActivity(MainActivity.class);
                closeActivity();
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        splashHandler.removeCallbacks(splashRunnable);
        super.onDestroy();
    }

    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
        }
    }
}
