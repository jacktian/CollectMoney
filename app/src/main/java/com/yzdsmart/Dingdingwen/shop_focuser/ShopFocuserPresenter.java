package com.yzdsmart.Dingdingwen.shop_focuser;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.ShopFocuserRequestResponse;

/**
 * Created by YZD on 2016/9/20.
 */

public class ShopFocuserPresenter implements ShopFocuserContract.ShopFocuserPresenter {
    private Context context;
    private ShopFocuserContract.ShopFocuserView mView;
    private ShopFocuserModel mModel;

    public ShopFocuserPresenter(Context context, ShopFocuserContract.ShopFocuserView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new ShopFocuserModel();
        mView.setPresenter(this);
    }

    @Override
    public void getShopFocuser(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopFocuser(action, submitCode, bazaCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopFocuserRequestResponse requestResponse = (ShopFocuserRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    if (requestResponse.getLists().size() > 0) {
                        mView.onGetShopFocuser(requestResponse.getLists());
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
