package com.yzdsmart.Collectmoney.edit_personal_info;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

/**
 * Created by YZD on 2016/9/24.
 */

public class EditPersonalInfoPresenter implements EditPersonalInfoContract.EditPersonalInfoPresenter {
    private Context context;
    private EditPersonalInfoContract.EditPersonalInfoView mView;
    private EditPersonalInfoModel mModel;

    public EditPersonalInfoPresenter(Context context, EditPersonalInfoContract.EditPersonalInfoView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new EditPersonalInfoModel();
        mView.setPresenter(this);
    }

    @Override
    public void getCustDetailInfo(String actioncode, String submitCode, String custCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
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
    public void setCustDetailInfo(final Integer editItem, String submitCode, String custCode, String cName, String cNickName, String cSex, String cBirthday, String cTel, String cIdNo, String cNation, Double cHeight, Double cWeight, String cProfession, String cAddress, String cProv, String cCity, String cDist, String cCountry, String cRemark) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.setting));
        mModel.setCustDetailInfo(submitCode, custCode, cName, cNickName, cSex, cBirthday, cTel, cIdNo, cNation, cHeight, cWeight, cProfession, cAddress, cProv, cCity, cDist, cCountry, cRemark, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onSetCustDetailInfo(editItem);
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
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
