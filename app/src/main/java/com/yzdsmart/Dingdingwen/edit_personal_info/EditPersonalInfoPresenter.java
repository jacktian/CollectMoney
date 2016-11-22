package com.yzdsmart.Dingdingwen.edit_personal_info;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getCustInfo(String submitcode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCustInfo(submitcode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustInfoRequestResponse requestResponse = (CustInfoRequestResponse) result;
                if (null != requestResponse) {
                    mView.onGetCustInfo(requestResponse);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_cust_info));
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
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_cust_detail_info));
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
    public void setCustDetailInfo(final Integer editItem, String submitCode, String custCode, String cName, String cNickName, String cSex, String cBirthday, String cTel, String cIdNo, String cNation, Double cHeight, Double cWeight, String cProfession, String cAddress, String cProv, String cCity, String cDist, String cCountry, String cRemark, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.setting));
        mModel.setCustDetailInfo(submitCode, custCode, cName, cNickName, cSex, cBirthday, cTel, cIdNo, cNation, cHeight, cWeight, cProfession, cAddress, cProv, cCity, cDist, cCountry, cRemark, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
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
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_cust_detail_info));
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
