package com.yzdsmart.Collectmoney.main.find_money;

import android.os.Bundle;

import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;

/**
 * Created by YZD on 2016/8/17.
 */
public class FindMoneyFragment extends BaseFragment {

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_find_money;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("-------FindMoneyFragment-----------onActivityCreated----------------------");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("----------FindMoneyFragment--------onResume----------------------");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("------------------------------------FindMoneyFragment is hidden " + hidden);
    }
}
