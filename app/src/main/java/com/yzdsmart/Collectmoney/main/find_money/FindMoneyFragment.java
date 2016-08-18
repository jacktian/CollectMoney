package com.yzdsmart.Collectmoney.main.find_money;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.main.personal.PersonalFragment;
import com.yzdsmart.Collectmoney.main.qr_scanner.QRScannerFragment;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class FindMoneyFragment extends BaseFragment {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;

    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_find_money;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.apply(hideViews, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
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

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
    void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                fragment = fm.findFragmentByTag("personal");
                if (null == fragment) {
                    fragment = new PersonalFragment();
                }
                ((MainActivity) getActivity()).addOrShowFragment(fragment, "personal");
                break;
            case R.id.title_right_operation_layout:
                fragment = fm.findFragmentByTag("qr_scan");
                if (null == fragment) {
                    fragment = new QRScannerFragment();
                }
                ((MainActivity) getActivity()).addOrShowFragment(fragment, "qr_scan");
                break;
        }
    }
}
