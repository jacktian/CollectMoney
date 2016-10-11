package com.yzdsmart.Collectmoney.personal_coin_list;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.GetCoinsLogRequestResponse;

/**
 * Created by YZD on 2016/9/5.
 */
public class PersonalCoinsPresenter implements PersonalCoinsContract.PersonalCoinsPresenter {
    private Context context;
    private PersonalCoinsContract.PersonalCoinsView mView;
    private PersonalCoinsModel mModel;

    public PersonalCoinsPresenter(Context context, PersonalCoinsContract.PersonalCoinsView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new PersonalCoinsModel();
        mView.setPresenter(this);
    }

    @Override
    public void getCoinsLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCoinsLog(action, submitCode, custCode, pageIndex, pageSize, lastsequence, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetCoinsLogRequestResponse response = (GetCoinsLogRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onGetCoinsLog(response.getLists(), response.getLastsequence());
                } else {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
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
