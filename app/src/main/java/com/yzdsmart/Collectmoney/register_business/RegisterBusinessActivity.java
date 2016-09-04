package com.yzdsmart.Collectmoney.register_business;

import android.os.Bundle;

import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.main.MainActivity;

/**
 * Created by YZD on 2016/9/4.
 */
public class RegisterBusinessActivity extends BaseActivity implements RegisterBusinessContract.RegisterBusinessView {

    private RegisterBusinessContract.RegisterBusinessPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RegisterBusinessPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register_business;
    }

    @Override
    public void setPresenter(RegisterBusinessContract.RegisterBusinessPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRegisterBusiness(boolean flag, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        App.getAppInstance().exitApp();
        openActivity(MainActivity.class);
    }
}
