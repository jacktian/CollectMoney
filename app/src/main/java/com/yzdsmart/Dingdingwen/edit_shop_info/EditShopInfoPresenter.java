package com.yzdsmart.Dingdingwen.edit_shop_info;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoByPersRequestResponse;

/**
 * Created by YZD on 2016/9/25.
 */

public class EditShopInfoPresenter implements EditShopInfoContract.EditShopInfoPresenter {
    private Context context;
    private EditShopInfoContract.EditShopInfoView mView;
    private EditShopInfoModel mModel;

    public EditShopInfoPresenter(Context context, EditShopInfoContract.EditShopInfoView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new EditShopInfoModel();
        mView.setPresenter(this);
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
    public void setShopInfos(final Integer editItem, String submitCode, String bazaCode, String bazaName, String bazaPers, String bazaTel, String bazaAddr, String remark, String coor) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.setting));
        mModel.setShopInfos(submitCode, bazaCode, bazaName, bazaPers, bazaTel, bazaAddr, remark, coor, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onSetShopInfos(editItem);
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
