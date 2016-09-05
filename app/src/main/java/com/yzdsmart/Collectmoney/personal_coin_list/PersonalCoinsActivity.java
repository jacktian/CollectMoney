package com.yzdsmart.Collectmoney.personal_coin_list;

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
import com.yzdsmart.Collectmoney.bean.GetCoinsLog;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/5.
 */
public class PersonalCoinsActivity extends BaseActivity implements PersonalCoinsContract.PersonalCoinsView {
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
    @BindView(R.id.coin_list)
    RecyclerView coinListRV;

    private static final String GET_COIN_LOG_CODE = "1666";
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private PersonalCoinsContract.PersonalCoinsPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<GetCoinsLog> logList;
    private PersonalCoinsAdapter personalCoinsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logList = new ArrayList<GetCoinsLog>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("获取金币日志列表");

        new PersonalCoinsPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        personalCoinsAdapter = new PersonalCoinsAdapter(this);
        coinListRV.setHasFixedSize(true);
        coinListRV.setLayoutManager(mLinearLayoutManager);
        coinListRV.addItemDecoration(dividerItemDecoration);
        coinListRV.setAdapter(personalCoinsAdapter);

        mPresenter.getCoinsLog(GET_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal_coins;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.publish_task})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    @Override
    public void onGetCoinsLog(List<GetCoinsLog> logList) {
        this.logList.clear();
        this.logList.addAll(logList);
        personalCoinsAdapter.appendList(this.logList);
    }

    @Override
    public void setPresenter(PersonalCoinsContract.PersonalCoinsPresenter presenter) {
        mPresenter = presenter;
    }
}
