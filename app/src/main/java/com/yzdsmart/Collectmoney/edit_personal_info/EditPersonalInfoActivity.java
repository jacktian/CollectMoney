package com.yzdsmart.Collectmoney.edit_personal_info;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.utils.Utils;
import com.yzdsmart.Collectmoney.views.BetterSpinner;
import com.yzdsmart.Collectmoney.views.pickerview.TimePickerDialog;
import com.yzdsmart.Collectmoney.views.pickerview.data.Type;
import com.yzdsmart.Collectmoney.views.pickerview.listener.OnDateSetListener;

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

    private EditPersonalInfoContract.EditPersonalInfoPresenter mPresenter;

    private AlertDialog editDialog;

    private DateTimeFormatter dtf;

    private long birthBefore = 150L * 365 * 1000 * 60 * 60 * 24L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("个人信息");

        new EditPersonalInfoPresenter(this, this);

        mPresenter.getCustDetailInfo("000000", "000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_personal_info;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.person_name, R.id.person_nickname, R.id.person_gender, R.id.person_phone, R.id.person_birth, R.id.person_area, R.id.person_address})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.person_name:
                showEditInfo("姓名", 0);
                break;
            case R.id.person_nickname:
                showEditInfo("昵称", 1);
                break;
            case R.id.person_gender:
                showEditInfo("性别", 2);
                break;
            case R.id.person_phone:
//                showEditInfo("电话", 3);
                break;
            case R.id.person_birth:
                showEditInfo("生日", 4);
                break;
            case R.id.person_area:
//                showEditInfo("省市区", 5);
                break;
            case R.id.person_address:
                showEditInfo("地址", 6);
                break;
        }
    }

    void showEditInfo(final String dialogTitle, final Integer editItem) {
        final String[] genderArray = getResources().getStringArray(R.array.gender_array);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
                R.layout.gender_array_item, genderArray);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.edit_info_dialog, null);
        TextView editInfoTitle = (TextView) view.findViewById(R.id.edit_info_dialog_title);
        editInfoTitle.setText("请输入" + dialogTitle);
        final EditText editInfoContent = (EditText) view.findViewById(R.id.edit_info_dialog_content);
        final BetterSpinner genderSpinner = (BetterSpinner) view.findViewById(R.id.edit_info_gender);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setText(genderArray[0]);
        switch (editItem) {
            case 2://性别
                editInfoContent.setVisibility(View.GONE);
                genderSpinner.setVisibility(View.VISIBLE);
                break;
            case 4://生日
                editInfoContent.setFocusable(false);
                editInfoContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                                .setCallBack(new OnDateSetListener() {
                                    @Override
                                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                        DateTime dateTime = new DateTime(millseconds);
                                        ((EditText) v).setText(dateTime.toString(dtf));
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
                    }
                });
                break;
            case 5://省市区
                editInfoContent.setFocusable(false);
                break;
        }
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
                switch (editItem) {
                    case 2:
                        break;
                    default:
                        if (editInfoContent.getText().toString().length() <= 0) {
                            editInfoContent.setError("请输入" + dialogTitle);
                            return;
                        }
                        break;
                }
                switch (editItem) {
                    case 0://姓名
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
                        break;
                    case 1://昵称
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null);
                        break;
                    case 2://性别
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, genderSpinner.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null);
                        break;
                    case 3://电话
                        if (!Utils.checkMobile(editInfoContent.getText().toString())) {
                            editInfoContent.setError("请输入正确的电话号码");
                            return;
                        }
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, null, null, editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null);
                        break;
                    case 4://生日
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, null, editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, null);
                        break;
                    case 5://省市区
//                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), editInfoContent.getText().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
                        break;
                    case 6://地址
                        mPresenter.setCustDetailInfo(editItem, "000000", SharedPreferencesUtils.getString(EditPersonalInfoActivity.this, "cust_code", ""), null, null, null, null, null, null, null, null, null, null, editInfoContent.getText().toString(), null, null, null, null, null);
                        break;
                }
            }
        });
        builder.setView(view);
        editDialog = builder.show();
        editDialog.setCancelable(false);
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
        EditText editInfoContentET = (EditText) editDialog.findViewById(R.id.edit_info_dialog_content);
        BetterSpinner genderSpinner = (BetterSpinner) editDialog.findViewById(R.id.edit_info_gender);
        switch (editItem) {
            case 0://姓名
                personNameTV.setText(editInfoContentET.getText().toString());
                break;
            case 1://昵称
                personNicknameTV.setText(editInfoContentET.getText().toString());
                break;
            case 2://性别
                personGenderTV.setText(genderSpinner.getText().toString());
                break;
            case 3://电话
                personPhoneTV.setText(editInfoContentET.getText().toString());
                break;
            case 4://生日
                personBirthTV.setText(editInfoContentET.getText().toString());
                DateTime birthDay = dtf.parseDateTime(editInfoContentET.getText().toString());
                personAgeTV.setText((Days.daysBetween(birthDay, new DateTime()).getDays() / 365 + 1) + "");
                break;
            case 5://省市区
                break;
            case 6://地址
                personAddressTV.setText(editInfoContentET.getText().toString());
                break;
        }
        editDialog.dismiss();
    }

    @Override
    public void setPresenter(EditPersonalInfoContract.EditPersonalInfoPresenter presenter) {
        mPresenter = presenter;
    }
}
