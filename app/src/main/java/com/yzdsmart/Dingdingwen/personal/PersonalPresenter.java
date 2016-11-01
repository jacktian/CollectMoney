package com.yzdsmart.Dingdingwen.personal;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustLevelRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoByPersRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.UploadFileRequestResponse;

/**
 * Created by YZD on 2016/10/25.
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
    public void getCustLevel(String code, String submitcode, String action) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCustLevel(code, submitcode, action, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustLevelRequestResponse response = (CustLevelRequestResponse) result;
                if (null != response) {
                    mView.onGetCustLevel(response.getGra(), response.getSta());
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
    public void getCustInfo(String submitcode, String custCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
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
    public void getShopInfo(String actioncode, String submitCode, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopInfo(actioncode, submitCode, bazaCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopInfoByPersRequestResponse requestResponse = (ShopInfoByPersRequestResponse) result;
                if (null != requestResponse) {
                    mView.onGetShopInfo(requestResponse);
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
    public void getShopGalley(String action, String submitCode, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopGalley(action, submitCode, bazaCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetGalleyRequestResponse requestResponse = (GetGalleyRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    if (null != requestResponse.getLists()) {
                        mView.onGetShopGalley(requestResponse.getLists());
                    }
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
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void uploadShopAvater(String action, String fileName, String fileData, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.uploading));
        mModel.uploadShopAvater(action, fileName, fileData, bazaCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                UploadFileRequestResponse requestResponse = (UploadFileRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onUploadShopAvater(requestResponse.getRelaImageUrl());
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
