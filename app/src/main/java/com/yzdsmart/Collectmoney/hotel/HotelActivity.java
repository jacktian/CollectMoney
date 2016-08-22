package com.yzdsmart.Collectmoney.hotel;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.HotelFollower;

import java.util.ArrayList;
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
    @Nullable
    @BindViews({R.id.hotel_base_info_layout, R.id.hotel_detail_introduction_layout})
    List<View> toggleViews;
    @Nullable
    @BindView(R.id.hotel_user_list)
    RecyclerView hotelUsersRV;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    List<HotelFollower> hotelFollowerList;
    private HotelFollowerAdapter hotelFollowerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hotelFollowerList = new ArrayList<HotelFollower>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.qr_code_icon));
        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        hotelFollowerAdapter = new HotelFollowerAdapter(this);
        hotelUsersRV.setHasFixedSize(true);
        hotelUsersRV.setLayoutManager(mLinearLayoutManager);
        hotelUsersRV.addItemDecoration(dividerItemDecoration);
        hotelUsersRV.setAdapter(hotelFollowerAdapter);

        List<HotelFollower> list = new ArrayList<HotelFollower>();
        list.add(new HotelFollower("file:///android_asset/album_pic.png", "艾伦", 10, "08:23"));
        list.add(new HotelFollower("file:///android_asset/album_pic.png", "嗣位", 22, "10:45"));
        list.add(new HotelFollower("file:///android_asset/album_pic.png", "木樨", 13, "12:30"));
        list.add(new HotelFollower("file:///android_asset/album_pic.png", "提姆", 41, "14:58"));
        list.add(new HotelFollower("file:///android_asset/album_pic.png", "韩梅梅", 8, "23:08"));

        hotelFollowerList.addAll(list);
        hotelFollowerAdapter.appenList(hotelFollowerList);
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
        switch (button.getId()) {
            case R.id.toggle_hotel_info:
                ButterKnife.apply(toggleViews, BUTTERKNIFEVISIBLE);
                break;
            case R.id.toggle_hotel_detail:
                ButterKnife.apply(toggleViews, BUTTERKNIFEGONE);
                break;
        }
    }
}
