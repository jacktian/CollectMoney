package com.yzdsmart.Collectmoney.hotel;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.hotel.hotel_detail.HotelDetailFragment;
import com.yzdsmart.Collectmoney.hotel.hotel_info.HotelInfoFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/21.
 */
public class HotelActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.hotel_info_toggle)
    RadioGroup hotelToggleRG;

    private FragmentManager fm;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.qr_code_icon));

        initView();
    }

    private void initView() {
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = new HotelInfoFragment();
        ft.add(R.id.hotel_info_pager, mCurrentFragment, "info");
        ft.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_hotel;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    @Optional
    @OnCheckedChanged({R.id.toggle_hotel_info, R.id.toggle_hotel_detail})
    void onCheckedChanged(CompoundButton button, boolean changed) {
        Fragment fragment;
        switch (button.getId()) {
            case R.id.toggle_hotel_info:
                fragment = fm.findFragmentByTag("detail");
                if (null == fragment) {
                    fragment = new HotelDetailFragment();
                }
                addOrShowFragment(fragment, "detail");
                break;
            case R.id.toggle_hotel_detail:
                fragment = fm.findFragmentByTag("info");
                if (null == fragment) {
                    fragment = new HotelInfoFragment();
                }
                addOrShowFragment(fragment, "info");
                break;
        }
    }

    /**
     * 添加或者显示碎片
     *
     * @param fragment
     * @param tag
     */
    public void addOrShowFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        if (!fragment.isAdded()) {
            ft.hide(mCurrentFragment).add(R.id.hotel_info_pager, fragment, tag);
        } else {
            ft.hide(mCurrentFragment).show(fragment);
        }
        ft.commit();
        mCurrentFragment = fragment;
    }
}
