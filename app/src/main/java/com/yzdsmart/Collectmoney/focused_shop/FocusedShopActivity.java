package com.yzdsmart.Collectmoney.focused_shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.FocusedShop;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

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

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final String GET_ACTION_CODE = "9212";//获取用户关注的店铺信息

    private FocusedShopContract.FocusedShopPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<FocusedShop> focusedShopList;
    private FocusedShopAdapter focusedShopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        focusedShopList = new ArrayList<FocusedShop>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText(getResources().getString(R.string.focus_shop));

        new FocusedShopPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        focusedShopAdapter = new FocusedShopAdapter(this);
        focusedShopRV.setHasFixedSize(true);
        focusedShopRV.setLayoutManager(mLinearLayoutManager);
        focusedShopRV.setAdapter(focusedShopAdapter);
        focusedShopRV.reenableLoadmore();
        focusedShopRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                mPresenter.getFocusedShopList(GET_ACTION_CODE, "000000", SharedPreferencesUtils.getString(FocusedShopActivity.this, "cust_code", ""), pageIndex, PAGE_SIZE);
            }
        });
        focusedShopRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                focusedShopRV.setRefreshing(false);
                pageIndex = 1;
                focusedShopAdapter.clearList();
                mPresenter.getFocusedShopList(GET_ACTION_CODE, "000000", SharedPreferencesUtils.getString(FocusedShopActivity.this, "cust_code", ""), pageIndex, PAGE_SIZE);
            }
        });

        mPresenter.getFocusedShopList(GET_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_focused_shop;
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
        focusedShopList.clear();
        focusedShopList.addAll(focusedShops);
        focusedShopAdapter.appendList(focusedShopList);
        pageIndex++;
    }

    @Override
    public void setPresenter(FocusedShopContract.FocusedShopPresenter presenter) {
        mPresenter = presenter;
    }
}
