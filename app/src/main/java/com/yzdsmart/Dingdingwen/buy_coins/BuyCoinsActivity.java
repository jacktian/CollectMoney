package com.yzdsmart.Dingdingwen.buy_coins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BuyCoinParameter;
import com.yzdsmart.Dingdingwen.bean.BuyCoinsLog;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.bean.ShopPayLog;
import com.yzdsmart.Dingdingwen.publish_tasks.PublishTasksActivity;
import com.yzdsmart.Dingdingwen.utils.AmountInputFilter;
import com.yzdsmart.Dingdingwen.utils.NetworkUtils;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.CustomNestRadioGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/4.
 */
public class BuyCoinsActivity extends BaseActivity implements BuyCoinsContract.BuyCoinsView, CustomNestRadioGroup.OnCheckedChangeListener {
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
    @BindView(R.id.coin_types)
    Spinner coinTypesBS;
    @Nullable
    @BindView(R.id.coin_counts)
    EditText coinCountsET;
    @Nullable
    @BindView(R.id.coin_list)
    UltimateRecyclerView coinListRV;
    @Nullable
    @BindView(R.id.pay_type_group)
    CustomNestRadioGroup payTypeGroup;

    private static final String TAG = "BuyCoinsActivity";

    private Integer userType;//0 个人 1 商家

    private Gson gson = new Gson();

    private static final Float GOLD_FORMAT_RMB_RATIO = 1.0f;

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付宝支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";

    private Integer payType = 1;//支付方式 0 银联 1 微信 2 支付宝

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;
    private Integer lastsequence = 0;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;

//    private List<BuyCoinsLog> logList;
//    private BuyCoinsLogAdapter buyCoinsLogAdapter;

    private List<ShopPayLog> logList;
    private ShopPayLogAdapter shopPayLogAdapter;

    private BuyCoinsContract.BuyCoinsPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable buySuccessRunnable;

    private CoinTypesAdapter coinTypesAdapter;
    private CoinType defaultCoinType;
    private CoinType selectedType;
    private List<CoinType> coinTypeList;

    private InputFilter[] amountFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logList = new ArrayList<ShopPayLog>();
        coinTypeList = new ArrayList<CoinType>();

        if (null != savedInstanceState) {
            userType = savedInstanceState.getInt("userType");
        } else {
            userType = getIntent().getExtras().getInt("userType");
        }

//        amountFilters = new InputFilter[]{new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                if (source.equals(".") && dest.toString().length() == 0) {
//                    return "0.";
//                }
//                if (dest.toString().contains(".")) {
//                    int index = dest.toString().indexOf(".");
//                    int mlength = dest.toString().substring(index).length();
//                    if (mlength == 3) {
//                        return "";
//                    }
//                }
//                if (dest.toString().length() > 9) {
//                    return "";
//                }
//                return null;
//            }
//        }};

        amountFilters = new InputFilter[]{new AmountInputFilter()};

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        switch (userType) {
            case 0:
                centerTitleTV.setText("个人购买金币");
                break;
            case 1:
                centerTitleTV.setText("商铺购买金币");
                break;
        }
        payTypeGroup.setOnCheckedChangeListener(this);
        coinCountsET.setFilters(amountFilters);

        new BuyCoinsPresenter(this, this);

//        initPingPlusPlus();

        MobclickAgent.openActivityDurationTrack(false);

        defaultCoinType = new CoinType(0, 0d, "普通金币", "");
        coinTypesAdapter = new CoinTypesAdapter(this);
        coinTypesBS.setAdapter(coinTypesAdapter);
        coinTypesBS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = coinTypeList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buySuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                openActivity(PublishTasksActivity.class);
                closeActivity();
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.divider_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

//        buyCoinsLogAdapter = new BuyCoinsLogAdapter(this);
//        coinListRV.setHasFixedSize(true);
//        coinListRV.setLayoutManager(mLinearLayoutManager);
//        coinListRV.addItemDecoration(dividerItemDecoration);
//        coinListRV.setAdapter(buyCoinsLogAdapter);
//        coinListRV.reenableLoadmore();
//        coinListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                getBuyCoinsLog();
//            }
//        });
//        coinListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                coinListRV.setRefreshing(false);
//                coinListRV.reenableLoadmore();
//                pageIndex = 1;
//                lastsequence = 0;
//                buyCoinsLogAdapter.clearList();
//                getBuyCoinsLog();
//            }
//        });

        shopPayLogAdapter = new ShopPayLogAdapter(this);
        coinListRV.setHasFixedSize(true);
        coinListRV.setLayoutManager(mLinearLayoutManager);
        coinListRV.addItemDecoration(dividerItemDecoration);
        coinListRV.setAdapter(shopPayLogAdapter);
        coinListRV.reenableLoadmore();
        coinListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getShopPayLog();
            }
        });
        coinListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                coinListRV.setRefreshing(false);
                coinListRV.reenableLoadmore();
                pageIndex = 1;
                lastsequence = 0;
                shopPayLogAdapter.clearList();
                getShopPayLog();
            }
        });

        getCoinTypes();
        getShopPayLog();
    }

    public void getNotPayCharge(ShopPayLog log) {
        if ("未支付".equals(log.getPayStatus())) {
            if (!Utils.isNetUsable(BuyCoinsActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.getNotPayCharge("000000", log.getChargeId(), SharedPreferencesUtils.getString(BuyCoinsActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(BuyCoinsActivity.this, "ddw_access_token", ""));
        }
    }

    private void getCoinTypes() {
        switch (userType) {
            case 0:
                coinTypesAdapter.appendList(Collections.singletonList(defaultCoinType));
                break;
            case 1:
                if (!Utils.isNetUsable(BuyCoinsActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getShopCoinTypes("", SharedPreferencesUtils.getString(this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    private void getBuyCoinsLog() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.buyCoinsLog(Constants.BUY_COIN_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    private void getShopPayLog() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopPayLog(Constants.SHOP_PAY_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_buy_coins;
    }

    private void initPingPlusPlus() {
        PingppLog.DEBUG = false;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.buy_coin})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.buy_coin:
                if (!requiredVerify(coinCountsET)) {
                    coinCountsET.setError(getResources().getString(R.string.buy_coin_coin_count_required));
                    return;
                }
                if (null == selectedType) {
                    showSnackbar("请选择金币类型");
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                Double amount = Double.valueOf(coinCountsET.getText().toString());
                // 支付宝 微信支付 银联
                BuyCoinParameter.PayParaBean payPara = new BuyCoinParameter.PayParaBean();
                payPara.setCurrency("cny");
                payPara.setAmount(amount);
                payPara.setSubject("叮叮蚊支付");
                payPara.setBody("充值金币");
                payPara.setClient_IP(NetworkUtils.getIPAddress(true));
                switch (payType) {
                    case 0:
                        payPara.setChannel(CHANNEL_UPACP);
                        break;
                    case 1:
                        payPara.setChannel(CHANNEL_WECHAT);
                        break;
                    case 2:
                        payPara.setChannel(CHANNEL_ALIPAY);
                        break;
                }
                BuyCoinParameter buyCoinParameter = new BuyCoinParameter();
                buyCoinParameter.setSubmitCode("000000");
                buyCoinParameter.setBazaCode(SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""));
                buyCoinParameter.setGoldNum(Double.valueOf(coinCountsET.getText().toString()));
                buyCoinParameter.setGoldType(selectedType.getGoldType());
                buyCoinParameter.setPayPara(payPara);
//                showMoveDialog(this, Integer.valueOf(coinCountsET.getText().toString()));
//                mPresenter.buyCoins(Constants.BUY_COIN_ACTION_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), Integer.valueOf(coinCountsET.getText().toString()), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                mPresenter.buyCoins(Constants.BUY_COIN_ACTION_CODE, gson.toJson(buyCoinParameter), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
//                orderPayPPP();
                break;
        }
    }

    //订单支付(Ping++)
    private void orderPayPPP() {
        Double amount = Double.valueOf(coinCountsET.getText().toString());
        // 支付宝 微信支付 银联
        BuyCoinParameter.PayParaBean payPara = null;
        switch (payType) {
            case 0:
                payPara = new BuyCoinParameter.PayParaBean();
                payPara.setAmount(amount);
                payPara.setChannel(CHANNEL_UPACP);
                payPara.setBody("购买金币");
                break;
            case 1:
                payPara = new BuyCoinParameter.PayParaBean();
                payPara.setAmount(amount);
                payPara.setChannel(CHANNEL_WECHAT);
                payPara.setBody("购买金币");
                break;
            case 2:
                payPara = new BuyCoinParameter.PayParaBean();
                payPara.setAmount(amount);
                payPara.setChannel(CHANNEL_ALIPAY);
                payPara.setBody("购买金币");
                break;
        }
        BuyCoinParameter request = new BuyCoinParameter();
        request.setSubmitCode("000000");
        request.setPayPara(payPara);
        mPresenter.buyCoinsPay(gson.toJson(request), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
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
    protected void onDestroy() {
        mHandler.removeCallbacks(buySuccessRunnable);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                System.out.println(result + "----" + errorMsg + "----" + extraMsg);
                if ("success".equals(result)) {
                    coinCountsET.setText("");
                    showSnackbar("购买金币支付成功");
                } else {
                    if ("invalid_credential".equals(errorMsg)) {
                        showSnackbar("订单已过期");
                    } else if ("user_cancelled".equals(errorMsg)) {
                        showSnackbar("支付已取消");
                    } else if ("wx_app_not_installed".equals(errorMsg)) {
                        showSnackbar("您未安装微信/支付宝");
                    } else {
                        showSnackbar("购买金币支付失败");
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("userType", userType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(BuyCoinsContract.BuyCoinsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onBuyCoins(boolean flag, String msg, Object charge) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
//        showProgressDialog(R.drawable.success, getResources().getString(R.string.loading));
//        coinCountsET.setText("");
//        mHandler.postDelayed(buySuccessRunnable, 500);
        Pingpp.createPayment(BuyCoinsActivity.this, gson.toJson(charge));
    }

    @Override
    public void onBuyCoinsPay(Object charge) {
        Pingpp.createPayment(BuyCoinsActivity.this, gson.toJson(charge));
    }

    @Override
    public void onBuyCoinsLog(List<BuyCoinsLog> logList, Integer lastsequence) {
//        this.lastsequence = lastsequence;
//        pageIndex++;
//        if (logList.size() < PAGE_SIZE) {
//            coinListRV.disableLoadmore();
//        }
//        if (logList.size() <= 0) return;
//        this.logList.clear();
//        this.logList.addAll(logList);
//        buyCoinsLogAdapter.appendList(this.logList);
    }

    @Override
    public void onShopPayLog(List<ShopPayLog> logList, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        if (logList.size() < PAGE_SIZE) {
            coinListRV.disableLoadmore();
        }
        if (logList.size() <= 0) {
            if (2 == pageIndex) {
                showSnackbar("购买明细没有数据,下拉刷新");
            }
            return;
        }
        this.logList.clear();
        this.logList.addAll(logList);
        shopPayLogAdapter.appendList(this.logList);
    }

    @Override
    public void onGetNotPayCharge(Object charge) {
        Pingpp.createPayment(BuyCoinsActivity.this, gson.toJson(charge));
    }

    @Override
    public void onGetCoinTypes(List<CoinType> coinTypes) {
        coinTypeList.clear();
        coinTypesAdapter.clearList();
        if (0 == coinTypes.size()) {
            coinTypeList.add(defaultCoinType);
            coinTypesAdapter.appendList(Collections.singletonList(defaultCoinType));
        } else {
            coinTypeList.addAll(coinTypes);
            coinTypesAdapter.appendList(coinTypes);
        }
    }

    @Override
    public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.wechat_pay_radio:
                payType = 1;
                break;
            case R.id.union_pay_radio:
                payType = 0;
                break;
            case R.id.alipay_pay_radio:
                payType = 2;
                break;
        }
    }
}
