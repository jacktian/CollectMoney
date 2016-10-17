package com.yzdsmart.Collectmoney.scan_coin_log;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.GetCoinsLog;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.views.BetterSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/5.
 */
public class ScanCoinsLogActivity extends BaseActivity implements ScanCoinsLogContract.ScanCoinsLogView {
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
    @BindView(R.id.coin_list)
    UltimateRecyclerView coinListRV;
    @Nullable
    @BindView(R.id.log_filter)
    BetterSpinner logFilterSpinner;

    private static final String GET_COIN_LOG_CODE = "1666";
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;
    private Integer lastsequence = 0;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    private ScanCoinsLogContract.ScanCoinsLogPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<GetCoinsLog> logList;
    private ScanCoinsLogAdapter personalCoinsAdapter;

    private String[] logFilters;
    private ArrayAdapter<String> logFilterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logList = new ArrayList<GetCoinsLog>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("获取金币详情");

        new ScanCoinsLogPresenter(this, this);

        logFilters = getResources().getStringArray(R.array.log_filter_array);
        logFilterAdapter = new ArrayAdapter<String>(this,
                R.layout.cities_list_item, logFilters);
        logFilterSpinner.setAdapter(logFilterAdapter);
        logFilterSpinner.setText(logFilters[0]);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        personalCoinsAdapter = new ScanCoinsLogAdapter(this);
        coinListRV.setHasFixedSize(true);
        coinListRV.setLayoutManager(mLinearLayoutManager);
        coinListRV.addItemDecoration(dividerItemDecoration);
        coinListRV.setAdapter(personalCoinsAdapter);
        coinListRV.reenableLoadmore();
        coinListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                mPresenter.getCoinsLog(GET_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(ScanCoinsLogActivity.this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence);
            }
        });
        coinListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                coinListRV.setRefreshing(false);
                coinListRV.reenableLoadmore();
                lastsequence = 0;
                pageIndex = 1;
                personalCoinsAdapter.clearList();
                mPresenter.getCoinsLog(GET_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(ScanCoinsLogActivity.this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence);
            }
        });

        mPresenter.getCoinsLog(GET_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_scan_coins_log;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.publish_task})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    @Override
    public void onGetCoinsLog(List<GetCoinsLog> logList, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        this.logList.clear();
        this.logList.addAll(logList);
        personalCoinsAdapter.appendList(this.logList);
        if (logList.size() < PAGE_SIZE) {
            coinListRV.disableLoadmore();
        }
    }

    @Override
    public void setPresenter(ScanCoinsLogContract.ScanCoinsLogPresenter presenter) {
        mPresenter = presenter;
    }
}
