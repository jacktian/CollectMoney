package com.yzdsmart.Dingdingwen.focused_shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.FocusedShop;
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
 * Created by YZD on 2016/9/19.
 */
public class FocusedShopActivity extends BaseActivity implements FocusedShopContract.FocusedShopView {
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
    @BindView(R.id.focused_shop_list)
    UltimateRecyclerView focusedShopRV;

    private static final String TAG = "FocusedShopActivity";

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private FocusedShopContract.FocusedShopPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<FocusedShop> focusedShopList;
    private FocusedShopAdapter focusedShopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        focusedShopList = new ArrayList<FocusedShop>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText(getResources().getString(R.string.focus_shop));

        new FocusedShopPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        mLinearLayoutManager = new LinearLayoutManager(this);
        focusedShopAdapter = new FocusedShopAdapter(this);
        focusedShopRV.setHasFixedSize(true);
        focusedShopRV.setLayoutManager(mLinearLayoutManager);
        focusedShopRV.setAdapter(focusedShopAdapter);
        focusedShopRV.reenableLoadmore();
        focusedShopRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getFocusedShopList();
            }
        });
        focusedShopRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                focusedShopRV.setRefreshing(false);
                focusedShopRV.reenableLoadmore();
                pageIndex = 1;
                focusedShopAdapter.clearList();
                getFocusedShopList();
            }
        });

        getFocusedShopList();
    }

    private void getFocusedShopList() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getFocusedShopList(Constants.GET_FOCUSED_SHOP_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_focused_shop;
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
    public void onGetFocusedShopList(List<FocusedShop> focusedShops) {
        pageIndex++;
        if (focusedShops.size() < PAGE_SIZE) {
            focusedShopRV.disableLoadmore();
        }
        if (focusedShops.size() <= 0) {
            if (2 == pageIndex) {
                showSnackbar("没有数据,下拉刷新");
            }
            return;
        }
        focusedShopList.clear();
        focusedShopList.addAll(focusedShops);
        focusedShopAdapter.appendList(focusedShopList);

    }

    @Override
    public void setPresenter(FocusedShopContract.FocusedShopPresenter presenter) {
        mPresenter = presenter;
    }
}
