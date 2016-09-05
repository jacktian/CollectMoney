package com.yzdsmart.Collectmoney.main.personal;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustLevelRequestResponse;

/**
 * Created by YZD on 2016/8/27.
 */
public class PersonalPresenter implements PersonalContract.PersonalPresenter {
    private Context context;
    private PersonalContract.PersonalView mView;
    private PersonalModel mModel;

    public PersonalPresenter(Context context, PersonalContract.PersonalView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new PersonalModel();
        mView.setPresenter(this);
    }

    @Override
    public void getCustLevel(String custcode, String submitcode) {
        mModel.getCustLevel(custcode, submitcode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustLevelRequestResponse response = (CustLevelRequestResponse) result;
                if (null != response) {
                    mView.onGetCustLevel(response.getGra(), response.getSta());
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
    public void getCustInfo(String submitcode, String custCode) {
        mModel.getCustInfo(submitcode, custCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustInfoRequestResponse requestResponse = (CustInfoRequestResponse) result;
                String name;
                if (null != requestResponse.getCName() && !"".equals(requestResponse.getCName())) {
                    name = requestResponse.getCName();
                } else if (null != requestResponse.getNickName() && !"".equals(requestResponse.getNickName())) {
                    name = requestResponse.getNickName();
                } else {
                    name = requestResponse.getC_UserCode();
                }
                mView.onGetCustInfo(name, requestResponse.getImageUrl() == null ? "" : requestResponse.getImageUrl(), requestResponse.getGoldNum());
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
