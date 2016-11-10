package com.yzdsmart.Dingdingwen.register_business;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/4.
 */
public class RegisterBusinessActivity extends BaseActivity implements RegisterBusinessContract.RegisterBusinessView {
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
    @BindView(R.id.business_name)
    EditText businessNameET;
    @Nullable
    @BindView(R.id.business_pers)
    EditText businessPersET;
    @Nullable
    @BindView(R.id.business_tel)
    EditText businessTelET;
    @Nullable
    @BindView(R.id.business_address)
    EditText businessAddressET;
    @Nullable
    @BindView(R.id.business_remark)
    EditText businessRemarkET;
    @Nullable
    @BindView(R.id.business_coor)
    EditText businessCoorET;

    private RegisterBusinessContract.RegisterBusinessPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable closeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("升级商户");

        businessCoorET.setText(SharedPreferencesUtils.getString(this, "qLocation", ""));

        new RegisterBusinessPresenter(this, this);

        closeRunnable = new Runnable() {
            @Override
            public void run() {
                App.getAppInstance().exitApp();
                openActivity(MainActivity.class);
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register_business;
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

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.refresh_coor, R.id.submit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.refresh_coor:
                businessCoorET.setText(SharedPreferencesUtils.getString(this, "qLocation", ""));
                break;
            case R.id.submit:
                if (!requiredVerify(businessNameET)) {
                    businessNameET.setError(getResources().getString(R.string.register_business_name_required));
                    return;
                }
                if (!requiredVerify(businessPersET)) {
                    businessPersET.setError(getResources().getString(R.string.register_business_pers_required));
                    return;
                }
                if (!requiredVerify(businessTelET)) {
                    businessTelET.setError(getResources().getString(R.string.register_business_tel_required));
                    return;
                }
                if (!requiredVerify(businessAddressET)) {
                    businessAddressET.setError(getResources().getString(R.string.register_business_address_required));
                    return;
                }
                if (!requiredVerify(businessRemarkET)) {
                    businessRemarkET.setError(getResources().getString(R.string.register_business_remark_required));
                    return;
                }
                if (!requiredVerify(businessCoorET)) {
                    businessCoorET.setError(getResources().getString(R.string.register_business_coor_required));
                    return;
                }
                String businessName = businessNameET.getText().toString();
                String businessPers = businessPersET.getText().toString();
                String businessTel = businessTelET.getText().toString();
                String businessAddress = businessAddressET.getText().toString();
                String businessRemark = businessRemarkET.getText().toString();
                String businessCoor = businessCoorET.getText().toString();
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.registerBusiness("000000", SharedPreferencesUtils.getString(this, "cust_code", ""), businessName, businessPers, businessTel, businessAddress, businessRemark, businessCoor, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
        }
    }

    @Override
    public void setPresenter(RegisterBusinessContract.RegisterBusinessPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRegisterBusiness(boolean flag, String msg) {
        showSnackbar(msg);
        if (!flag) {
            return;
        }
        SharedPreferencesUtils.clear(this, PreferenceManager.getDefaultSharedPreferences(this));
        mHandler.postDelayed(closeRunnable, 1500);
    }
}
