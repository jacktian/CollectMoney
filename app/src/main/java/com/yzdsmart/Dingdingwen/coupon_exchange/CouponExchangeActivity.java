package com.yzdsmart.Dingdingwen.coupon_exchange;

import android.app.Dialog;
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
import com.yzdsmart.Dingdingwen.bean.CouponBean;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.ExchangeCouponDialog;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/12/19.
 */

public class CouponExchangeActivity extends BaseActivity implements CouponExchangeContract.CouponExchangeView {
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
    @BindView(R.id.coin_counts)
    TextView coinCountsTV;
    @Nullable
    @BindView(R.id.coupon_list)
    UltimateRecyclerView couponListRV;

    private final static String TAG = "CouponExchangeActivity";

    private Integer couponType;//0 指定商铺兑换列表 1 指定金币类型可兑换列表
    private String bazaCode;
    private Integer goldType;

    private DecimalFormat decimalFormat;

    private CouponExchangeContract.CouponExchangePresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private CouponExchangeAdapter couponExchangeAdapter;

    private Dialog exchangeCouponDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        if (null != savedInstanceState) {
            couponType = savedInstanceState.getInt("couponType");
            if (null != couponType) {
                switch (couponType) {
                    case 0:
                        bazaCode = savedInstanceState.getString("bazaCode");
                        break;
                    case 1:
                        goldType = savedInstanceState.getInt("goldType");
                        break;
                }
            }
        } else {
            Bundle bundle = getIntent().getExtras();
            couponType = bundle.getInt("couponType");
            if (null != couponType) {
                switch (couponType) {
                    case 0:
                        bazaCode = bundle.getString("bazaCode");
                        break;
                    case 1:
                        goldType = bundle.getInt("goldType");
                        break;
                }
            }
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText(getResources().getString(R.string.exchange_title));

        new CouponExchangePresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        mLinearLayoutManager = new LinearLayoutManager(this);
        couponExchangeAdapter = new CouponExchangeAdapter(this);
        couponListRV.setHasFixedSize(true);
        couponListRV.setLayoutManager(mLinearLayoutManager);
        couponListRV.setAdapter(couponExchangeAdapter);
        couponListRV.setSaveEnabled(true);
        couponListRV.setClipToPadding(false);
        couponListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                couponListRV.setRefreshing(false);
                couponExchangeAdapter.clearList();
                getCouponList();
            }
        });

        getCoinCounts();
        getCouponList();
    }

    private void getCoinCounts() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        if (SharedPreferencesUtils.getString(CouponExchangeActivity.this, "baza_code", "").trim().length() > 0) {
            if (null != couponType) {
                switch (couponType) {
                    case 0:
                        mPresenter.getShopLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), -1, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                        break;
                    case 1:
                        mPresenter.getShopLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), goldType, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                        break;
                }
            }
        } else {
            if (null != couponType) {
                switch (couponType) {
                    case 0:
                        mPresenter.getPersonalLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, Constants.PERSONAL_WITHDRAW_ACTION_TYPE_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), -1, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                        break;
                    case 1:
                        mPresenter.getPersonalLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, Constants.PERSONAL_WITHDRAW_ACTION_TYPE_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), goldType, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                        break;
                }
            }
        }
    }

    private void getCouponList() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        if (null != couponType) {
            switch (couponType) {
                case 0:
                    mPresenter.getShopExchangeList(Constants.SHOP_COUPON_ACTION_CODE, "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                    break;
                case 1:
                    mPresenter.getCoinExchangeList(Constants.COIN_COUPON_ACTION_CODE, "000000", goldType, SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                    break;
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_coupon_exchange;
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("couponType", couponType);
        switch (couponType) {
            case 0:
                outState.putString("bazaCode", bazaCode);
                break;
            case 1:
                outState.putInt("goldType", goldType);
                break;
        }
        super.onSaveInstanceState(outState);
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

    public void exchangeCoupon(final CouponBean couponBean) {
        exchangeCouponDialog = new ExchangeCouponDialog(this, couponBean);
        exchangeCouponDialog.show();
        exchangeCouponDialog.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != exchangeCouponDialog) {
                    exchangeCouponDialog.dismiss();
                    exchangeCouponDialog = null;
                }
                if (!Utils.isNetUsable(CouponExchangeActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.exchangeCoupon("000000", couponBean.getExchangeId(), couponBean.getGoldNum(), SharedPreferencesUtils.getString(CouponExchangeActivity.this, "cust_code", ""), SharedPreferencesUtils.getString(CouponExchangeActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(CouponExchangeActivity.this, "ddw_access_token", ""));
            }
        });
        exchangeCouponDialog.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != exchangeCouponDialog) {
                    exchangeCouponDialog.dismiss();
                    exchangeCouponDialog = null;
                }
            }
        });
    }

    @Override
    public void onGetPersonalLeftCoins(Double goldNum) {
        coinCountsTV.setText(decimalFormat.format(goldNum));
    }

    @Override
    public void onGetShopLeftCoins(Double goldNum) {
        coinCountsTV.setText(decimalFormat.format(goldNum));
    }

    @Override
    public void onGetExchangeList(List<CouponBean> couponBeans) {
        couponExchangeAdapter.appendList(couponBeans);
    }

    @Override
    public void onExchangeCoupon(Double goldNum) {
        showSnackbar("兑换成功");
        coinCountsTV.setText(decimalFormat.format((Double.valueOf(coinCountsTV.getText().toString().trim()) - goldNum)));
        showShare();
    }

    @Override
    public void setPresenter(CouponExchangeContract.CouponExchangePresenter presenter) {
        mPresenter = presenter;
    }
}
