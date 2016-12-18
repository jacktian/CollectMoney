package com.yzdsmart.Dingdingwen.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.PaymentParameter;
import com.yzdsmart.Dingdingwen.bean.ShopDiscount;
import com.yzdsmart.Dingdingwen.utils.NetworkUtils;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.CustomNestRadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;

/**
 * Created by YZD on 2016/12/18.
 */

public class PaymentActivity extends BaseActivity implements PaymentContract.PaymentView, CustomNestRadioGroup.OnCheckedChangeListener {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.right_title)
    TextView rightTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.pay_amount)
    EditText payAmountET;
    @Nullable
    @BindView(R.id.shop_discount_title)
    TextView shopDiscountTitleTV;
    @Nullable
    @BindView(R.id.pay_type_group)
    CustomNestRadioGroup payTypeGroup;
    @Nullable
    @BindView(R.id.discount_types)
    Spinner discountTypesBS;
    @Nullable
    @BindView(R.id.left_coin_counts)
    TextView leftCoinCountsTV;
    @Nullable
    @BindView(R.id.coin_counts)
    EditText coinCountsET;
    @Nullable
    @BindView(R.id.actual_amount)
    TextView actualAmountTV;
    @Nullable
    @BindView(R.id.confirm_payment)
    Button confirmPayBtn;

    private final static String TAG = "PaymentActivity";

    private PaymentContract.PaymentPresenter mPresenter;

    private String bazaCode = "";

    private Gson gson = new Gson();

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

    private ShopDiscountAdapter shopDiscountAdapter;
    private List<ShopDiscount> shopDiscountList;
    private ShopDiscount shopDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bazaCode = getIntent().getExtras().getString("bazaCode");

        shopDiscountList = new ArrayList<ShopDiscount>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText(getResources().getString(R.string.payment_title));
        ButterKnife.apply(rightTitleTV, BUTTERKNIFEVISIBLE);
        rightTitleTV.setText("兑换");

        new PaymentPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        payTypeGroup.setOnCheckedChangeListener(this);
        shopDiscountAdapter = new ShopDiscountAdapter(this);
        discountTypesBS.setAdapter(shopDiscountAdapter);
        discountTypesBS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shopDiscount = shopDiscountList.get(i);
                if (payAmountET.getText().toString().trim().length() > 0) {
                    float discountPrice = 0f;
                    switch (shopDiscount.getDisType()) {
                        case 23:
                            discountPrice = Float.valueOf(payAmountET.getText().toString().trim()) * (1 - shopDiscount.getDiscReta());
                            break;
                        case 45:
                            discountPrice = (Float.valueOf(payAmountET.getText().toString().trim()) / shopDiscount.getFullPrice()) > 1.0 ? shopDiscount.getDiscPrice() : 0f;
                            break;
                    }
                    if (coinCountsET.getText().toString().trim().length() > 0) {
                        if ((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice - Float.valueOf(coinCountsET.getText().toString().trim())) < 0) {
                            coinCountsET.setText((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice) + "");
                            actualAmountTV.setText("0");
                        } else {
                            actualAmountTV.setText((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice - Float.valueOf(coinCountsET.getText().toString().trim())) + "");
                        }
                    } else {
                        actualAmountTV.setText((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice) + "");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopDiscounts("", bazaCode, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        mPresenter.getCustInfo("", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_payment;
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
    @OnClick({R.id.title_left_operation_layout, R.id.confirm_payment})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.confirm_payment:
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                PaymentParameter.PayParaBean payInfoBean = new PaymentParameter.PayParaBean();
                payInfoBean.setAmount(Float.valueOf(actualAmountTV.getText().toString().trim()));
                payInfoBean.setCurrency("cny");
                payInfoBean.setSubject("叮叮蚊消费支付");
                payInfoBean.setBody("消费支付");
                payInfoBean.setClient_IP(NetworkUtils.getIPAddress(true));
                switch (payType) {
                    case 0:
                        payInfoBean.setChannel(CHANNEL_UPACP);
                        break;
                    case 1:
                        payInfoBean.setChannel(CHANNEL_WECHAT);
                        break;
                    case 2:
                        payInfoBean.setChannel(CHANNEL_ALIPAY);
                        break;
                }
                PaymentParameter paymentParameter = new PaymentParameter();
                paymentParameter.setSubmitCode("0000000");
                paymentParameter.setBazaCode(bazaCode);
                paymentParameter.setCustCode(SharedPreferencesUtils.getString(PaymentActivity.this, "cust_code", ""));
                paymentParameter.setUseGold(coinCountsET.getText().toString().trim().length() > 0 ? Float.valueOf(coinCountsET.getText().toString().trim()) : 0f);
                paymentParameter.setDiscount((Float.valueOf(payAmountET.getText().toString().trim()) - Float.valueOf(coinCountsET.getText().toString().trim().length() > 0 ? Float.valueOf(coinCountsET.getText().toString().trim()) : 0) - Float.valueOf(actualAmountTV.getText().toString().trim())));
                paymentParameter.setTotal(Float.valueOf(payAmountET.getText().toString().trim()));
                paymentParameter.setPayPara(payInfoBean);
                mPresenter.submitPayment(Constants.PERSONAL_PAYMENT_ACTION_CODE, gson.toJson(paymentParameter), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    @Optional
    @OnTextChanged(R.id.pay_amount)
    void onPayAmountChanged(CharSequence s, int i, int j, int k) {
        if (s.toString().length() > 0) {
            confirmPayBtn.setEnabled(true);
            if (null == shopDiscount) {
                if (coinCountsET.getText().toString().trim().length() > 0) {
                    actualAmountTV.setText((Float.valueOf(s.toString().trim()) - Float.valueOf(coinCountsET.getText().toString().trim())) + "");
                } else {
                    actualAmountTV.setText(s.toString());
                }
            } else {
                float discountPrice = 0f;
                switch (shopDiscount.getDisType()) {
                    case 23:
                        discountPrice = Float.valueOf(s.toString().trim()) * (1 - shopDiscount.getDiscReta());
                        break;
                    case 45:
                        discountPrice = (Float.valueOf(s.toString().trim()) / shopDiscount.getFullPrice()) > 1.0 ? shopDiscount.getDiscPrice() : 0f;
                        break;
                }
                if (coinCountsET.getText().toString().trim().length() > 0) {
                    if ((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice - Float.valueOf(coinCountsET.getText().toString().trim())) < 0) {
                        coinCountsET.setText((Float.valueOf(s.toString().trim()) - discountPrice) + "");
                        actualAmountTV.setText("0");
                    } else {
                        actualAmountTV.setText((Float.valueOf(s.toString().trim()) - discountPrice - Float.valueOf(coinCountsET.getText().toString().trim())) + "");
                    }
                } else {
                    actualAmountTV.setText((Float.valueOf(s.toString().trim()) - discountPrice) + "");
                }
            }
        } else {
            confirmPayBtn.setEnabled(false);
        }
    }

    @Optional
    @OnTextChanged(R.id.coin_counts)
    void onCoinCountChanged(CharSequence s, int i, int j, int k) {
        if (payAmountET.getText().toString().trim().length() > 0) {
            if (s.toString().trim().length() > 0) {
                if (null == shopDiscount) {
                    actualAmountTV.setText((Float.valueOf(payAmountET.getText().toString().trim()) - Float.valueOf(s.toString().trim())) + "");
                } else {
                    float discountPrice = 0f;
                    switch (shopDiscount.getDisType()) {
                        case 23:
                            discountPrice = Float.valueOf(payAmountET.getText().toString().trim()) * (1 - shopDiscount.getDiscReta());
                            break;
                        case 45:
                            discountPrice = (Float.valueOf(payAmountET.getText().toString().trim()) / shopDiscount.getFullPrice()) > 1.0 ? shopDiscount.getDiscPrice() : 0f;
                            break;
                    }
                    if ((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice - Float.valueOf(s.toString().trim())) < 0) {
                        coinCountsET.setText((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice) + "");
                        actualAmountTV.setText("0");
                    } else {
                        actualAmountTV.setText((Float.valueOf(payAmountET.getText().toString().trim()) - discountPrice - Float.valueOf(s.toString().trim())) + "");
                    }
                }
            } else {
                if (null == shopDiscount) {
                    actualAmountTV.setText(payAmountET.getText().toString());
                } else {
                    float discountPrice = 0f;
                    switch (shopDiscount.getDisType()) {
                        case 23:
                            discountPrice = Float.valueOf(payAmountET.getText().toString().trim()) * (1 - shopDiscount.getDiscReta());
                            break;
                        case 45:
                            discountPrice = (Float.valueOf(payAmountET.getText().toString().trim()) / shopDiscount.getFullPrice()) > 1.0 ? shopDiscount.getDiscPrice() : 0f;
                            break;
                    }
                    actualAmountTV.setText((Float.valueOf(payAmountET.getText().toString()) - discountPrice) + "");
                }
            }
        }
    }

    @Override
    public void setPresenter(PaymentContract.PaymentPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetCustInfo(Float goldNum) {
        leftCoinCountsTV.setText("" + goldNum);
    }

    @Override
    public void onGetShopDiscounts(List<ShopDiscount> shopDiscounts) {
        shopDiscountList.clear();
        shopDiscountList.addAll(shopDiscounts);
        shopDiscountAdapter.clearList();
        shopDiscountAdapter.appendList(shopDiscounts);
        if (shopDiscounts.size() == 0) {
            ButterKnife.apply(shopDiscountTitleTV, BUTTERKNIFEGONE);
            ButterKnife.apply(discountTypesBS, BUTTERKNIFEGONE);
        } else {
            ButterKnife.apply(shopDiscountTitleTV, BUTTERKNIFEVISIBLE);
            ButterKnife.apply(discountTypesBS, BUTTERKNIFEVISIBLE);
        }
    }

    @Override
    public void onSubmitPayment(boolean flag, String msg, Object charge) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        showSnackbar("" + charge);
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
