package com.yzdsmart.Collectmoney.withdrawals;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;

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

    private static final String GET_LEFT_COINS_ACTION_CODE = "1288";
    private static final String SHOP_WITHDRAW_ACTION_CODE = "688";
    private static final String PERSONAL_WITHDRAW_ACTION_CODE = "588";
    private static final String PERSONAL_WITHDRAW_ACTION_TYPE_CODE = "166";

    private Float GOLD_FORMAT_RMB_RATIO = 0.0f;

    private Integer userType;//0 个人 1 商家

    private WithDrawContract.WithDrawPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable shopWithdrawSuccessRunnable;
    private Runnable personalWithdrawSuccessRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        userType = bundle.getInt("userType");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        switch (userType) {
            case 0:
                centerTitleTV.setText("个人提现");
                break;
            case 1:
                centerTitleTV.setText("商家提现");
                GOLD_FORMAT_RMB_RATIO = 0.94f;
                break;
        }
        goldRMBRatioTV.setText("1金币=" + GOLD_FORMAT_RMB_RATIO + "元");

        new WithDrawPresenter(this, this);

        switch (userType) {
            case 0:
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
                break;
            case 1:
                mPresenter.getLeftCoins(GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
                break;
        }

        shopWithdrawSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getLeftCoins(GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(WithDrawActivity.this, "baza_code", ""));
            }
        };
        personalWithdrawSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(WithDrawActivity.this, "cust_code", ""));
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_withdraw;
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
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.withdraw_money, R.id.with_all})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.withdraw_money:
                if (!requiredVerify(withdrawGoldNumET)) {
                    withdrawGoldNumET.setError(getResources().getString(R.string.input_withdraw_gold_num));
                    return;
                }
                switch (userType) {
                    case 0:
                        mPresenter.personalWithdrawCoins(PERSONAL_WITHDRAW_ACTION_CODE, PERSONAL_WITHDRAW_ACTION_TYPE_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), Integer.valueOf(withdrawGoldNumET.getText().toString()));
                        break;
                    case 1:
                        mPresenter.shopWithdrawCoins(SHOP_WITHDRAW_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), Integer.valueOf(withdrawGoldNumET.getText().toString()));
                        break;
                }
                break;
            case R.id.with_all:
                if (Float.valueOf(coinCountsTV.getText().toString()) > 0) {
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
                    if (Float.valueOf(withdrawGoldNumET.getText().toString()) > 100) {
                        GOLD_FORMAT_RMB_RATIO = 0.9f;
                    } else {
                        GOLD_FORMAT_RMB_RATIO = 0.8f;
                    }
                    break;
            }
            goldRMBRatioTV.setText("1金币=" + GOLD_FORMAT_RMB_RATIO + "元");
            withdrawMoneyBtn.setEnabled(true);
            DecimalFormat df = new DecimalFormat("#.00");
            withdrawRMBTV.setText("￥" + df.format((Float.valueOf(withdrawGoldNumET.getText().toString()) * GOLD_FORMAT_RMB_RATIO)));
        } else {
            withdrawRMBTV.setText("￥0.0");
            withdrawMoneyBtn.setEnabled(false);
        }
    }

    @Override
    public void onGetCustInfo(CustInfoRequestResponse response) {
        coinCountsTV.setText("" + response.getGoldNum());
    }

    @Override
    public void onGetLeftCoins(Integer counts) {
        coinCountsTV.setText("" + counts);
    }

    @Override
    public void onShopWithdrawCoins(String withdrawRMB) {
        withdrawGoldNumET.setText("");
        withdrawRMBTV.setText("");
        showProgressDialog(R.drawable.success, "您已成功提现" + withdrawRMB + "元");
        mHandler.postDelayed(shopWithdrawSuccessRunnable, 500);
    }

    @Override
    public void onPersonalWithdrawCoins(String withdrawRMB) {
        withdrawGoldNumET.setText("");
        withdrawRMBTV.setText("");
        showProgressDialog(R.drawable.success, "您已成功提现" + withdrawRMB + "元");
        mHandler.postDelayed(personalWithdrawSuccessRunnable, 500);
    }

    @Override
    public void setPresenter(WithDrawContract.WithDrawPresenter presenter) {
        mPresenter = presenter;
    }
}
