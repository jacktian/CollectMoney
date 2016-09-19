package com.yzdsmart.Collectmoney.publish_tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.views.pickerview.TimePickerDialog;
import com.yzdsmart.Collectmoney.views.pickerview.data.Type;
import com.yzdsmart.Collectmoney.views.pickerview.listener.OnDateSetListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    //    @Nullable
//    @BindView(R.id.total_coin_counts)
//    EditText totalCoinCountsET;
//    @Nullable
//    @BindView(R.id.min_coin_counts)
//    EditText minCoinCountsET;
//    @Nullable
//    @BindView(R.id.max_coin_counts)
//    EditText maxCoinCountsET;
    @Nullable
    @BindView(R.id.begin_time)
    EditText beginTimeET;
    @Nullable
    @BindView(R.id.end_time)
    EditText endTimeET;

    private PublishTasksContract.PublishTasksPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable closeRunnable;

    private DateTimeFormatter dtf;
    private long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("发布任务");

        new PublishTasksPresenter(this, this);

        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        closeRunnable = new Runnable() {
            @Override
            public void run() {
                closeActivity();
            }
        };
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
//                if (!requiredVerify(totalCoinCountsET)) {
//                    totalCoinCountsET.setError(getResources().getString(R.string.publish_task_total_coin_required));
//                    return;
//                }
//                if (!requiredVerify(minCoinCountsET)) {
//                    minCoinCountsET.setError(getResources().getString(R.string.publish_task_min_coin_required));
//                    return;
//                }
//                if (!requiredVerify(maxCoinCountsET)) {
//                    maxCoinCountsET.setError(getResources().getString(R.string.publish_task_max_coin_required));
//                    return;
//                }
                if (!requiredVerify(beginTimeET)) {
                    beginTimeET.setError(getResources().getString(R.string.publish_task_begin_time_required));
                    return;
                }
                if (!requiredVerify(endTimeET)) {
                    endTimeET.setError(getResources().getString(R.string.publish_task_end_time_required));
                    return;
                }
//                Integer totalCoinCounts = Integer.valueOf(totalCoinCountsET.getText().toString());
//                Integer minCoinCounts = Integer.valueOf(minCoinCountsET.getText().toString());
//                Integer maxCoinCounts = Integer.valueOf(maxCoinCountsET.getText().toString());
                String beginTime = beginTimeET.getText().toString();
                String endTime = endTimeET.getText().toString();
//                mPresenter.publishTask("000000", SharedPreferencesUtils.getString(this, "baza_code", ""), totalCoinCounts, minCoinCounts, maxCoinCounts, beginTime, endTime);
                break;
            case R.id.begin_time:
            case R.id.end_time:
                TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                DateTime dateTime = new DateTime(millseconds);
                                ((EditText) view).setText(dateTime.toString(dtf));
                            }
                        })
//                        .setCancelStringId("取消")
                        .setCancelTextColor(getResources().getColor(R.color.grey))
//                        .setSureStringId("确认")
                        .setSureTextColor(getResources().getColor(R.color.grey))
//                        .setTitleStringId("")
//                        .setYearText("年")
//                        .setMonthText("月")
//                        .setDayText("日")
//                        .setHourText("时")
//                        .setMinuteText("分")
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis())
                        .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(Color.WHITE)
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.light_grey))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.grey))
                        .setWheelItemTextSize(14)
                        .build();
                mDialogAll.show(getSupportFragmentManager(), "all");
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
}
