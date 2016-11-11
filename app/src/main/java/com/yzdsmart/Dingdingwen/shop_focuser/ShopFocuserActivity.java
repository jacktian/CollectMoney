package com.yzdsmart.Dingdingwen.shop_focuser;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.ShopFocuser;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/20.
 */

public class ShopFocuserActivity extends BaseActivity implements ShopFocuserContract.ShopFocuserView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.focuser_counts)
    TextView focuserCountsTV;
    @Nullable
    @BindView(R.id.shop_focuser_list)
    UltimateRecyclerView shopFocuserRV;

    private static final String TAG = "ShopFocuserActivity";

    private Integer focuserCounts = 0;
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<ShopFocuser> shopFocuserList;
    private ShopFocuserAdapter shopFocuserAdapter;

    private ShopFocuserContract.ShopFocuserPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shopFocuserList = new ArrayList<ShopFocuser>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("我的蚊客");

        new ShopFocuserPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        shopFocuserAdapter = new ShopFocuserAdapter(this);
        shopFocuserRV.setHasFixedSize(true);
        shopFocuserRV.setLayoutManager(mLinearLayoutManager);
        shopFocuserRV.addItemDecoration(dividerItemDecoration);
        shopFocuserRV.setAdapter(shopFocuserAdapter);
        shopFocuserRV.reenableLoadmore();
        shopFocuserRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getShopFocuser();
            }
        });
        shopFocuserRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shopFocuserRV.setRefreshing(false);
                shopFocuserRV.reenableLoadmore();
                focuserCounts = 0;
                pageIndex = 1;
                shopFocuserAdapter.clearList();
                getShopFocuser();
            }
        });

        getShopFocuser();
    }

    private void getShopFocuser() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopFocuser(Constants.GET_SHOP_FOCUSER_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_focuser;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
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

    @Override
    public void onGetShopFocuser(List<ShopFocuser> shopFocusers) {
        pageIndex++;
        focuserCounts += shopFocusers.size();
        focuserCountsTV.setText("" + focuserCounts);
        if (shopFocusers.size() < PAGE_SIZE) {
            shopFocuserRV.disableLoadmore();
        }
        if (shopFocusers.size() <= 0) return;
        shopFocuserList.clear();
        shopFocuserList.addAll(shopFocusers);
        shopFocuserAdapter.appendList(shopFocuserList);
    }

    @Override
    public void setPresenter(ShopFocuserContract.ShopFocuserPresenter presenter) {
        mPresenter = presenter;
    }
}
