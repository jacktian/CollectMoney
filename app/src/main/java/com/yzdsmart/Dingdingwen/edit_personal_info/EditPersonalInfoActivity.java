package com.yzdsmart.Dingdingwen.edit_personal_info;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.crop.ImageCropActivity;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.city_picker.CityPickerDialog;
import com.yzdsmart.Dingdingwen.views.city_picker.listener.OnCitySetListener;
import com.yzdsmart.Dingdingwen.views.gender_picker.GenderPickerDialog;
import com.yzdsmart.Dingdingwen.views.gender_picker.listener.OnGenderSetListener;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/9/24.
 */

public class EditPersonalInfoActivity extends BaseActivity implements EditPersonalInfoContract.EditPersonalInfoView {
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
    @BindView(R.id.personal_avater)
    CircleImageView personalAvaterIV;
    @Nullable
    @BindView(R.id.person_name)
    TextView personNameTV;
    @Nullable
    @BindView(R.id.person_nickname)
    TextView personNicknameTV;
    @Nullable
    @BindView(R.id.person_gender)
    TextView personGenderTV;
    @Nullable
    @BindView(R.id.person_phone)
    TextView personPhoneTV;
    @Nullable
    @BindView(R.id.person_birth)
    TextView personBirthTV;
    @Nullable
    @BindView(R.id.person_age)
    TextView personAgeTV;
    @Nullable
    @BindView(R.id.person_area)
    TextView personAreaTV;
    @Nullable
    @BindView(R.id.person_address)
    TextView personAddressTV;

    private static final String TAG = "EditPersonalInfoActivity";

    private EditPersonalInfoContract.EditPersonalInfoPresenter mPresenter;

    private AlertDialog editDialog;

    private DateTimeFormatter dtf;
    private String birthdayChecked = "";
    private String cityChecked = "";
    private String genderChecked = "";

    private long birthBefore = 150L * 365 * 1000 * 60 * 60 * 24L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("个人资料");

        new EditPersonalInfoPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getCustInfo("000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        mPresenter.getCustDetailInfo("000000", "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_personal_info;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.person_name, R.id.person_nickname, R.id.person_gender, R.id.person_phone, R.id.person_birth, R.id.person_area, R.id.person_address, R.id.personal_avater})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.person_name:
                showEditInfo("姓名", 0, personNameTV.getText().toString());
                break;
            case R.id.person_nickname:
                showEditInfo("昵称", 1, personNicknameTV.getText().toString());
                break;
            case R.id.person_gender:
                GenderPickerDialog mGenderPickerDialog = new GenderPickerDialog.Builder()
                        .setCallBack(new OnGenderSetListener() {
                            @Override
                            public void onGenderSet(GenderPickerDialog genderPickerView, String gender) {
                                genderChecked = gender;
                                mPresenter.setCustDetailInfo(2, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, gender, null, null, null, null, null, null, null, null, null, null, null, null, null, SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_access_token", ""));
                            }
                        })
                        .setCancelTextColor(getResources().getColor(R.color.font_grey))
                        .setSureTextColor(getResources().getColor(R.color.font_grey))
                        .setCyclic(false)
                        .setThemeColor(Color.WHITE)
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.light_grey))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.font_grey))
                        .setWheelItemTextSize(14)
                        .build();
                mGenderPickerDialog.show(getSupportFragmentManager(), "all");
                break;
            case R.id.person_phone:
//                showEditInfo("电话", 3);
                break;
            case R.id.person_birth:
                TimePickerDialog mTimePickerDialog = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                DateTime dateTime = new DateTime(millseconds);
                                birthdayChecked = dateTime.toString(dtf);
                                mPresenter.setCustDetailInfo(4, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, null, birthdayChecked, null, null, null, null, null, null, null, null, null, null, null, null, SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_access_token", ""));
                            }
                        })
                        .setCancelTextColor(getResources().getColor(R.color.font_grey))
                        .setSureTextColor(getResources().getColor(R.color.font_grey))
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis() - birthBefore)
                        .setMaxMillseconds(System.currentTimeMillis())
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(Color.WHITE)
                        .setType(Type.YEAR_MONTH_DAY)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.light_grey))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.font_grey))
                        .setWheelItemTextSize(14)
                        .build();
                mTimePickerDialog.show(getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.person_area:
                CityPickerDialog mCityPickerDialog = new CityPickerDialog.Builder()
                        .setCallBack(new OnCitySetListener() {
                            @Override
                            public void onCitySet(CityPickerDialog cityPickerView, String province, String city, String district) {
                                cityChecked = province + city + district;
                                mPresenter.setCustDetailInfo(5, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, null, null, null, null, null, null, null, null, null, province, city, district, null, null, SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_access_token", ""));
                            }
                        })
                        .setCancelTextColor(getResources().getColor(R.color.font_grey))
                        .setSureTextColor(getResources().getColor(R.color.font_grey))
                        .setCyclic(false)
                        .setThemeColor(Color.WHITE)
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.light_grey))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.font_grey))
                        .setWheelItemTextSize(14)
                        .build();
                mCityPickerDialog.show(getSupportFragmentManager(), "all");
                break;
            case R.id.person_address:
                showEditInfo("地址", 6, personAddressTV.getText().toString());
                break;
            case R.id.personal_avater:
                openActivity(ImageCropActivity.class);
                break;
        }
    }

    void showEditInfo(final String dialogTitle, final Integer editItem, String oldValue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.edit_info_dialog, null);
        TextView editInfoTitle = (TextView) view.findViewById(R.id.edit_info_dialog_title);
        editInfoTitle.setText("请输入" + dialogTitle);
        final EditText editInfoContent = (EditText) view.findViewById(R.id.edit_info_dialog_content);
        editInfoContent.setText(oldValue);
        editInfoContent.setSelection(oldValue.length());
        Button editCancel = (Button) view.findViewById(R.id.edit_cancel);
        Button editConfirm = (Button) view.findViewById(R.id.edit_confirm);
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        editConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInfoContent.getText().toString().length() <= 0) {
                    editInfoContent.setError("请输入" + dialogTitle);
                    return;
                }
                if (!Utils.isNetUsable(EditPersonalInfoActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                switch (editItem) {
                    case 0://姓名
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_access_token", ""));
                        break;
                    case 1://昵称
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_access_token", ""));
                        break;
                    case 3://电话
                        if (!Utils.checkMobile(editInfoContent.getText().toString())) {
                            editInfoContent.setError("请输入正确的电话号码");
                            return;
                        }
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, null, null, editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_access_token", ""));
                        break;
                    case 6://地址
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, null, null, null, null, null, null, null, null, editInfoContent.getText().toString(), null, null, null, null, null, SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "ddw_access_token", ""));
                        break;
                }
            }
        });
        builder.setView(view);
        editDialog = builder.show();
        editDialog.setCancelable(false);
    }

    @Override
    public void onGetCustInfo(CustInfoRequestResponse response) {
        Glide.with(this).load(response.getImageUrl()).asBitmap().placeholder(getResources().getDrawable(R.mipmap.ic_holder_light)).error(getResources().getDrawable(R.mipmap.user_avater)).into(personalAvaterIV);
    }

    @Override
    public void onGetCustDetailInfo(CustDetailInfoRequestResponse response) {
        if (null != response.getCName() && !"".equals(response.getCName())) {
            personNameTV.setText(response.getCName());
        } else {
            personNameTV.setText(response.getC_UserCode());
        }
        personNicknameTV.setText(response.getCNickName());
        personGenderTV.setText(response.getCSex());
        personPhoneTV.setText(response.getCTel());
        if (null != response.getCBirthday() && !"".equals(response.getCBirthday())) {
            DateTime birthDay = dtf.parseDateTime(response.getCBirthday());
            personBirthTV.setText(birthDay.toString("yyyy-MM-dd"));
            personAgeTV.setText((Days.daysBetween(birthDay, new DateTime()).getDays() / 365 + 1) + "");
        }
        personAreaTV.setText(response.getCProv() + response.getCCity() + response.getCDist());
        personAddressTV.setText(response.getCAddress());
    }

    @Override
    public void onSetCustDetailInfo(Integer editItem) {
        EditText editInfoContentET;
        switch (editItem) {
            case 0://姓名
                editInfoContentET = (EditText) editDialog.findViewById(R.id.edit_info_dialog_content);
                personNameTV.setText(editInfoContentET.getText().toString());
                editDialog.dismiss();
                break;
            case 1://昵称
                editInfoContentET = (EditText) editDialog.findViewById(R.id.edit_info_dialog_content);
                personNicknameTV.setText(editInfoContentET.getText().toString());
                editDialog.dismiss();
                break;
            case 2://性别
                personGenderTV.setText(genderChecked);
                break;
            case 3://电话
                editInfoContentET = (EditText) editDialog.findViewById(R.id.edit_info_dialog_content);
                personPhoneTV.setText(editInfoContentET.getText().toString());
                editDialog.dismiss();
                break;
            case 4://生日
                personBirthTV.setText(birthdayChecked);
                DateTime birthDay = dtf.parseDateTime(birthdayChecked);
                personAgeTV.setText((Days.daysBetween(birthDay, new DateTime()).getDays() / 365 + 1) + "");
                break;
            case 5://省市区
                personAreaTV.setText(cityChecked);
                break;
            case 6://地址
                editInfoContentET = (EditText) editDialog.findViewById(R.id.edit_info_dialog_content);
                personAddressTV.setText(editInfoContentET.getText().toString());
                editDialog.dismiss();
                break;
        }
    }

    @Override
    public void setPresenter(EditPersonalInfoContract.EditPersonalInfoPresenter presenter) {
        mPresenter = presenter;
    }
}
