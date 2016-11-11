package com.yzdsmart.Dingdingwen.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/10/17.
 */

public class SplashFragment_3 extends Fragment {
    private static final String TAG = "SplashFragment_3";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash_3, container, false);
        Button button = (Button) v.findViewById(R.id.start_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SplashActivity) getActivity()).startMain();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
}
