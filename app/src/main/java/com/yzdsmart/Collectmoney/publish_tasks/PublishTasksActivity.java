package com.yzdsmart.Collectmoney.publish_tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.Constants;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
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
import butterknife.OnTextChanged;
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
    @BindView(R.id.left_coins)
    TextView leftCoinsTV;
    @Nullable
    @BindView(R.id.total_coin_counts)
    EditText totalCoinCountsET;
    @Nullable
    @BindView(R.id.total_coin_amounts)
    EditText totalCoinAmountsET;
    @Nullable
    @BindView(R.id.begin_time)
    EditText beginTimeET;
    @Nullable
    @BindView(R.id.end_time)
    EditText endTimeET;
    @Nullable
    @BindView(R.id.publish_task)
    Button publishTaskBtn;

    private PublishTasksContract.PublishTasksPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable closeRunnable;

    private DateTimeFormatter dtf;
    private long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

    private boolean packageCountsPass = false;
    private boolean packageAmountPass = false;
    private boolean packageBeginPass = false;
    private boolean packageEndPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("发布任务");

        new PublishTasksPresenter(this, this);

        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        closeRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                closeActivity();
            }
        };

        mPresenter.getLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_publish_tasks;
    }

    @Optional
    @OnTextChanged({R.id.total_coin_counts})
    void onPackageCountsTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().length() > 0) {
            packageCountsPass = true;
        } else {
            packageCountsPass = false;
        }
        if (packageCountsPass && packageAmountPass && packageBeginPass && packageEndPass) {
            publishTaskBtn.setEnabled(true);
        } else {
            publishTaskBtn.setEnabled(false);
        }
    }

    @Optional
    @OnTextChanged({R.id.total_coin_amounts})
    void onPackageAmountsTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().length() > 0) {
            packageAmountPass = true;
        } else {
            packageAmountPass = false;
        }
        if (packageCountsPass && packageAmountPass && packageBeginPass && packageEndPass) {
            publishTaskBtn.setEnabled(true);
        } else {
            publishTaskBtn.setEnabled(false);
        }
    }

    @Optional
    @OnTextChanged({R.id.begin_time})
    void onBeginTimeTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().length() > 0) {
            packageBeginPass = true;
        } else {
            packageBeginPass = false;
        }
        if (packageCountsPass && packageAmountPass && packageBeginPass && packageEndPass) {
            publishTaskBtn.setEnabled(true);
        } else {
            publishTaskBtn.setEnabled(false);
        }
    }

    @Optional
    @OnTextChanged({R.id.end_time})
    void onEndTimeTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().length() > 0) {
            packageEndPass = true;
        } else {
            packageEndPass = false;
        }
        if (packageCountsPass && packageAmountPass && packageBeginPass && packageEndPass) {
            publishTaskBtn.setEnabled(true);
        } else {
            publishTaskBtn.setEnabled(false);
        }
    }


    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.publish_task, R.id.begin_time, R.id.end_time})
    void onClick(final View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.publish_task:
                String totalCoinCounts = totalCoinCountsET.getText().toString();
                String totalCoinAmounts = totalCoinAmountsET.getText().toString();
                String beginTime = beginTimeET.getText().toString();
                String endTime = endTimeET.getText().toString();
                mPresenter.publishTask("000000", SharedPreferencesUtils.getString(this, "baza_code", ""), Integer.valueOf(totalCoinAmounts), Integer.valueOf(totalCoinCounts), beginTime, endTime);
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
    public void onGetLeftCoins(Integer counts) {
        leftCoinsTV.setText(" " + counts);
    }

    @Override
    public void onPublishTask(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        showProgressDialog(R.drawable.success, getResources().getString(R.string.publish_success));
        mHandler.postDelayed(closeRunnable, 500);
    }
}
