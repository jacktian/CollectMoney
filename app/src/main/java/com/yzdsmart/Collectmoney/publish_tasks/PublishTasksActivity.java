package com.yzdsmart.Collectmoney.publish_tasks;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.PublishTaskLog;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/4.
 */
public class PublishTasksActivity extends BaseActivity implements PublishTasksContract.PublishTasksView {
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
    @BindView(R.id.total_coin_counts)
    EditText totalCoinCountsET;
    @Nullable
    @BindView(R.id.min_coin_counts)
    EditText minCoinCountsET;
    @Nullable
    @BindView(R.id.max_coin_counts)
    EditText maxCoinCountsET;
    @Nullable
    @BindView(R.id.begin_time)
    EditText beginTimeET;
    @Nullable
    @BindView(R.id.end_time)
    EditText endTimeET;
    @Nullable
    @BindView(R.id.publish_list)
    RecyclerView publishListRV;

    private static final String PUBLISH_TASK_LOG_CODE = "2188";
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<PublishTaskLog> logList;
    private PublishTasksAdapter publishTasksAdapter;

    private PublishTasksContract.PublishTasksPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable closeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logList = new ArrayList<PublishTaskLog>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("发布任务");

        new PublishTasksPresenter(this, this);

        closeRunnable = new Runnable() {
            @Override
            public void run() {
                closeActivity();
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        publishTasksAdapter = new PublishTasksAdapter(this);
        publishListRV.setHasFixedSize(true);
        publishListRV.setLayoutManager(mLinearLayoutManager);
        publishListRV.addItemDecoration(dividerItemDecoration);
        publishListRV.setAdapter(publishTasksAdapter);

        mPresenter.publishTaskLog(PUBLISH_TASK_LOG_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_publish_tasks;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.publish_task, R.id.begin_time, R.id.end_time})
    void onClick(final View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.publish_task:
                if (!requiredVerify(totalCoinCountsET)) {
                    totalCoinCountsET.setError(getResources().getString(R.string.publish_task_total_coin_required));
                    return;
                }
                if (!requiredVerify(minCoinCountsET)) {
                    minCoinCountsET.setError(getResources().getString(R.string.publish_task_min_coin_required));
                    return;
                }
                if (!requiredVerify(maxCoinCountsET)) {
                    maxCoinCountsET.setError(getResources().getString(R.string.publish_task_max_coin_required));
                    return;
                }
                if (!requiredVerify(beginTimeET)) {
                    beginTimeET.setError(getResources().getString(R.string.publish_task_begin_time_required));
                    return;
                }
                if (!requiredVerify(endTimeET)) {
                    endTimeET.setError(getResources().getString(R.string.publish_task_end_time_required));
                    return;
                }
                Integer totalCoinCounts = Integer.valueOf(totalCoinCountsET.getText().toString());
                Integer minCoinCounts = Integer.valueOf(minCoinCountsET.getText().toString());
                Integer maxCoinCounts = Integer.valueOf(maxCoinCountsET.getText().toString());
                String beginTime = beginTimeET.getText().toString();
                String endTime = endTimeET.getText().toString();
                mPresenter.publishTask("000000", SharedPreferencesUtils.getString(this, "baza_code", ""), totalCoinCounts, minCoinCounts, maxCoinCounts, beginTime, endTime);
                break;
            case R.id.begin_time:
            case R.id.end_time:
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(PublishTasksActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        ((EditText) view).setText(dateDesc);
                    }
                }).textConfirm("确定") //text of confirm button
                        .textCancel("取消") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#fc7d1c"))//color of confirm button
                        .minYear(2016) //min year in loop
                        .maxYear(2550) // max year in loop
                        .dateChose("2016-01-01") // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(PublishTasksActivity.this);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(closeRunnable);
        super.onDestroy();
    }

    @Override
    public void setPresenter(PublishTasksContract.PublishTasksPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPublishTask(boolean flag, String msg) {
        showSnackbar(msg);
        if (!flag) {
            return;
        }
        mHandler.postDelayed(closeRunnable, 1500);
    }

    @Override
    public void onPublishTaskLog(List<PublishTaskLog> logList) {
        this.logList.clear();
        this.logList.addAll(logList);
        publishTasksAdapter.appendList(this.logList);
    }
}
