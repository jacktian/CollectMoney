package com.yzdsmart.Collectmoney.main.personal;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.bean.User;
import com.yzdsmart.Collectmoney.http.RequestListener;

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
    public void getUserGraSta(String custcode, String submitcode) {
        mModel.getUserGraSta(custcode, submitcode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                User userGraSta = (User) result;
                if (null != userGraSta) {
                    mView.onGetUserGraSta(userGraSta.getGra(), userGraSta.getSta());
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
