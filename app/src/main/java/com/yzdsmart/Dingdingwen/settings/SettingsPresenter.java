package com.yzdsmart.Dingdingwen.settings;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCustDetailInfo(actioncode, submitCode, custCode, selfCustCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustDetailInfoRequestResponse response = (CustDetailInfoRequestResponse) result;
                if (null != response) {
                    mView.onGetCustDetailInfo(response);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
                if (err.contains("HTTP 401 Unauthorized")) {
                    MainActivity.getInstance().refreshAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
