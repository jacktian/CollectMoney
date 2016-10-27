package com.yzdsmart.Collectmoney.buy_coins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.Constants;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.BuyCoinsLog;
import com.yzdsmart.Collectmoney.bean.PaymentRequest;
import com.yzdsmart.Collectmoney.http.response.BuyCoinsPayRequestResponse;
import com.yzdsmart.Collectmoney.publish_tasks.PublishTasksActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.views.CustomNestRadioGroup;

import java.util.ArrayList;
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
    @BindView(R.id.coin_counts)
    EditText coinCountsET;
    @Nullable
    @BindView(R.id.coin_list)
    UltimateRecyclerView coinListRV;

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
    private Integer lastsequence;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<BuyCoinsLog> logList;
    private BuyCoinsLogAdapter buyCoinsLogAdapter;

    private BuyCoinsContract.BuyCoinsPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable buySuccessRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logList = new ArrayList<BuyCoinsLog>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("购买金币");

        new BuyCoinsPresenter(this, this);

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

        buyCoinsLogAdapter = new BuyCoinsLogAdapter(this);
        coinListRV.setHasFixedSize(true);
        coinListRV.setLayoutManager(mLinearLayoutManager);
        coinListRV.addItemDecoration(dividerItemDecoration);
        coinListRV.setAdapter(buyCoinsLogAdapter);
        coinListRV.reenableLoadmore();
        coinListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                mPresenter.buyCoinsLog(Constants.BUY_COIN_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
            }
        });
        coinListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                coinListRV.setRefreshing(false);
                coinListRV.reenableLoadmore();
                pageIndex = 1;
                lastsequence = 0;
                buyCoinsLogAdapter.clearList();
                mPresenter.buyCoinsLog(Constants.BUY_COIN_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
            }
        });

        initPingPlusPlus();

        mPresenter.buyCoinsLog(Constants.BUY_COIN_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_buy_coins;
    }

    private void initPingPlusPlus() {
        PingppLog.DEBUG = true;
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
//                showMoveDialog(this, Integer.valueOf(coinCountsET.getText().toString()));
//                mPresenter.buyCoins(Constants.BUY_COIN_ACTION_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), Integer.valueOf(coinCountsET.getText().toString()));
                orderPayPPP();
                break;
        }
    }

    //订单支付(Ping++)
    private void orderPayPPP() {
        Double amount = Double.valueOf(coinCountsET.getText().toString());
        // 支付宝 微信支付 银联
        PaymentRequest.PayParaBean payPara = null;
        switch (payType) {
            case 0:
                payPara = new PaymentRequest.PayParaBean();
                payPara.setAmount(amount);
                payPara.setChannel(CHANNEL_UPACP);
                payPara.setBody("购买金币");
                break;
            case 1:
                payPara = new PaymentRequest.PayParaBean();
                payPara.setAmount(amount);
                payPara.setChannel(CHANNEL_WECHAT);
                payPara.setBody("购买金币");
                break;
            case 2:
                payPara = new PaymentRequest.PayParaBean();
                payPara.setAmount(amount);
                payPara.setChannel(CHANNEL_ALIPAY);
                payPara.setBody("购买金币");
                break;
        }
        PaymentRequest request = new PaymentRequest();
        request.setSubmitCode("000000");
        request.setPayPara(payPara);
        Gson gson = new Gson();
        mPresenter.buyCoinsPay(gson.toJson(request));
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
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showSnackbar(result + "----" + errorMsg + "----" + extraMsg);
            }
        }
    }

    @Override
    public void setPresenter(BuyCoinsContract.BuyCoinsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onBuyCoins(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        showProgressDialog(R.drawable.success, getResources().getString(R.string.loading));
        coinCountsET.setText("");
        mHandler.postDelayed(buySuccessRunnable, 500);
    }

    @Override
    public void onBuyCoinsPay(BuyCoinsPayRequestResponse.ChargeBean charge) {
        Gson gson = new Gson();
        Pingpp.createPayment(BuyCoinsActivity.this, gson.toJson(charge));
    }

    @Override
    public void onBuyCoinsLog(List<BuyCoinsLog> logList, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        if (logList.size() < PAGE_SIZE) {
            coinListRV.disableLoadmore();
        }
        if (logList.size() <= 0) return;
        this.logList.clear();
        this.logList.addAll(logList);
        buyCoinsLogAdapter.appendList(this.logList);
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
        }
    }
}
