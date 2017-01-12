package com.yzdsmart.Dingdingwen.bind_bank_card;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ValidateBankCardRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/12/14.
 */

public class BindBankCardPresenter implements BindBankCardContract.BindBankCardPresenter {
    private Context context;
    private BindBankCardContract.BindBankCardView mView;
    private BindBankCardModel mModel;

    public BindBankCardPresenter(Context context, BindBankCardContract.BindBankCardView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new BindBankCardModel();
        mView.setPresenter(this);
    }

    @Override
    public void bindBankCard(String submitCode, String custCode, String bankCode, String bankCardNum, String custName, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.bind_bank_binding));
        mModel.bindBankCard(submitCode, custCode, bankCode, bankCardNum, custName, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onBindBankCard();
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_bind_bank_card));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }


    @Override
    public void validateBankCard(String submitCode, String bankCardNum, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.withdrawing));
        mModel.validateBankCard(submitCode, bankCardNum, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ValidateBankCardRequestResponse requestResponse = (ValidateBankCardRequestResponse) result;
                if (requestResponse.getValidated()) {
                    mView.onValidateBankCard(requestResponse.getBank());
                } else {
                    ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.bind_bank_card_fail));
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_validate_bank_card));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
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
