package com.yzdsmart.Collectmoney.main.personal;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustLevelRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;

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
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
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
    public void getShopInfo(String actioncode, String submitCode, String bazaCode, String custCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopInfo(actioncode, submitCode, bazaCode, custCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopInfoRequestResponse shopInfo = (ShopInfoRequestResponse) result;
                if (null != shopInfo) {
                    mView.onGetShopInfo(shopInfo);
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
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
