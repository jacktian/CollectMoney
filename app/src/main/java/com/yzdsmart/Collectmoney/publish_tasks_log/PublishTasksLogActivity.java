package com.yzdsmart.Collectmoney.publish_tasks_log;

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
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.PublishTaskLog;
import com.yzdsmart.Collectmoney.buy_coins.BuyCoinsActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/18.
 */
public class PublishTasksLogActivity extends BaseActivity implements PublishTasksLogContract.PublishTasksLogView {
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
    @BindView(R.id.publish_list)
    UltimateRecyclerView publishListRV;
    @Nullable
    @BindView(R.id.tasks_left_coins)
    TextView tasksLeftCoinsTV;

    private PublishTasksLogContract.PublishTasksLogPresenter mPresenter;

    private static final String GET_TASKS_LEFT_COINS_ACTION_CODE = "7288";
    private static final String PUBLISH_TASK_LOG_CODE = "2188";
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 15;
    private Integer lastsequence = 0;//保存的分页数列值，第一页默认为：0  第二页开始必须根据第一页返回值lastsequence进行传递

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<PublishTaskLog> logList;
    private PublishTasksAdapter publishTasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logList = new ArrayList<PublishTaskLog>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("发布任务日志");

        new PublishTasksLogPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.divider_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        publishTasksAdapter = new PublishTasksAdapter(this);
        publishListRV.setHasFixedSize(true);
        publishListRV.setLayoutManager(mLinearLayoutManager);
        publishListRV.addItemDecoration(dividerItemDecoration);
        publishListRV.setAdapter(publishTasksAdapter);
        publishListRV.reenableLoadmore();
        publishListRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                mPresenter.publishTaskLog(PUBLISH_TASK_LOG_CODE, "000000", SharedPreferencesUtils.getString(PublishTasksLogActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
            }
        });
        publishListRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                publishListRV.setRefreshing(false);
                publishListRV.reenableLoadmore();
                lastsequence = 0;
                pageIndex = 1;
                publishTasksAdapter.clearList();
                mPresenter.publishTaskLog(PUBLISH_TASK_LOG_CODE, "000000", SharedPreferencesUtils.getString(PublishTasksLogActivity.this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
            }
        });

        mPresenter.getLeftCoins(GET_TASKS_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
        mPresenter.publishTaskLog(PUBLISH_TASK_LOG_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_publish_tasks_log;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.buy_coin})
    void onClick(final View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.buy_coin:
                openActivity(BuyCoinsActivity.class);
                break;
        }
    }

    @Override
    public void setPresenter(PublishTasksLogContract.PublishTasksLogPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetLeftCoins(Integer counts) {
        tasksLeftCoinsTV.setText(" " + counts);
    }

    @Override
    public void onPublishTaskLog(List<PublishTaskLog> logList, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        if (logList.size() < PAGE_SIZE) {
            publishListRV.disableLoadmore();
        }
        if (logList.size() <= 0) return;
        this.logList.clear();
        this.logList.addAll(logList);
        publishTasksAdapter.appendList(this.logList);
    }
}
