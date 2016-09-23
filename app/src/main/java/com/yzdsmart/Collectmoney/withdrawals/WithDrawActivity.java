package com.yzdsmart.Collectmoney.withdrawals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    private static final String GET_LEFT_COINS_ACTION_CODE = "1288";

    private Integer userType;//0 个人 1 商家

    private WithDrawContract.WithDrawPresenter mPresenter;

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
                break;
        }

        new WithDrawPresenter(this, this);

        switch (userType) {
            case 0:
                mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
                break;
            case 1:
                mPresenter.getLeftCoins(GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
                break;
        }
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

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
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
    public void setPresenter(WithDrawContract.WithDrawPresenter presenter) {
        mPresenter = presenter;
    }
}
