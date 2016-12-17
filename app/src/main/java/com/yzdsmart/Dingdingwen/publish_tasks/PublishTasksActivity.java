package com.yzdsmart.Dingdingwen.publish_tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.time_picker.TimePickerDialog;
import com.yzdsmart.Dingdingwen.views.time_picker.data.Type;
import com.yzdsmart.Dingdingwen.views.time_picker.listener.OnDateSetListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
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
    @BindView(R.id.coin_types)
    Spinner coinTypesBS;
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

    private static final String TAG = "PublishTasksActivity";

    private PublishTasksContract.PublishTasksPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable publishTaskRunnable;

    private DateTimeFormatter dtf;
    private long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

    private boolean packageCountsPass = false;
    private boolean packageAmountPass = false;
    private boolean packageBeginPass = false;
    private boolean packageEndPass = false;

    private CoinTypesAdapter coinTypesAdapter;
    private CoinType defaultCoinType;
    private CoinType selectedType;
    private List<CoinType> coinTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coinTypeList = new ArrayList<CoinType>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("发布任务");

        new PublishTasksPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        publishTaskRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                closeActivity();
            }
        };

        defaultCoinType = new CoinType(0, "普通金币", "");
        coinTypesAdapter = new CoinTypesAdapter(this);
        coinTypesBS.setAdapter(coinTypesAdapter);
        coinTypesBS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = coinTypeList.get(i);
                if (!Utils.isNetUsable(PublishTasksActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getShopLeftCoins(Constants.GET_LEFT_COINS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(PublishTasksActivity.this, "baza_code", ""), selectedType.getGoldType(), SharedPreferencesUtils.getString(PublishTasksActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(PublishTasksActivity.this, "ddw_access_token", ""));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (!Utils.isNetUsable(PublishTasksActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopCoinTypes("", SharedPreferencesUtils.getString(this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_publish_tasks;
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
                mPresenter.publishTask("000000", SharedPreferencesUtils.getString(this, "baza_code", ""), Integer.valueOf(totalCoinAmounts), Integer.valueOf(totalCoinCounts), 0, beginTime, endTime, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
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
        mHandler.removeCallbacks(publishTaskRunnable);
        super.onDestroy();
    }

    @Override
    public void setPresenter(PublishTasksContract.PublishTasksPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetShopLeftCoins(Float counts) {
        leftCoinsTV.setText(" " + counts);
    }

    @Override
    public void onPublishTask(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        showProgressDialog(R.drawable.success, getResources().getString(R.string.publish_success));
        mHandler.postDelayed(publishTaskRunnable, 500);
    }

    @Override
    public void onGetCoinTypes(List<CoinType> coinTypes) {
        coinTypeList.clear();
        coinTypesAdapter.clearList();
        if (0 == coinTypes.size()) {
            coinTypeList.add(defaultCoinType);
            coinTypesAdapter.appendList(Collections.singletonList(defaultCoinType));
        } else {
            coinTypeList.addAll(coinTypes);
            coinTypesAdapter.appendList(coinTypes);
        }
    }

}
