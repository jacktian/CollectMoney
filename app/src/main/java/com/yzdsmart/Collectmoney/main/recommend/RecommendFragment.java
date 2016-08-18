package com.yzdsmart.Collectmoney.main.recommend;

import android.os.Bundle;

import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;

/**
 * Created by YZD on 2016/8/17.
 */
public class RecommendFragment extends BaseFragment {
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("-----------RecommendFragment-------onActivityCreated----------------------");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("------RecommendFragment------------onResume----------------------");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("------------------------------------RecommendFragment is hidden " + hidden);
    }
}
