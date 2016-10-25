package com.yzdsmart.Collectmoney.buy_coins;

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

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.pingplusplus.android.PingppLog;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.BuyCoinsLog;
import com.yzdsmart.Collectmoney.bean.PaymentRequest;
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

    private static final String BUY_COIN_CODE = "66";
    private static final String BUY_COIN_LOG_CODE = "688";
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
                mPresenter.buyCoinsLog(BUY_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
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
                mPresenter.buyCoinsLog(BUY_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
            }
        });

        initPingPlusPlus();

        mPresenter.buyCoinsLog(BUY_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
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
                mPresenter.buyCoins(BUY_COIN_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), Integer.valueOf(coinCountsET.getText().toString()));
//                orderPayPPP();
                break;
        }
    }

    //订单支付(Ping++)
    private void orderPayPPP() {
        Integer amount = Integer.valueOf(coinCountsET.getText().toString());
        // 支付宝 微信支付 银联
        PaymentRequest paymentRequest;
        switch (payType) {
            case 0:
                paymentRequest = new PaymentRequest(CHANNEL_UPACP, amount);
                break;
            case 1:
                paymentRequest = new PaymentRequest(CHANNEL_WECHAT, amount);
                break;
            case 2:
                paymentRequest = new PaymentRequest(CHANNEL_ALIPAY, amount);
                break;
        }
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
