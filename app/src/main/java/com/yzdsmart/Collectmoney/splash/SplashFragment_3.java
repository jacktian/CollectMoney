package com.yzdsmart.Collectmoney.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yzdsmart.Collectmoney.R;

/**
 * Created by YZD on 2016/10/17.
 */

public class SplashFragment_3 extends Fragment {
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
}
