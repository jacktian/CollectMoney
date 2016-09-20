package com.yzdsmart.Collectmoney.shop_focuser;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ShopFocuser;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/20.
 */

public class ShopFocuserActivity extends BaseActivity implements ShopFocuserContract.ShopFocuserView {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.focuser_counts)
    TextView focuserCountsTV;
    @Nullable
    @BindView(R.id.shop_focuser_list)
    RecyclerView shopFocuserRV;

    private Integer focuserCounts = 0;
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private static final String SHOP_FOCUSER_ACTION_CODE = "9012";

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<ShopFocuser> shopFocuserList;
    private ShopFocuserAdapter shopFocuserAdapter;

    private ShopFocuserContract.ShopFocuserPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shopFocuserList = new ArrayList<ShopFocuser>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));

        new ShopFocuserPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        shopFocuserAdapter = new ShopFocuserAdapter(this);
        shopFocuserRV.setHasFixedSize(true);
        shopFocuserRV.setLayoutManager(mLinearLayoutManager);
        shopFocuserRV.addItemDecoration(dividerItemDecoration);
        shopFocuserRV.setAdapter(shopFocuserAdapter);

        mPresenter.getShopFocuser(SHOP_FOCUSER_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_focuser;
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
    public void onGetShopFocuser(List<ShopFocuser> shopFocusers) {
        focuserCounts += shopFocusers.size();
        focuserCountsTV.setText("" + focuserCounts);
        shopFocuserList.clear();
        shopFocuserList.addAll(shopFocusers);
        shopFocuserAdapter.appendList(shopFocuserList);
    }

    @Override
    public void setPresenter(ShopFocuserContract.ShopFocuserPresenter presenter) {
        mPresenter = presenter;
    }
}
