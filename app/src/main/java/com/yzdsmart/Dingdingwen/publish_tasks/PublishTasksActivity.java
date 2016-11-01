package com.yzdsmart.Dingdingwen.publish_tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.share_sdk.OnekeyShare;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.views.time_picker.TimePickerDialog;
import com.yzdsmart.Dingdingwen.views.time_picker.data.Type;
import com.yzdsmart.Dingdingwen.views.time_picker.listener.OnDateSetListener;

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
import cn.sharesdk.framework.ShareSDK;

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
    private Runnable publishTaskRunnable;

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

        ShareSDK.initSDK(this);

        publishTaskRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                closeActivity();
                showShare();
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
        mHandler.removeCallbacks(publishTaskRunnable);
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
        mHandler.postDelayed(publishTaskRunnable, 500);
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
