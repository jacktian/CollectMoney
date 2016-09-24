package com.yzdsmart.Collectmoney.edit_personal_info;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                showEditInfo("电话", 3);
                break;
            case R.id.person_birth:
                showEditInfo("生日", 4);
                break;
            case R.id.person_area:
                showEditInfo("省市区", 5);
                break;
            case R.id.person_address:
                showEditInfo("地址", 6);
                break;
        }
    }

    void showEditInfo(final String dialogTitle, final Integer editItem) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.edit_info_dialog, null);
        TextView editInfoTitle = (TextView) view.findViewById(R.id.edit_info_dialog_title);
        editInfoTitle.setText(dialogTitle);
        final EditText editInfoContent = (EditText) view.findViewById(R.id.edit_info_dialog_content);
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
                switch (editItem) {
                    case 0://姓名
                        break;
                    case 1://昵称
                        break;
                    case 2://性别
                        break;
                    case 3://电话
                        break;
                    case 4://生日
                        break;
                    case 5://省市区
                        break;
                    case 6://地址
                        break;
                }
                editDialog.dismiss();
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
        personGenderTV.setText(response.getCSex());
        personPhoneTV.setText(response.getCTel());
        personBirthTV.setText(response.getCBirthday());
        personAgeTV.setText(response.getCBirthday());
        personAreaTV.setText(response.getCProv() + response.getCCity() + response.getCDist());
        personAddressTV.setText(response.getCAddress());
    }

    @Override
    public void onSetCustDetailInfo() {

    }

    @Override
    public void setPresenter(EditPersonalInfoContract.EditPersonalInfoPresenter presenter) {
        mPresenter = presenter;
    }
}
