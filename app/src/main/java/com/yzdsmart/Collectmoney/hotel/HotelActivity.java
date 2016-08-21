package com.yzdsmart.Collectmoney.hotel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

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
    TabLayout hotelInfoToggleTL;
    @Nullable
    @BindView(R.id.hotel_info_pager)
    ViewPager hotelInfoPagerVP;

    private String[] hotelTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hotelTabs = getResources().getStringArray(R.array.hotel_tab);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.qr_code_icon));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_hotel;
    }
}
