package com.yzdsmart.Dingdingwen.withdrawals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.bean.PersonalWithdrawParameter;
import com.yzdsmart.Dingdingwen.bean.ShopWithdrawParameter;
import com.yzdsmart.Dingdingwen.card_bag.CardBagActivity;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.utils.AmountInputFilter;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by jacks on 2016/9/23.
 */

public class WithDrawActivity extends BaseActivity implements WithDrawContract.WithDrawView {
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
    TextView coinCountsTV;
    @Nullable
    @BindView(R.id.withdraw_gold_num)
    EditText withdrawGoldNumET;
    @Nullable
    @BindView(R.id.withdraw_rmb)
    TextView withdrawRMBTV;
    @Nullable
    @BindView(R.id.gold_rmb_ratio)
    TextView goldRMBRatioTV;
    @Nullable
    @BindView(R.id.withdraw_money)
    Button withdrawMoneyBtn;
    @Nullable
    @BindView(R.id.bank_logo)
    ImageView bankLogoIV;
    @Nullable
    @BindView(R.id.card_num)
    TextView cardNumTV;

    private static final String TAG = "WithDrawActivity";

    private DecimalFormat decimalFormat;

    private InputFilter[] amountFilters;

    private Gson gson = new Gson();

    private Double GOLD_FORMAT_RMB_RATIO = 0.0d;

    private Integer userType;//0 个人 1 商家

    private WithDrawContract.WithDrawPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable shopWithdrawSuccessRunnable;
    private Runnable personalWithdrawSuccessRunnable;

    private BankCard selectedBankCard;
    private CoinTypesAdapter coinTypesAdapter;
    private CoinType defaultCoinType;
    private CoinType selectedType;
    private List<CoinType> coinTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        coinTypeList = new ArrayList<CoinType>();

        amountFilters = new InputFilter[]{new AmountInputFilter()};

        if (null != savedInstanceState) {
            userType = savedInstanceState.getInt("userType");
        } else {
            userType = getIntent().getExtras().getInt("userType");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        switch (userType) {
            case 0:
                centerTitleTV.setText("个人提现");
                break;
            case 1:
                centerTitleTV.setText("商家提现");
                GOLD_FORMAT_RMB_RATIO = 0.994d;
                break;
        }

        withdrawGoldNumET.setFilters(amountFilters);
        goldRMBRatioTV.setText("1金币=" + GOLD_FORMAT_RMB_RATIO + "元");

        new WithDrawPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        ShareSDK.initSDK(this);

        defaultCoinType = new CoinType(0, 0d, "普通金币", "");
        coinTypesAdapter = new CoinTypesAdapter(this);
        coinTypesBS.setAdapter(coinTypesAdapter);
        coinTypesBS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = coinTypeList.get(i);
                switch (userType) {
                    case 0:
                        getPersonalLeftCoins();
                        break;
                    case 1:
                        getShopLeftCoins();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        shopWithdrawSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                getShopLeftCoins();
            }
        };
        personalWithdrawSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                getPersonalLeftCoins();
            }
        };

        if (!Utils.isNetUsable(WithDrawActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        switch (userType) {
            case 0:
                mPresenter.getPersonalCoinTypes("", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case 1:
                mPresenter.getShopCoinTypes("", SharedPreferencesUtils.getString(this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
        mPresenter.getBankCardList("", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    private void getShopLeftCoins() {
        if (!Utils.isNetUsable(WithDrawActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(WithDrawActivity.this, "baza_code", ""), selectedType.getGoldType(), SharedPreferencesUtils.getString(WithDrawActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(WithDrawActivity.this, "ddw_access_token", ""));
    }

    private void getPersonalLeftCoins() {
        if (!Utils.isNetUsable(WithDrawActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getPersonalLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, Constants.PERSONAL_WITHDRAW_ACTION_TYPE_CODE, "", SharedPreferencesUtils.getString(this, "cust_code", ""), selectedType.getGoldType(), SharedPreferencesUtils.getString(WithDrawActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(WithDrawActivity.this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_withdraw;
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
        mHandler.removeCallbacks(shopWithdrawSuccessRunnable);
        mHandler.removeCallbacks(personalWithdrawSuccessRunnable);
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_BANK_CARD_NUM_CODE == requestCode && RESULT_OK == resultCode) {
            Bundle bundle = data.getExtras();
            selectedBankCard = bundle.getParcelable("bankCard");
            Glide.with(this).load("https://apimg.alipay.com/combo.png?d=cashier&t=" + selectedBankCard.getBankCode()).asBitmap().override(Math.round(Utils.getScreenRatio(this) * 151), Math.round(Utils.getScreenRatio(this) * 43)).into(bankLogoIV);
            cardNumTV.setText(Utils.cardIdHide(selectedBankCard.getBankCardNum()));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            bankLogoIV.setLayoutParams(params);
            cardNumTV.setLayoutParams(params);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("userType", userType);
        super.onSaveInstanceState(outState);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.bank_logo_layout, R.id.withdraw_money, R.id.with_all})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.bank_logo_layout:
                openActivity(CardBagActivity.class, null, Constants.REQUEST_BANK_CARD_NUM_CODE);
                break;
            case R.id.withdraw_money:
                if (!requiredVerify(withdrawGoldNumET)) {
                    withdrawGoldNumET.setError(getResources().getString(R.string.input_withdraw_gold_num));
                    return;
                }
                if (null == selectedBankCard) {
                    showSnackbar("请选择银行卡");
                    return;
                }
                if (null == selectedType) {
                    showSnackbar("请选择金币类型");
                    return;
                }
                if (!Utils.isNetUsable(WithDrawActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                switch (userType) {
                    case 0:
                        PersonalWithdrawParameter personalWithdrawParameter = new PersonalWithdrawParameter();
                        personalWithdrawParameter.setSubmitCode("");
                        personalWithdrawParameter.setCustCode(SharedPreferencesUtils.getString(this, "cust_code", ""));
                        personalWithdrawParameter.setGoldNum(Double.valueOf(withdrawGoldNumET.getText().toString()));
                        personalWithdrawParameter.setGoldType(selectedType.getGoldType());
                        PersonalWithdrawParameter.PayInfoBean personalPayInfoBean = new PersonalWithdrawParameter.PayInfoBean();
                        personalPayInfoBean.setBankCode(selectedBankCard.getBankCode());
                        personalPayInfoBean.setBankCardNum(selectedBankCard.getBankCardNum());
                        personalPayInfoBean.setCustName(selectedBankCard.getCustName());
                        personalWithdrawParameter.setPayInfo(personalPayInfoBean);
                        mPresenter.personalWithdrawCoins(Constants.PERSONAL_WITHDRAW_ACTION_CODE, Constants.PERSONAL_WITHDRAW_ACTION_TYPE_CODE, gson.toJson(personalWithdrawParameter), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                        break;
                    case 1:
                        ShopWithdrawParameter shopWithdrawParameter = new ShopWithdrawParameter();
                        shopWithdrawParameter.setSubmitCode("");
                        shopWithdrawParameter.setBazaCode(SharedPreferencesUtils.getString(this, "baza_code", ""));
                        shopWithdrawParameter.setGoldNum(Double.valueOf(withdrawGoldNumET.getText().toString()));
                        shopWithdrawParameter.setGoldType(selectedType.getGoldType());
                        ShopWithdrawParameter.PayInfoBean shopPayInfoBean = new ShopWithdrawParameter.PayInfoBean();
                        shopPayInfoBean.setBankCode(selectedBankCard.getBankCode());
                        shopPayInfoBean.setBankCardNum(selectedBankCard.getBankCardNum());
                        shopPayInfoBean.setCustName(selectedBankCard.getCustName());
                        shopWithdrawParameter.setPayInfo(shopPayInfoBean);
                        mPresenter.shopWithdrawCoins(Constants.SHOP_WITHDRAW_ACTION_CODE, gson.toJson(shopWithdrawParameter), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                        break;
                }
                break;
            case R.id.with_all:
                if (Double.valueOf(coinCountsTV.getText().toString()) > 0) {
                    withdrawGoldNumET.setText(coinCountsTV.getText().toString());
                    withdrawMoneyBtn.setEnabled(true);
                }
                break;
        }
    }

    @Optional
    @OnTextChanged({R.id.withdraw_gold_num})
    void onTextChanged() {
        if (withdrawGoldNumET.getText().toString().length() > 0) {
            switch (userType) {
                case 0:
                    if (Double.valueOf(withdrawGoldNumET.getText().toString()) > 10) {
                        GOLD_FORMAT_RMB_RATIO = 0.994d;
                    } else {
                        GOLD_FORMAT_RMB_RATIO = 0.9d;
                    }
                    break;
            }
            goldRMBRatioTV.setText("1金币=" + GOLD_FORMAT_RMB_RATIO + "元");
            withdrawMoneyBtn.setEnabled(true);
            withdrawRMBTV.setText("￥" + decimalFormat.format((Double.valueOf(withdrawGoldNumET.getText().toString()) * GOLD_FORMAT_RMB_RATIO)));
        } else {
            withdrawRMBTV.setText("￥0.0");
            withdrawMoneyBtn.setEnabled(false);
        }
    }

    @Override
    public void onGetCustInfo(CustInfoRequestResponse response) {
        coinCountsTV.setText(decimalFormat.format(response.getGoldNum()));
    }

    @Override
    public void onGetPersonalLeftCoins(Double counts) {
        coinCountsTV.setText(decimalFormat.format(counts));
    }

    @Override
    public void onGetShopLeftCoins(Double counts) {
        coinCountsTV.setText(decimalFormat.format(counts));
    }

    @Override
    public void onShopWithdrawCoins(String withdrawRMB) {
        withdrawGoldNumET.setText("");
        withdrawRMBTV.setText("");
        showProgressDialog(R.drawable.success, "您已成功提现" + withdrawRMB + "元");
        mHandler.postDelayed(shopWithdrawSuccessRunnable, 500);
        showShare();
    }

    @Override
    public void onPersonalWithdrawCoins(String withdrawRMB) {
        withdrawGoldNumET.setText("");
        withdrawRMBTV.setText("");
        showProgressDialog(R.drawable.success, "您已成功提现" + withdrawRMB + "元");
        mHandler.postDelayed(personalWithdrawSuccessRunnable, 500);
        showShare();
    }

    @Override
    public void onGetBankCardList(List<BankCard> bankCards) {
        if (0 == bankCards.size()) {
            showSnackbar("您当前还没有绑定银行卡");
            return;
        }
        selectedBankCard = bankCards.get(0);
        Glide.with(this).load("https://apimg.alipay.com/combo.png?d=cashier&t=" + selectedBankCard.getBankCode()).asBitmap().override(Math.round(Utils.getScreenRatio(this) * 151), Math.round(Utils.getScreenRatio(this) * 43)).into(bankLogoIV);
        cardNumTV.setText(Utils.cardIdHide(selectedBankCard.getBankCardNum()));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        bankLogoIV.setLayoutParams(params);
        cardNumTV.setLayoutParams(params);
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
    public void setPresenter(WithDrawContract.WithDrawPresenter presenter) {
        mPresenter = presenter;
    }
}
