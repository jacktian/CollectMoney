package com.yzdsmart.Dingdingwen.register_login.set_info;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.CustomNestRadioGroup;
import com.yzdsmart.Dingdingwen.views.time_picker.TimePickerDialog;
import com.yzdsmart.Dingdingwen.views.time_picker.data.Type;
import com.yzdsmart.Dingdingwen.views.time_picker.listener.OnDateSetListener;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/23.
 */

public class SetInfoActivity extends BaseActivity implements SetInfoContract.SetInfoView {
    @Nullable
    @BindViews({R.id.register_login_name_layout, R.id.user_count_down_layout, R.id.user_pwd_layout, R.id.user_confirm_pwd_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    //    @Nullable
//    @BindView(R.id.user_name)
//    EditText userNameET;
    @Nullable
    @BindView(R.id.user_nickname)
    EditText nickNameET;
    @Nullable
    @BindView(R.id.gender_group)
    CustomNestRadioGroup userGenderRG;
    @Nullable
    @BindView(R.id.user_age)
    EditText userAgeET;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button confirmButton;

    private static final String TAG = "SetInfoActivity";

    private String userName, password;

    private SetInfoContract.SetInfoPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable registerSuccessRunnable;

    private DateTimeFormatter dtf;

    private long birthBefore = 150L * 365 * 1000 * 60 * 60 * 24L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        password = bundle.getString("password");

        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText(getResources().getString(R.string.register));
        confirmButton.setText(getResources().getString(R.string.register));
//        userNameET.setEnabled(false);
//        userNameET.setText(userName);

        new SetInfoPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        registerSuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                if (!Utils.isNetUsable(SetInfoActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.userLogin(userName, password, "", SharedPreferencesUtils.getString(SetInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(SetInfoActivity.this, "ddw_access_token", ""));
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_set_info;
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

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(registerSuccessRunnable);
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.user_age, R.id.register_calendar_icon, R.id.login_register_confirm_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.user_age:
            case R.id.register_calendar_icon:
                TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                userAgeET.setError(null, null);
                                DateTime dateTime = new DateTime(millseconds);
                                userAgeET.setText(dateTime.toString(dtf));
                            }
                        })
                        .setCancelTextColor(getResources().getColor(R.color.grey))
                        .setSureTextColor(getResources().getColor(R.color.grey))
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis() - birthBefore)
                        .setMaxMillseconds(System.currentTimeMillis())
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(Color.WHITE)
                        .setType(Type.YEAR_MONTH_DAY)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.light_grey))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.grey))
                        .setWheelItemTextSize(14)
                        .build();
                mDialogAll.show(getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.login_register_confirm_button:
                if (!requiredVerify(userAgeET)) {
                    userAgeET.setError(getResources().getString(R.string.input_age));
                    return;
                }
                if (!requiredVerify(nickNameET)) {
                    nickNameET.setError(getResources().getString(R.string.input_nickname));
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.userRegister(Constants.REGISTER_ACTION_CODE, userName, password, (userGenderRG.getCheckedRadioButtonId() == R.id.gender_male) ? "男" : "女", Integer.valueOf(Days.daysBetween(dtf.parseDateTime(userAgeET.getText().toString()), new DateTime()).getDays() / 365 + 1), nickNameET.getText().toString(), Utils.md5(Constants.REGISTER_ACTION_CODE + "yzd" + userName), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    @Override
    public void onUserRegister() {
        showProgressDialog(R.drawable.success, getResources().getString(R.string.register_success));
        mHandler.postDelayed(registerSuccessRunnable, 500);
    }

    @Override
    public void onUserLogin(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        openActivityClear(MainActivity.class, null, 0);
    }

    @Override
    public void setPresenter(SetInfoContract.SetInfoPresenter presenter) {
        mPresenter = presenter;
    }
}
