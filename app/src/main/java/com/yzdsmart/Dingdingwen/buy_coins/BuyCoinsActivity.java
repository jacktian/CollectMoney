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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BuyCoinsLog;
import com.yzdsmart.Dingdingwen.bean.PaymentRequest;
import com.yzdsmart.Dingdingwen.http.response.BuyCoinsPayRequestResponse;
import com.yzdsmart.Dingdingwen.publish_tasks.PublishTasksActivity;
import com.yzdsmart.Dingdingwen.share_sdk.OnekeyShare;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.views.CustomNestRadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.sharesdk.framework.ShareSDK;

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

        ShareSDK.initSDK(this, "188d0cc56cba8");
        initPingPlusPlus();

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

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
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
        ShareSDK.stopSDK(this);
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
