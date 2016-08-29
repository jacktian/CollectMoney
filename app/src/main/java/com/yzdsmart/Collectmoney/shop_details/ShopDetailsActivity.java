package com.yzdsmart.Collectmoney.shop_details;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ShopFollower;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/21.
 */
public class ShopDetailsActivity extends BaseActivity implements ShopDetailsContract.ShopDetailsView {
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
    @BindView(R.id.hotel_user_list)
    RecyclerView hotelUsersRV;
    @Nullable
    @BindView(R.id.hotel_address)
    TextView hotelAddressTV;
    @Nullable
    @BindView(R.id.hotel_name)
    TextView hotelNameTV;
    @Nullable
    @BindView(R.id.focus_person_counts)
    TextView focusPersonCountsTV;
    @Nullable
    @BindView(R.id.daily_coin_counts)
    TextView dailyCoinCountsTV;
    @Nullable
    @BindView(R.id.visit_person_counts)
    TextView visitPersonCountsTV;
    @Nullable
    @BindView(R.id.is_atte)
    ImageView isAtteIV;

    private String bazaCode;//商铺编码
    private Boolean isAtte = false;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    List<ShopFollower> hotelFollowerList;
    private ShopFollowerAdapter hotelFollowerAdapter;

    private ShopDetailsContract.ShopDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hotelFollowerList = new ArrayList<ShopFollower>();

        bazaCode = getIntent().getExtras().getString("bazaCode");

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

        hotelFollowerAdapter = new ShopFollowerAdapter(this);
        hotelUsersRV.setHasFixedSize(true);
        hotelUsersRV.setLayoutManager(mLinearLayoutManager);
        hotelUsersRV.addItemDecoration(dividerItemDecoration);
        hotelUsersRV.setAdapter(hotelFollowerAdapter);

        new ShopDetailsPresenter(this, this);

        mPresenter.getShopInfo("000000", "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""));

        List<ShopFollower> list = new ArrayList<ShopFollower>();
        list.add(new ShopFollower("file:///android_asset/album_pic.png", "艾伦", 10, "08:23"));
        list.add(new ShopFollower("file:///android_asset/album_pic.png", "嗣位", 22, "10:45"));
        list.add(new ShopFollower("file:///android_asset/album_pic.png", "木樨", 13, "12:30"));
        list.add(new ShopFollower("file:///android_asset/album_pic.png", "提姆", 41, "14:58"));
        list.add(new ShopFollower("file:///android_asset/album_pic.png", "韩梅梅", 8, "23:08"));

        hotelFollowerList.addAll(list);
        hotelFollowerAdapter.appenList(hotelFollowerList);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_details;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.is_atte})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.is_atte:
                mPresenter.setFollow(isAtte ? "56" : "66", "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), bazaCode);
                break;
        }
    }

    @Override
    public void setPresenter(ShopDetailsContract.ShopDetailsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetShopInfo(ShopInfoRequestResponse shopDetails) {
        hotelNameTV.setText(shopDetails.getName());
        hotelAddressTV.setText(shopDetails.getAddr());
        focusPersonCountsTV.setText("" + shopDetails.getAtteNum());
        dailyCoinCountsTV.setText("" + shopDetails.getTodayGlodNum());
        visitPersonCountsTV.setText("" + shopDetails.getVisiNum());
        isAtte = shopDetails.getAtte();
        isAtteIV.setImageDrawable(isAtte ? getResources().getDrawable(R.mipmap.heart_icon_white_checked) : getResources().getDrawable(R.mipmap.heart_icon_white));
    }

    @Override
    public void onSetFollow(Boolean flag, String action, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        isAtte = "56".equals(action) ? false : true;
        isAtteIV.setImageDrawable(isAtte ? getResources().getDrawable(R.mipmap.heart_icon_white_checked) : getResources().getDrawable(R.mipmap.heart_icon_white));
    }
}
