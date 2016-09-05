package com.yzdsmart.Collectmoney.buy_coins;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/4.
 */
public class BuyCoinsActivity extends BaseActivity implements BuyCoinsContract.BuyCoinsView {
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

    private static final String BUY_COIN_CODE = "66";

    private BuyCoinsContract.BuyCoinsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("购买金币");

        new BuyCoinsPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_buy_coins;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.buy_coin})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.buy_coin:
                mPresenter.buyCoins(BUY_COIN_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), Integer.valueOf(coinCountsET.getText().toString()));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void setPresenter(BuyCoinsContract.BuyCoinsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onBuyCoins(boolean flag, String msg) {
        showSnackbar(msg);
        if (!flag) {
            return;
        }
        try {
            Thread.sleep(1000);
            closeActivity();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
