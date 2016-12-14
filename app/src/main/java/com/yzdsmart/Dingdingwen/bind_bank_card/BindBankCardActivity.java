package com.yzdsmart.Dingdingwen.bind_bank_card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;

/**
 * Created by YZD on 2016/12/14.
 */

public class BindBankCardActivity extends BaseActivity implements BindBankCardContract.BindBankCardView {
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
    @BindView(R.id.cust_name)
    EditText custNameET;
    @Nullable
    @BindView(R.id.bank_card_num)
    EditText cardNumET;
    @Nullable
    @BindView(R.id.bank_logo_layout)
    FrameLayout bankLogoLayout;
    @Nullable
    @BindView(R.id.bank_card_logo)
    ImageView bankLogoIV;
    @Nullable
    @BindView(R.id.bind_card)
    Button bindCardBtn;

    private BindBankCardContract.BindBankCardPresenter mPresenter;

    private String bankCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("添加银行卡");

        new BindBankCardPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_bind_bank_card;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.bind_card})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.bind_card:
                if (!Utils.isNetUsable(BindBankCardActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.bindBankCard("", SharedPreferencesUtils.getString(this, "cust_code", ""), bankCode, cardNumET.getText().toString(), custNameET.getText().toString(), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    @Optional
    @OnTextChanged(R.id.cust_name)
    void onCustNameChanged(CharSequence s, int start, int count, int after) {
        if (bankCode.length() > 0 && s.length() > 0) {
            bindCardBtn.setEnabled(true);
        } else {
            bindCardBtn.setEnabled(false);
        }
    }

    @Optional
    @OnTextChanged(R.id.bank_card_num)
    void onCardNumChanged(CharSequence s, int start, int count, int after) {
        if (s.length() == 19) {
            if (!Utils.isNetUsable(BindBankCardActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.validateBankCard("", s.toString(), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        } else {
            bankCode = "";
            bindCardBtn.setEnabled(false);
            ButterKnife.apply(bankLogoLayout, BUTTERKNIFEGONE);
            cardNumET.setError("请输入19位账号储蓄卡/借记卡");
        }
    }

    @Override
    public void setPresenter(BindBankCardContract.BindBankCardPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onValidateBankCard(String bankCode) {
        this.bankCode = bankCode;
        ButterKnife.apply(bankLogoLayout, BUTTERKNIFEVISIBLE);
        Glide.with(this).load("https://apimg.alipay.com/combo.png?d=cashier&t=" + bankCode).asBitmap().override(Math.round(Utils.getScreenRatio(this) * 151), Math.round(Utils.getScreenRatio(this) * 43)).into(bankLogoIV);
        if (custNameET.getText().toString().trim().length() > 0) {
            bindCardBtn.setEnabled(true);
        }
    }

    @Override
    public void onBindBankCard() {
        setResult(RESULT_OK);
        closeActivity();
    }
}
