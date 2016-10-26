package com.yzdsmart.Collectmoney.withdrawals_log;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.Constants;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.PersonalWithdrawLog;
import com.yzdsmart.Collectmoney.bean.ShopWithdrawLog;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by jacks on 2016/9/24.
 */

public class WithDrawLogActivity extends BaseActivity implements WithDrawLogContract.WithDrawLogView {
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
    @BindView(R.id.withdraw_log_list)
    UltimateRecyclerView withdrawLogTV;

    private Integer userType;//0 个人 1 商家

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;
    private Integer lastsequence = 0;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<PersonalWithdrawLog> personalWithdrawLogList;
    private List<ShopWithdrawLog> shopWithdrawLogList;
    private WithDrawLogAdapter withDrawLogAdapter;

    private WithDrawLogContract.WithDrawLogPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        personalWithdrawLogList = new ArrayList<PersonalWithdrawLog>();
        shopWithdrawLogList = new ArrayList<ShopWithdrawLog>();

        Bundle bundle = getIntent().getExtras();
        userType = bundle.getInt("userType");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("提现日志");

        new WithDrawLogPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.divider_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        withDrawLogAdapter = new WithDrawLogAdapter(userType, this);
        withdrawLogTV.setHasFixedSize(true);
        withdrawLogTV.setLayoutManager(mLinearLayoutManager);
        withdrawLogTV.addItemDecoration(dividerItemDecoration);
        withdrawLogTV.setAdapter(withDrawLogAdapter);
        withdrawLogTV.reenableLoadmore();
        withdrawLogTV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                switch (userType) {
                    case 0:
                        mPresenter.getPersonalWithdrawLog(Constants.PERSONAL_WITHDRAW_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(WithDrawLogActivity.this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence);
                        break;
                    case 1:
                        mPresenter.getShopWithdrawLog(Constants.SHOP_WITHDRAW_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(WithDrawLogActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
                        break;
                }
            }
        });
        withdrawLogTV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                withdrawLogTV.setRefreshing(false);
                withdrawLogTV.reenableLoadmore();
                lastsequence = 0;
                pageIndex = 1;
                switch (userType) {
                    case 0:
                        withDrawLogAdapter.clearPersonalList();
                        mPresenter.getPersonalWithdrawLog(Constants.PERSONAL_WITHDRAW_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(WithDrawLogActivity.this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence);
                        break;
                    case 1:
                        withDrawLogAdapter.clearShopList();
                        mPresenter.getShopWithdrawLog(Constants.SHOP_WITHDRAW_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(WithDrawLogActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
                        break;
                }
            }
        });

        switch (userType) {
            case 0:
                mPresenter.getPersonalWithdrawLog(Constants.PERSONAL_WITHDRAW_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE, lastsequence);
                break;
            case 1:
                mPresenter.getShopWithdrawLog(Constants.SHOP_WITHDRAW_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_withdraw_log;
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
    public void onGetPersonalWithdrawLog(List<PersonalWithdrawLog> personalWithdrawLogs, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        if (personalWithdrawLogs.size() < PAGE_SIZE) {
            withdrawLogTV.disableLoadmore();
        }
        if (personalWithdrawLogs.size() <= 0) return;
        personalWithdrawLogList.clear();
        personalWithdrawLogList.addAll(personalWithdrawLogs);
        withDrawLogAdapter.appendPersonalLogList(personalWithdrawLogList);
    }

    @Override
    public void onGetShopWithdrawLog(List<ShopWithdrawLog> shopWithdrawLogs, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        if (shopWithdrawLogs.size() < PAGE_SIZE) {
            withdrawLogTV.disableLoadmore();
        }
        if (shopWithdrawLogs.size() <= 0) return;
        shopWithdrawLogList.clear();
        shopWithdrawLogList.addAll(shopWithdrawLogs);
        withDrawLogAdapter.appendShopLogList(shopWithdrawLogList);
    }

    @Override
    public void setPresenter(WithDrawLogContract.WithDrawLogPresenter presenter) {
        mPresenter = presenter;
    }
}
