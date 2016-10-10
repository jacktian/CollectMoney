package com.yzdsmart.Collectmoney.shop_scanned_log;

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
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ScannedLog;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/10/9.
 */

public class ShopScannedLogActivity extends BaseActivity implements ShopScannedLogContract.ShopScannedLogView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.scanned_log_list)
    UltimateRecyclerView scannedLogRV;

    private final static String GET_SCANNED_LOG_ACTION_CODE = "1688";
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<ScannedLog> scannedLogList;
    private ShopScannedLogAdapter shopScannedLogAdapter;

    private ShopScannedLogContract.ShopScannedLogPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannedLogList = new ArrayList<ScannedLog>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("获取被扫日志列表");

        new ShopScannedLogPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        shopScannedLogAdapter = new ShopScannedLogAdapter(this);
        scannedLogRV.setHasFixedSize(true);
        scannedLogRV.setLayoutManager(mLinearLayoutManager);
        scannedLogRV.addItemDecoration(dividerItemDecoration);
        scannedLogRV.setAdapter(shopScannedLogAdapter);
        scannedLogRV.reenableLoadmore();
        scannedLogRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                mPresenter.getScannedLog(GET_SCANNED_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(ShopScannedLogActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE);
            }
        });
        scannedLogRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scannedLogRV.setRefreshing(false);
                pageIndex = 1;
                shopScannedLogAdapter.clearList();
                mPresenter.getScannedLog(GET_SCANNED_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(ShopScannedLogActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE);
            }
        });

        mPresenter.getScannedLog(GET_SCANNED_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_scanned_log;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
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
    public void setPresenter(ShopScannedLogContract.ShopScannedLogPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetScannedLog(List<ScannedLog> scannedLogs) {
        scannedLogList.clear();
        scannedLogList.addAll(scannedLogs);
        shopScannedLogAdapter.appendList(scannedLogList);
        pageIndex++;
    }
}
