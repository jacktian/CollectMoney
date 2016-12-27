package com.yzdsmart.Dingdingwen.payment_log;

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
import com.yzdsmart.Dingdingwen.bean.PaymentLog;
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

public class PaymentLogActivity extends BaseActivity implements PaymentLogContract.PaymentLogView {
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
    @BindView(R.id.payment_log_list)
    UltimateRecyclerView paymentLogListRV;

    private static final String TAG = "PaymentLogActivity";

    private PaymentLogContract.PaymentLogPresenter mPresenter;

    private Integer userType;//0 个人 1 商铺

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 15;
    private Integer lastsequence = 0;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    private LinearLayoutManager mLinearLayoutManager;
    private List<PaymentLog> paymentLogList;
    private PaymentLogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paymentLogList = new ArrayList<PaymentLog>();

        if (null != savedInstanceState) {
            userType = savedInstanceState.getInt("userType");
        } else {
            userType = getIntent().getExtras().getInt("userType");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("付款日志");

        new PaymentLogPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        mLinearLayoutManager = new LinearLayoutManager(this);

        logAdapter = new PaymentLogAdapter(this);
        paymentLogListRV.setHasFixedSize(true);
        paymentLogListRV.setLayoutManager(mLinearLayoutManager);
        paymentLogListRV.setAdapter(logAdapter);
        paymentLogListRV.reenableLoadmore();
        paymentLogListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getPaymentLogs();
            }
        });
        paymentLogListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                paymentLogListRV.setRefreshing(false);
                paymentLogListRV.reenableLoadmore();
                lastsequence = 0;
                pageIndex = 1;
                logAdapter.clearList();
                getPaymentLogs();
            }
        });

        getPaymentLogs();
    }

    private void getPaymentLogs() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        switch (userType) {
            case 0:
                mPresenter.getPersonalPaymentLogs(Constants.PERSONAL_PAYMENT_LOG_ACTION_CODE, "", SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case 1:
                mPresenter.getShopPaymentLogs(Constants.SHOP_PAYMENT_LOG_ACTION_CODE, "", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_payment_log;
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
    public void onGetPaymentLogs(Integer sequence, List<PaymentLog> paymentLogs) {
        lastsequence = sequence;
        pageIndex++;
        if (paymentLogs.size() < PAGE_SIZE) {
            paymentLogListRV.disableLoadmore();
        }
        if (paymentLogs.size() <= 0) {
            if (2 == pageIndex) {
                showSnackbar("没有数据,下拉刷新");
            }
            return;
        }
        paymentLogList.clear();
        paymentLogList.addAll(paymentLogs);
        logAdapter.appendList(paymentLogList);
    }

    @Override
    public void setPresenter(PaymentLogContract.PaymentLogPresenter presenter) {
        mPresenter = presenter;
    }
}
