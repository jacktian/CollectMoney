package com.yzdsmart.Dingdingwen.coupon_log;

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
import com.yzdsmart.Dingdingwen.bean.CouponLog;
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
 * Created by YZD on 2016/12/26.
 */

public class CouponLogActivity extends BaseActivity implements CouponLogContract.CouponLogView {
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
    @BindView(R.id.coupon_log_list)
    UltimateRecyclerView couponLogListRV;

    private static final String TAG = "CouponLogActivity";

    private Integer userType;//0 个人 1 商铺

    private CouponLogContract.CouponLogPresenter mPresenter;

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 15;
    private Integer lastsequence = 0;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<CouponLog> couponLogList;
    private CouponLogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        couponLogList = new ArrayList<CouponLog>();

        if (null != savedInstanceState) {
            userType = savedInstanceState.getInt("userType");
        } else {
            userType = getIntent().getExtras().getInt("userType");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("兑换日志");

        new CouponLogPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.divider_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        logAdapter = new CouponLogAdapter(this);
        couponLogListRV.setHasFixedSize(true);
        couponLogListRV.setLayoutManager(mLinearLayoutManager);
        couponLogListRV.addItemDecoration(dividerItemDecoration);
        couponLogListRV.setAdapter(logAdapter);
        couponLogListRV.reenableLoadmore();
        couponLogListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getCouponLogs();
            }
        });
        couponLogListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                couponLogListRV.setRefreshing(false);
                couponLogListRV.reenableLoadmore();
                lastsequence = 0;
                pageIndex = 1;
                logAdapter.clearList();
                getCouponLogs();
            }
        });

        getCouponLogs();
    }

    private void getCouponLogs() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        switch (userType) {
            case 0:
                mPresenter.getPersonalCouponLogs(Constants.PERSONAL_COUPON_LOG_ACTION_CODE, "", SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case 1:
                mPresenter.getPersonalCouponLogs(Constants.SHOP_COUPON_LOG_ACTION_CODE, "", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_coupon_log;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("userType", userType);
        super.onSaveInstanceState(outState);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(final View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    @Override
    public void onGetCouponLogs(Integer sequence, List<CouponLog> paymentLogs) {
        lastsequence = sequence;
        pageIndex++;
        if (paymentLogs.size() < PAGE_SIZE) {
            couponLogListRV.disableLoadmore();
        }
        if (paymentLogs.size() <= 0) {
            if (2 == pageIndex) {
                showSnackbar("没有数据,下拉刷新");
            }
            return;
        }
        couponLogList.clear();
        couponLogList.addAll(paymentLogs);
        logAdapter.appendList(couponLogList);
    }

    @Override
    public void setPresenter(CouponLogContract.CouponLogPresenter presenter) {
        mPresenter = presenter;
    }
}
