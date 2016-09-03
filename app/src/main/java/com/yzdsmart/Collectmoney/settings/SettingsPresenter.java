package com.yzdsmart.Collectmoney.settings;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;

/**
 * Created by YZD on 2016/9/3.
 */
public class SettingsPresenter implements SettingsContract.SettingsPresenter {
    private Context context;
    private SettingsContract.SettingView mView;
    private SettingsModel mModel;

    public SettingsPresenter(Context context, SettingsContract.SettingView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new SettingsModel();
        mView.setPresenter(this);
    }

    @Override
    public void getCustDetailInfo(String actioncode, String submitCode, String custCode) {
        mModel.getCustDetailInfo(actioncode, submitCode, custCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustDetailInfoRequestResponse response = (CustDetailInfoRequestResponse) result;
                if (null != response) {
                    mView.onGetCustDetailInfo(response);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
