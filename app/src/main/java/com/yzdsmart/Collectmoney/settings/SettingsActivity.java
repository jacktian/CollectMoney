package com.yzdsmart.Collectmoney.settings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/3.
 */
public class SettingsActivity extends BaseActivity implements SettingsContract.SettingView {
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
    @BindView(R.id.person_name)
    TextView personNameTV;
    @Nullable
    @BindView(R.id.person_gender)
    TextView personGenderTV;
    @Nullable
    @BindView(R.id.person_phone)
    TextView personPhoneTV;
    @Nullable
    @BindView(R.id.person_age)
    TextView personAgeTV;
    @Nullable
    @BindView(R.id.person_area)
    TextView personAreaTV;
    @Nullable
    @BindView(R.id.person_address)
    TextView personAddressTV;

    private SettingsContract.SettingsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("设置");

        new SettingsPresenter(this, this);

//        mPresenter.getCustDetailInfo("000000", "000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.settings_logout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.settings_logout:
                SharedPreferencesUtils.clear(this, PreferenceManager.getDefaultSharedPreferences(this));
                App.getAppInstance().exitApp();
                openActivity(MainActivity.class);
                break;
        }
    }

    @Override
    public void setPresenter(SettingsContract.SettingsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetCustDetailInfo(CustDetailInfoRequestResponse response) {
        if (null != response.getCName() && !"".equals(response.getCName())) {
            personNameTV.setText(response.getCName());
        } else {
            personNameTV.setText(response.getC_UserCode());
        }
        personGenderTV.setText(response.getCSex());
        personPhoneTV.setText(response.getCTel());
        personAgeTV.setText(response.getCBirthday());
        personAreaTV.setText(response.getCProv() + response.getCCity() + response.getCDist());
        personAddressTV.setText(response.getCAddress());
    }
}
