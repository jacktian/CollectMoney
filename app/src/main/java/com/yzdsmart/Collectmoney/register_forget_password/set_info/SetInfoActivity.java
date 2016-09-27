package com.yzdsmart.Collectmoney.register_forget_password.set_info;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.utils.Utils;
import com.yzdsmart.Collectmoney.views.BetterSpinner;

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
    @BindViews({R.id.user_count_down_layout, R.id.user_pwd_layout, R.id.user_confirm_pwd_layout, R.id.forget_pwd_link, R.id.new_user_link})
    List<View> hideViews;
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
    @BindView(R.id.user_gender)
    BetterSpinner userGenderSpinner;
    @Nullable
    @BindView(R.id.user_age)
    EditText userAgeET;
    @Nullable
    @BindView(R.id.user_nickname)
    EditText nickNameET;
    @Nullable
    @BindView(R.id.login_register_confirm_button)
    Button confirmButton;

    private static final String REG_ACTION_CODE = "1688";

    private String userName, password;

    private String[] genderArray;
    private ArrayAdapter<String> genderAdapter;

    private SetInfoContract.SetInfoPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable registerSuccessRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        password = bundle.getString("password");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText(getResources().getString(R.string.register));
        confirmButton.setText(getResources().getString(R.string.register));
        userNameET.setEnabled(false);
        userNameET.setText(userName);

        genderArray = getResources().getStringArray(R.array.gender_array);
        genderAdapter = new ArrayAdapter<String>(this,
                R.layout.gender_array_item, genderArray);
        userGenderSpinner.setAdapter(genderAdapter);
        userGenderSpinner.setText(genderArray[0]);

        new SetInfoPresenter(this, this);

        registerSuccessRunnable = new Runnable() {
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
        return R.layout.activity_set_info;
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
    @OnClick({R.id.title_left_operation_layout, R.id.login_register_confirm_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
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
                mPresenter.userRegister(REG_ACTION_CODE, userName, password, userGenderSpinner.getText().toString(), Integer.valueOf(userAgeET.getText().toString()), nickNameET.getText().toString(), Utils.md5(REG_ACTION_CODE + "yzd" + userName));
                break;
        }
    }

    @Override
    public void onUserRegister() {
        showProgressDialog(R.drawable.success, getResources().getString(R.string.register_success));
        mHandler.postDelayed(registerSuccessRunnable, 500);
    }

    @Override
    public void setPresenter(SetInfoContract.SetInfoPresenter presenter) {
        mPresenter = presenter;
    }
}
