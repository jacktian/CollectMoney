package com.yzdsmart.Collectmoney.register_forget_password.set_password;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.register_forget_password.set_info.SetInfoActivity;
import com.yzdsmart.Collectmoney.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/26.
 */
public class SetPasswordActivity extends BaseActivity implements SetPasswordContract.SetPasswordView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout, R.id.user_count_down_layout, R.id.forget_pwd_link, R.id.new_user_link, R.id.user_gender_layout, R.id.user_age_layout, R.id.user_nickname_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.user_name)
    EditText userNameET;
    @Nullable
    @BindView(R.id.user_pwd)
    EditText userPwdET;
    @Nullable
    @BindView(R.id.user_confirm_pwd)
    EditText userConfirmPwdET;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button confirmButton;

    private Integer opeType;//0 注册 1 忘记密码
    private String userName;

    private static final String FIND_PWD_ACTION_CODE = "1656";

    private SetPasswordContract.SetPasswordPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable setPwdSuccessRunable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        opeType = bundle.getInt("opeType");
        userName = bundle.getString("userName");
        userNameET.setText(userName);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        toolBar.setBackgroundColor(getResources().getColor(R.color.light_yellow));
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        userNameET.setEnabled(false);

        //密码最多16位
        userPwdET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        userConfirmPwdET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

        switch (opeType) {
            case 0:
                centerTitleTV.setText(getResources().getString(R.string.register));
                confirmButton.setText(getResources().getString(R.string.register));
                break;
            case 1:
                centerTitleTV.setText(getResources().getString(R.string.forget_pwd));
                confirmButton.setText(getResources().getString(R.string.confirm));
                break;
        }

        new SetPasswordPresenter(this, this);

        setPwdSuccessRunable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                App.getAppInstance().exitApp();
                openActivity(MainActivity.class);
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_set_password;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.login_register_confirm_button})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.login_register_confirm_button:
                if (!requiredVerify(userPwdET)) {
                    userPwdET.setError(getResources().getString(R.string.input_pwd));
                    return;
                }
                if (!requiredVerify(userConfirmPwdET)) {
                    userConfirmPwdET.setError(getResources().getString(R.string.input_pwd));
                    return;
                }
                if (!userPwdET.getText().toString().equals(userConfirmPwdET.getText().toString())) {
                    userConfirmPwdET.setError(getResources().getString(R.string.confirm_pwd_different));
                    return;
                }
                if (userPwdET.getText().toString().trim().length() < 8) {
                    userPwdET.setError(getResources().getString(R.string.min_pwd_length));
                    return;
                }
                boolean allNumber = true;
                for (int acc_query = 0; acc_query < userPwdET.getText().toString().trim().length(); ++acc_query) {
                    char req_con = userPwdET.getText().toString().trim().charAt(acc_query);
                    if (req_con != 46 && req_con != 95 && !Character.isLetterOrDigit(req_con)) {
                        return;
                    }
                    if (!Character.isDigit(req_con)) {
                        allNumber = false;
                    }
                }
                if (allNumber) {
                    userPwdET.setError(getResources().getString(R.string.number_pwd));
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                switch (opeType) {
                    case 0:
                        bundle = new Bundle();
                        bundle.putString("userName", userNameET.getText().toString());
                        bundle.putString("password", userPwdET.getText().toString());
                        openActivity(SetInfoActivity.class, bundle, 0);
                        break;
                    case 1:
                        mPresenter.setPassword(FIND_PWD_ACTION_CODE, userName, userPwdET.getText().toString(), Utils.md5(FIND_PWD_ACTION_CODE + "yzd" + userName));
                        break;
                }
                break;
        }
    }

    @Optional
    @OnTextChanged(R.id.user_confirm_pwd)
    void onTextChanged() {
        if (!userPwdET.getText().toString().equals(userConfirmPwdET.getText().toString())) {
            userConfirmPwdET.setError(getResources().getString(R.string.confirm_pwd_different));
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(setPwdSuccessRunable);
        super.onDestroy();
    }

    @Override
    public void setPresenter(SetPasswordContract.SetPasswordPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSetPassword(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        showProgressDialog(R.drawable.success, getResources().getString(R.string.set_success));
        mHandler.postDelayed(setPwdSuccessRunable, 500);
    }
}
