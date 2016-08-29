package com.yzdsmart.Collectmoney.personal_friend_detail;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustLevelRequestResponse;

/**
 * Created by YZD on 2016/8/29.
 */
public class PersonalFriendDetailPresenter implements PersonalFriendDetailContract.PersonalFriendDetailPresenter {
    private Context context;
    private PersonalFriendDetailContract.PersonalFriendDetailView mView;
    private PersonalFriendDetailModel mModel;

    public PersonalFriendDetailPresenter(Context context, PersonalFriendDetailContract.PersonalFriendDetailView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new PersonalFriendDetailModel();
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
                if (null != requestResponse) {
                    mView.onGetCustInfo(requestResponse);
                }
            }

            @Override
            public void onError(String err) {

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
