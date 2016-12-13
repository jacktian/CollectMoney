package com.yzdsmart.Dingdingwen.card_bag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/12/13.
 */

public class CardBagActivity extends BaseActivity implements CardBagContract.CardBagView {
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
    @BindView(R.id.card_bag_items)
    RecyclerView cardbagItemsRV;

    private static final String TAG = "CardBagActivity";

    private CardBagContract.CardBagPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private BankCard addBankCard = null;
    private CardBagAdapter cardBagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("卡包");

        addBankCard = new BankCard();
        addBankCard.setBankCode("ADD_BANK_CARD");

        new CardBagPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        cardBagAdapter = new CardBagAdapter(this);
        cardBagAdapter.appendList(Collections.singletonList(addBankCard));
        cardbagItemsRV.setHasFixedSize(true);
        cardbagItemsRV.setLayoutManager(mLinearLayoutManager);
        cardbagItemsRV.setAdapter(cardBagAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_card_bag;
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getBankCardList("", SharedPreferencesUtils.getString(CardBagActivity.this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));

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
    public void setPresenter(CardBagContract.CardBagPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetBankCardList(List<BankCard> bankCards) {
        showSnackbar("共有" + bankCards.size() + "张银行卡");
    }
}
