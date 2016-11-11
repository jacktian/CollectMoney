package com.yzdsmart.Dingdingwen.edit_shop_info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoByPersRequestResponse;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/25.
 */

public class EditShopInfoActivity extends BaseActivity implements EditShopInfoContract.EditShopInfoView {
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
    @BindView(R.id.baza_name)
    TextView bazaNameTV;
    @Nullable
    @BindView(R.id.baza_pers)
    TextView bazaPersTV;
    @Nullable
    @BindView(R.id.baza_tel)
    TextView bazaTelTV;
    @Nullable
    @BindView(R.id.baza_address)
    TextView bazaAddressTV;
    @Nullable
    @BindView(R.id.baza_remark)
    TextView bazaRemarkTV;

    private static final String TAG = "EditShopInfoActivity";

    private EditShopInfoContract.EditShopInfoPresenter mPresenter;

    private AlertDialog editDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        centerTitleTV.setText("商铺信息");

        new EditShopInfoPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopInfo(Constants.GET_SHOP_INFO_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_shop_info;
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
    @OnClick({R.id.title_left_operation_layout, R.id.baza_name, R.id.baza_pers, R.id.baza_tel, R.id.baza_address, R.id.baza_remark})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.baza_name:
                showEditInfo("商铺名称", 0);
                break;
            case R.id.baza_pers:
//                showEditInfo("联系人", 1);
                break;
            case R.id.baza_tel:
//                showEditInfo("联系电话", 2);
                break;
            case R.id.baza_address:
//                showEditInfo("商铺地址", 3);
                break;
            case R.id.baza_remark:
                showEditInfo("备注", 4);
                break;
        }
    }

    void showEditInfo(final String dialogTitle, final Integer editItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_info_dialog, null);
        TextView editInfoTitle = (TextView) view.findViewById(R.id.edit_info_dialog_title);
        editInfoTitle.setText("请输入" + dialogTitle);
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
                if (!Utils.isNetUsable(EditShopInfoActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                switch (editItem) {
                    case 0://商铺名称
                        mPresenter.setShopInfos(editItem, "000000", SharedPreferencesUtils.getString(EditShopInfoActivity.this, "baza_code", ""), editInfoContent.getText().toString(), null, null, null, null, null, SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_access_token", ""));
                        break;
                    case 1://联系人
                        mPresenter.setShopInfos(editItem, "000000", SharedPreferencesUtils.getString(EditShopInfoActivity.this, "baza_code", ""), null, editInfoContent.getText().toString(), null, null, null, null, SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_access_token", ""));
                        break;
                    case 2://联系电话
                        if (!Utils.checkMobile(editInfoContent.getText().toString())) {
                            editInfoContent.setError("请输入正确的电话号码");
                            return;
                        }
                        mPresenter.setShopInfos(editItem, "000000", SharedPreferencesUtils.getString(EditShopInfoActivity.this, "baza_code", ""), null, null, editInfoContent.getText().toString(), null, null, null, SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_access_token", ""));
                        break;
                    case 3://商铺地址
                        mPresenter.setShopInfos(editItem, "000000", SharedPreferencesUtils.getString(EditShopInfoActivity.this, "baza_code", ""), null, null, null, editInfoContent.getText().toString(), null, SharedPreferencesUtils.getString(EditShopInfoActivity.this, "qLocation", ""), SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_access_token", ""));
                        break;
                    case 4://备注
                        mPresenter.setShopInfos(editItem, "000000", SharedPreferencesUtils.getString(EditShopInfoActivity.this, "baza_code", ""), null, null, null, null, editInfoContent.getText().toString(), null, SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(EditShopInfoActivity.this, "ddw_access_token", ""));
                        break;

                }
            }
        });
        builder.setView(view);
        editDialog = builder.show();
        editDialog.setCancelable(false);
    }

    @Override
    public void onGetShopInfo(ShopInfoByPersRequestResponse shopDetails) {
        bazaNameTV.setText(shopDetails.getName());
        bazaPersTV.setText(shopDetails.getPers());
        bazaTelTV.setText(shopDetails.getTel());
        bazaAddressTV.setText(shopDetails.getAddr());
        bazaRemarkTV.setText(shopDetails.getRemark());
    }

    @Override
    public void onSetShopInfos(Integer editItem) {
        EditText editInfoContentET = (EditText) editDialog.findViewById(R.id.edit_info_dialog_content);
        switch (editItem) {
            case 0://商铺名称
                bazaNameTV.setText(editInfoContentET.getText().toString());
                break;
            case 1://联系人
                bazaPersTV.setText(editInfoContentET.getText().toString());
                break;
            case 2://联系电话
                bazaTelTV.setText(editInfoContentET.getText().toString());
                break;
            case 3://商铺地址
                bazaAddressTV.setText(editInfoContentET.getText().toString());
                break;
            case 4://备注
                bazaRemarkTV.setText(editInfoContentET.getText().toString());
                break;
        }
        editDialog.dismiss();
    }

    @Override
    public void setPresenter(EditShopInfoContract.EditShopInfoPresenter presenter) {
        mPresenter = presenter;
    }
}
