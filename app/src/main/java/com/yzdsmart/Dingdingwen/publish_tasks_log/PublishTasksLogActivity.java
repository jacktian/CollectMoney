package com.yzdsmart.Dingdingwen.publish_tasks_log;

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
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.PublishTaskLog;
import com.yzdsmart.Dingdingwen.buy_coins.BuyCoinsActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

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

    private static final String TAG = "PublishTasksLogActivity";

    private PublishTasksLogContract.PublishTasksLogPresenter mPresenter;

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

        MobclickAgent.openActivityDurationTrack(false);

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
                getPublishTaskLog();
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
                getPublishTaskLog();
            }
        });
        getPublishTaskLog();
    }

    private void getPublishTaskLog() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.publishTaskLog(Constants.PUBLISH_TASK_LOG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE, lastsequence, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_publish_tasks_log;
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

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.buy_coin})
    void onClick(final View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.buy_coin:
                Bundle bundle = new Bundle();
                bundle.putInt("userType", 1);
                openActivity(BuyCoinsActivity.class, bundle, 0);
                break;
        }
    }

    @Override
    public void setPresenter(PublishTasksLogContract.PublishTasksLogPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPublishTaskLog(List<PublishTaskLog> logList, Integer lastsequence) {
        this.lastsequence = lastsequence;
        pageIndex++;
        if (logList.size() < PAGE_SIZE) {
            publishListRV.disableLoadmore();
        }
        if (logList.size() <= 0) {
            if (2 == pageIndex) {
                showSnackbar("没有数据,下拉刷新");
            }
            return;
        }
        this.logList.clear();
        this.logList.addAll(logList);
        publishTasksAdapter.appendList(this.logList);
    }
}
