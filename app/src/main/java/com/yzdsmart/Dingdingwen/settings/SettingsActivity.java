package com.yzdsmart.Dingdingwen.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

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

    private static final String TAG = "SettingsActivity";

    private SettingsContract.SettingsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("设置");

        new SettingsPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        ShareSDK.initSDK(this);

//        mPresenter.getCustDetailInfo("000000", "000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
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
    @OnClick({R.id.title_left_operation_layout, R.id.settings_logout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.settings_logout:
                showProgressDialog(R.drawable.loading, getResources().getString(R.string.logout));
                TIMManager.getInstance().logout(new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        hideProgressDialog();
                        showSnackbar("退出登录失败");
                    }

                    @Override
                    public void onSuccess() {
                        hideProgressDialog();
                        JPushInterface.stopPush(App.getAppInstance());
                        String platformName = SharedPreferencesUtils.getString(SettingsActivity.this, "platform", "");
                        if (null != platformName && platformName.length() > 0) {
                            Platform platform = null;
                            if ("qq".equals(platformName)) {
                                platform = ShareSDK.getPlatform(SettingsActivity.this, QQ.NAME);
                            } else if ("wb".equals(platformName)) {
                                platform = ShareSDK.getPlatform(SettingsActivity.this, SinaWeibo.NAME);
                            } else if ("wx".equals(platformName)) {
                                platform = ShareSDK.getPlatform(SettingsActivity.this, Wechat.NAME);
                            }
                            if (platform.isAuthValid()) {
                                platform.removeAccount(true);
                            }
                            SharedPreferencesUtils.remove(SettingsActivity.this, "platform");
                            SharedPreferencesUtils.remove(SettingsActivity.this, "exportData");
                            SharedPreferencesUtils.remove(SettingsActivity.this, "userGender");
                            SharedPreferencesUtils.remove(SettingsActivity.this, "userNickName");
                        }
                        SharedPreferencesUtils.remove(SettingsActivity.this, "baza_code");
                        SharedPreferencesUtils.remove(SettingsActivity.this, "cust_code");
                        SharedPreferencesUtils.remove(SettingsActivity.this, "im_account");
                        SharedPreferencesUtils.remove(SettingsActivity.this, "im_password");
                        MainActivity.getInstance().initBackgroundBag();
                        MainActivity.getInstance().clearRoutePlan();
                        openActivityClear(MainActivity.class, null, 0);
                    }
                });
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
