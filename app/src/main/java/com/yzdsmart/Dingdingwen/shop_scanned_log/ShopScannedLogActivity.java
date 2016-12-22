package com.yzdsmart.Dingdingwen.shop_scanned_log;

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
import com.yzdsmart.Dingdingwen.bean.ScannedLog;
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

    private static final String TAG = "ShopScannedLogActivity";

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;
    private Integer lastsequence = 0;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

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
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("我的打赏");

        new ShopScannedLogPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.divider_grey));
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
                getScannedLog();
            }
        });
        scannedLogRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scannedLogRV.setRefreshing(false);
                scannedLogRV.reenableLoadmore();
                pageIndex = 1;
                lastsequence = 0;
                shopScannedLogAdapter.clearList();
                getScannedLog();
            }
        });

        getScannedLog();
    }

    private void getScannedLog() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getScannedLog(Constants.GET_SCANNED_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_scanned_log;
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
    public void onGetScannedLog(List<ScannedLog> scannedLogs, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        if (scannedLogs.size() < PAGE_SIZE) {
            scannedLogRV.disableLoadmore();
        }
        if (scannedLogs.size() <= 0) {
            if (2 == pageIndex) {
                showSnackbar("没有数据,下拉刷新");
            }
            return;
        }
        scannedLogList.clear();
        scannedLogList.addAll(scannedLogs);
        shopScannedLogAdapter.appendList(scannedLogList);
    }
}
